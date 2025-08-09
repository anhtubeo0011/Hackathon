package com.example.cms.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDTO {
  private Long id;
  private String name;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;
  private List<TaskDTO> tasks;
}