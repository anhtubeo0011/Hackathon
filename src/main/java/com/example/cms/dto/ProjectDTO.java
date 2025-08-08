package com.example.cms.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ProjectDTO {
  private Long id;
  private String name;
  private Date startDate;
  private Date endDate;
  private String description;
  private List<TaskDTO> tasks;
}