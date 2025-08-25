package com.example.cms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class DocumentDTO {
    private Long id;
    private String documentId;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Error type is required")
    private String errorType;
    
    @NotBlank(message = "Content is required")
    private String content;
    
    @NotNull(message = "Priority is required")
    private String priority;
    
    @NotBlank(message = "Created by is required")
    private String createdBy;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> attachments;
    private List<String> viewPermissions;

    // Constructors
    public DocumentDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getErrorType() { return errorType; }
    public void setErrorType(String errorType) { this.errorType = errorType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<String> getAttachments() { return attachments; }
    public void setAttachments(List<String> attachments) { this.attachments = attachments; }

    public List<String> getViewPermissions() { return viewPermissions; }
    public void setViewPermissions(List<String> viewPermissions) { this.viewPermissions = viewPermissions; }
}