package com.example.cms.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDTO {
  private Long id;
  private Long projectId;
  private ProjectDTO projectDTO;
  private Long employeeId;
  private EmployeeDTO employeeDTO;
  private Date startDate;
  private Date dueDate;
  private String status;
  private String description;
  private String kind;
  private String level;
}
