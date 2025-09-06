package com.knowledgehub.dto;

import com.knowledgehub.entity.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public class DocumentRequest {
    @NotBlank
    @Size(max = 255)
    private String title;

    private String content;

    @Size(max = 500)
    private String summary;

    private List<String> tags;

    private Document.Privacy privacy = Document.Privacy.GROUP;

    private String fileUrl;
    private String fileType;
    private Long fileSize;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Document.Privacy getPrivacy() { return privacy; }
    public void setPrivacy(Document.Privacy privacy) { this.privacy = privacy; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
}