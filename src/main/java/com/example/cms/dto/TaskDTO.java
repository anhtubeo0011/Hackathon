package com.example.cms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
  private Long id;
  private Long projectId;
  private String projectName;
  private Long employeeId;
  private String employeeName;
  private Date startDate;
  private Date dueDate;
  private String status;
  private String description;
  private String kind;
  private String level;
}
