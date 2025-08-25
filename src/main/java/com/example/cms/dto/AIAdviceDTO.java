package com.example.cms.dto;

import java.util.List;

public class AIAdviceDTO {
    private String suggestion;
    private Integer confidence;
    private List<String> relatedDocs;

    // Constructors
    public AIAdviceDTO() {}

    public AIAdviceDTO(String suggestion, Integer confidence, List<String> relatedDocs) {
        this.suggestion = suggestion;
        this.confidence = confidence;
        this.relatedDocs = relatedDocs;
    }

    // Getters and Setters
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }

    public Integer getConfidence() { return confidence; }
    public void setConfidence(Integer confidence) { this.confidence = confidence; }

    public List<String> getRelatedDocs() { return relatedDocs; }
    public void setRelatedDocs(List<String> relatedDocs) { this.relatedDocs = relatedDocs; }
}