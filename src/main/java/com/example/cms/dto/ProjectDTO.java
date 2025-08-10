package com.example.cms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDTO {
  private Long id;
  private String name;
  private String client;
  private String pm;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
  private float progress;
  private String riskLevel;
  private int teamSize;
  private int taskCount;
  private String description;
}