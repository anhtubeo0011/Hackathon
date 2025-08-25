package com.example.cms.service;

import com.example.cms.dto.DeveloperDTO;
import com.example.cms.entity.Developer;
import com.example.cms.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public List<DeveloperDTO> getAllDevelopers() {
        return developerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<DeveloperDTO> getDeveloperById(Long id) {
        return developerRepository.findById(id)
                .map(this::convertToDTO);
    }

    public DeveloperDTO createDeveloper(DeveloperDTO developerDTO) {
        Developer developer = convertToEntity(developerDTO);
        Developer savedDeveloper = developerRepository.save(developer);
        return convertToDTO(savedDeveloper);
    }

    public Optional<DeveloperDTO> updateDeveloper(Long id, DeveloperDTO developerDTO) {
        return developerRepository.findById(id)
                .map(developer -> {
                    developer.setName(developerDTO.getName());
                    developer.setEmail(developerDTO.getEmail());
                    if (developerDTO.getTotalResolved() != null) {
                        developer.setTotalResolved(developerDTO.getTotalResolved());
                    }
                    return convertToDTO(developerRepository.save(developer));
                });
    }

    public boolean deleteDeveloper(Long id) {
        if (developerRepository.existsById(id)) {
            developerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<DeveloperDTO> getTopDevelopers() {
        return developerRepository.findTopDevelopersByResolved().stream()
                .limit(5)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DeveloperDTO convertToDTO(Developer developer) {
        return new DeveloperDTO(
                developer.getId(),
                developer.getName(),
                developer.getEmail(),
                developer.getActiveIncidents(),
                developer.getTotalResolved()
        );
    }

    private Developer convertToEntity(DeveloperDTO dto) {
        Developer developer = new Developer();
        developer.setName(dto.getName());
        developer.setEmail(dto.getEmail());
        if (dto.getTotalResolved() != null) {
            developer.setTotalResolved(dto.getTotalResolved());
        }
        return developer;
    }
}