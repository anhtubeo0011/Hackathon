package com.example.cms.dto;

import lombok.Data;

@Data
public class DashboardDTO {
  private Integer totalTasks;
  private Float onTimeRate;
  private Integer overDueTasks;
  private Integer overloadedDevs;
  private Integer projectAtRisk;
}
