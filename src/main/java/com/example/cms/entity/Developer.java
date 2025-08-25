package com.example.cms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "developers")
public class Developer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "assignedDev", fetch = FetchType.LAZY)
    private List<Incident> assignedIncidents;

    private Integer totalResolved = 0;

    // Constructors
    public Developer() {}

    public Developer(String name, String email) {
        this.name = name;
        this.email = email;
        this.totalResolved = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Incident> getAssignedIncidents() { return assignedIncidents; }
    public void setAssignedIncidents(List<Incident> assignedIncidents) { this.assignedIncidents = assignedIncidents; }

    public Integer getTotalResolved() { return totalResolved; }
    public void setTotalResolved(Integer totalResolved) { this.totalResolved = totalResolved; }

    public int getActiveIncidents() {
        if (assignedIncidents == null) return 0;
        return (int) assignedIncidents.stream()
                .filter(incident -> incident.getStatus() != IncidentStatus.RESOLVED && 
                                  incident.getStatus() != IncidentStatus.CLOSED)
                .count();
    }
}