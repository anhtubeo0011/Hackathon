package com.example.cms.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmployeeDashboardDTO {
  private Long id;
  private String name;
  private String mail;
  private String strength;
  private String weakness;
  private List<TaskDTO> tasks;
}
