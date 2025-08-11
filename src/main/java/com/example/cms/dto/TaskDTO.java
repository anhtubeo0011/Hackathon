package com.example.cms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TaskDTO {
  private Long id;
  private String title;
  private String description;
  private Long projectId;
  private String projectName;
  private Long assigneeId;
  private String assigneeName;
  private String status;
  private String priority;
  private Integer estimateHours;
  private LocalDate startDate;
  private LocalDate deadline;
  private String requiredSkills;
  private LocalDateTime updateTime;
}
