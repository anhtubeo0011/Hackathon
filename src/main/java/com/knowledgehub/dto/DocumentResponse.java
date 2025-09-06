package com.knowledgehub.dto;

import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentResponse {
    private String id;
    private String title;
    private String content;
    private String summary;
    private List<String> tags;
    private String authorId;
    private AuthorInfo author;
    private String privacy;
    private String fileUrl;
    private String fileType;
    private Long fileSize;
    private Integer views;
    private Double averageRating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DocumentResponse(Document document) {
        this.id = document.getId().toString();
        this.title = document.getTitle();
        this.content = document.getContent();
        this.summary = document.getSummary();
        this.tags = document.getTags();
        this.authorId = document.getAuthor().getId().toString();
        this.author = new AuthorInfo(document.getAuthor());
        this.privacy = document.getPrivacy().name().toLowerCase();
        this.fileUrl = document.getFileUrl();
        this.fileType = document.getFileType();
        this.fileSize = document.getFileSize();
        this.views = document.getViews();
        this.averageRating = document.getAverageRating();
        this.createdAt = document.getCreatedAt();
        this.updatedAt = document.getUpdatedAt();
    }

    public static class AuthorInfo {
        private String id;
        private String username;
        private String fullName;
        private String avatar;
        private String group;

        public AuthorInfo(User user) {
            this.id = user.getId().toString();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
            this.avatar = user.getAvatar();
            this.group = user.getGroupName();
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }

        public String getGroup() { return group; }
        public void setGroup(String group) { this.group = group; }
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }

    public AuthorInfo getAuthor() { return author; }
    public void setAuthor(AuthorInfo author) { this.author = author; }

    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Integer getViews() { return views; }
    public void setViews(Integer views) { this.views = views; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}