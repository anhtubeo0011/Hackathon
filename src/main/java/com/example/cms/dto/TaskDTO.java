package com.example.cms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class TaskDTO {
  private Long id;
  private String title;
  private String description;
  private Long projectId;
  private Long assigneeId;
  private String status;
  private String priority;
  private Integer estimateHours;
  private LocalDate startDate;
  private LocalDate deadline;
  private String requiredSkills;
}
