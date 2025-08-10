package com.example.cms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentActivity {
  private String type;
  private String status;
  private LocalDateTime timestamp;
  private LocalDateTime updateTime;
}
