package com.example.cms.entity;

public enum IncidentStatus {
    NEW("new"),
    IN_PROGRESS("in-progress"),
    RESOLVED("resolved"),
    CLOSED("closed");

    private final String value;

    IncidentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static IncidentStatus fromValue(String value) {
        for (IncidentStatus status : IncidentStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}