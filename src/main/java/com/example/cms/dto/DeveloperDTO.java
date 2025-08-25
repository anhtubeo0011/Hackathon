package com.example.cms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DeveloperDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    private Integer activeIncidents;
    private Integer totalResolved;

    // Constructors
    public DeveloperDTO() {}

    public DeveloperDTO(Long id, String name, String email, Integer activeIncidents, Integer totalResolved) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.activeIncidents = activeIncidents;
        this.totalResolved = totalResolved;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getActiveIncidents() { return activeIncidents; }
    public void setActiveIncidents(Integer activeIncidents) { this.activeIncidents = activeIncidents; }

    public Integer getTotalResolved() { return totalResolved; }
    public void setTotalResolved(Integer totalResolved) { this.totalResolved = totalResolved; }
}