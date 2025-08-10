package com.example.cms.dto;

import com.example.cms.entity.Skill;
import com.example.cms.entity.Strength;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {
  private Long id;
  private String name;
  private String mail;
  private String avatar;
  private Integer workloadHours;
  private Integer maxCapacity;
  private Float completionRate;
  private Float avgTaskTime;
  private List<String> activeProjects;
  private List<Strength> skills;
  private RecentActivity recentActivity;
}
