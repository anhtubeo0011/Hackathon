package com.example.cms.controller;

import com.example.cms.dto.IncidentDTO;
import com.example.cms.dto.AIAdviceDTO;
import com.example.cms.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incidents")
@CrossOrigin(origins = "*")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        List<IncidentDTO> incidents = incidentService.getAllIncidents();
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable Long id) {
        return incidentService.getIncidentById(id)
                .map(incident -> ResponseEntity.ok(incident))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<IncidentDTO> createIncident(@Valid @RequestBody IncidentDTO incidentDTO) {
        IncidentDTO createdIncident = incidentService.createIncident(incidentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncident);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentDTO> updateIncident(@PathVariable Long id, @Valid @RequestBody IncidentDTO incidentDTO) {
        return incidentService.updateIncident(id, incidentDTO)
                .map(incident -> ResponseEntity.ok(incident))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        if (incidentService.deleteIncident(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<IncidentDTO>> searchIncidents(@RequestParam String keyword) {
        List<IncidentDTO> incidents = incidentService.searchIncidents(keyword);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByStatus(@PathVariable String status) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByStatus(status);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<IncidentDTO>> getIncidentsByPriority(@PathVariable String priority) {
        List<IncidentDTO> incidents = incidentService.getIncidentsByPriority(priority);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getIncidentStatistics() {
        Map<String, Long> statistics = incidentService.getIncidentStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/{id}/ai-advice")
    public ResponseEntity<AIAdviceDTO> getAIAdvice(@PathVariable Long id) {
        AIAdviceDTO advice = incidentService.getAIAdvice(id);
        if (advice != null) {
            return ResponseEntity.ok(advice);
        }
        return ResponseEntity.notFound().build();
    }
}