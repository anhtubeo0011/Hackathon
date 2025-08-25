package com.example.cms.service;

import com.example.cms.dto.IncidentDTO;
import com.example.cms.dto.AIAdviceDTO;
import com.example.cms.entity.Incident;
import com.example.cms.entity.IncidentStatus;
import com.example.cms.entity.Priority;
import com.example.cms.entity.Developer;
import com.example.cms.repository.IncidentRepository;
import com.example.cms.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

@Service
@Transactional
public class IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private DocumentService documentService;

    public List<IncidentDTO> getAllIncidents() {
        return incidentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<IncidentDTO> getIncidentById(Long id) {
        return incidentRepository.findById(id)
                .map(this::convertToDTO);
    }

    public IncidentDTO createIncident(IncidentDTO incidentDTO) {
        Incident incident = convertToEntity(incidentDTO);
        
        // Generate incident ID
        String incidentId = generateIncidentId();
        incident.setIncidentId(incidentId);
        
        // Set due date (3 days from now)
        incident.setDueDate(LocalDateTime.now().plusDays(3));
        
        // Assign developer if provided
        if (incidentDTO.getAssignedDev() != null) {
            Optional<Developer> developer = developerRepository.findById(Long.parseLong(incidentDTO.getAssignedDev()));
            developer.ifPresent(incident::setAssignedDev);
        }
        
        Incident savedIncident = incidentRepository.save(incident);
        return convertToDTO(savedIncident);
    }

    public Optional<IncidentDTO> updateIncident(Long id, IncidentDTO incidentDTO) {
        return incidentRepository.findById(id)
                .map(incident -> {
                    incident.setTitle(incidentDTO.getTitle());
                    incident.setDescription(incidentDTO.getDescription());
                    incident.setStatus(IncidentStatus.fromValue(incidentDTO.getStatus()));
                    incident.setPriority(Priority.fromValue(incidentDTO.getPriority()));
                    incident.setErrorType(incidentDTO.getErrorType());
                    
                    if (incidentDTO.getAssignedDev() != null) {
                        Optional<Developer> developer = developerRepository.findById(Long.parseLong(incidentDTO.getAssignedDev()));
                        developer.ifPresent(incident::setAssignedDev);
                    }
                    
                    return convertToDTO(incidentRepository.save(incident));
                });
    }

    public boolean deleteIncident(Long id) {
        if (incidentRepository.existsById(id)) {
            incidentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<IncidentDTO> searchIncidents(String keyword) {
        return incidentRepository.findByKeyword(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<IncidentDTO> getIncidentsByStatus(String status) {
        IncidentStatus incidentStatus = IncidentStatus.fromValue(status);
        return incidentRepository.findByStatus(incidentStatus).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<IncidentDTO> getIncidentsByPriority(String priority) {
        Priority incidentPriority = Priority.fromValue(priority);
        return incidentRepository.findByPriority(incidentPriority).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getIncidentStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) incidentRepository.findAll().size());
        stats.put("new", incidentRepository.countByStatus(IncidentStatus.NEW));
        stats.put("inProgress", incidentRepository.countByStatus(IncidentStatus.IN_PROGRESS));
        stats.put("resolved", incidentRepository.countByStatus(IncidentStatus.RESOLVED));
        stats.put("closed", incidentRepository.countByStatus(IncidentStatus.CLOSED));
        stats.put("overdue", (long) incidentRepository.findOverdueIncidents(LocalDateTime.now()).size());
        
        // Priority statistics
        stats.put("critical", incidentRepository.countByPriority(Priority.CRITICAL));
        stats.put("high", incidentRepository.countByPriority(Priority.HIGH));
        stats.put("medium", incidentRepository.countByPriority(Priority.MEDIUM));
        stats.put("low", incidentRepository.countByPriority(Priority.LOW));
        
        return stats;
    }

    public AIAdviceDTO getAIAdvice(Long incidentId) {
        Optional<Incident> incidentOpt = incidentRepository.findById(incidentId);
        if (incidentOpt.isEmpty()) {
            return null;
        }
        
        Incident incident = incidentOpt.get();
        String errorType = incident.getErrorType();
        
        // Get related documents
        List<String> relatedDocs = documentService.getDocumentsByErrorType(errorType)
                .stream()
                .limit(2)
                .map(doc -> doc.getDocumentId())
                .collect(Collectors.toList());
        
        // Generate AI suggestion based on error type
        String suggestion = generateAISuggestion(errorType);
        
        // Generate confidence score (70-99%)
        int confidence = 70 + (int) (Math.random() * 30);
        
        return new AIAdviceDTO(suggestion, confidence, relatedDocs);
    }

    private String generateAISuggestion(String errorType) {
        Map<String, String> suggestions = new HashMap<>();
        suggestions.put("Authentication", "Kiểm tra token expiration và refresh token mechanism. Verify database user credentials và session management.");
        suggestions.put("Database", "Monitor connection pool status. Thực hiện query optimization và check for missing indexes. Consider caching frequently accessed data.");
        suggestions.put("Frontend", "Test responsive design trên multiple devices. Kiểm tra CSS media queries và JavaScript errors in console.");
        suggestions.put("Backend", "Profile API endpoints để identify bottlenecks. Implement proper error handling và logging.");
        suggestions.put("Email", "Check SMTP configuration và email templates. Verify email queue processing và delivery status.");
        
        return suggestions.getOrDefault(errorType, "Phân tích log files và reproduce issue trong development environment.");
    }

    private String generateIncidentId() {
        long count = incidentRepository.count() + 1;
        return String.format("INC%03d", count);
    }

    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setIncidentId(incident.getIncidentId());
        dto.setTitle(incident.getTitle());
        dto.setDescription(incident.getDescription());
        dto.setStatus(incident.getStatus().getValue());
        dto.setPriority(incident.getPriority().getValue());
        dto.setErrorType(incident.getErrorType());
        dto.setCreatedAt(incident.getCreatedAt());
        dto.setUpdatedAt(incident.getUpdatedAt());
        dto.setDueDate(incident.getDueDate());
        dto.setAttachments(incident.getAttachments());
        
        if (incident.getAssignedDev() != null) {
            dto.setAssignedDev(incident.getAssignedDev().getId().toString());
            dto.setAssignedDevInfo(convertDeveloperToDTO(incident.getAssignedDev()));
        }
        
        return dto;
    }

    private Incident convertToEntity(IncidentDTO dto) {
        Incident incident = new Incident();
        incident.setTitle(dto.getTitle());
        incident.setDescription(dto.getDescription());
        incident.setStatus(dto.getStatus() != null ? IncidentStatus.fromValue(dto.getStatus()) : IncidentStatus.NEW);
        incident.setPriority(Priority.fromValue(dto.getPriority()));
        incident.setErrorType(dto.getErrorType());
        incident.setAttachments(dto.getAttachments());
        return incident;
    }

    private com.example.cms.dto.DeveloperDTO convertDeveloperToDTO(Developer developer) {
        return new com.example.cms.dto.DeveloperDTO(
                developer.getId(),
                developer.getName(),
                developer.getEmail(),
                developer.getActiveIncidents(),
                developer.getTotalResolved()
        );
    }
}