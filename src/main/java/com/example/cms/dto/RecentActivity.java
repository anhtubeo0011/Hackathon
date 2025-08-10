package com.example.cms.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentActivity {
  private Long id;
  private String type;
  private String description;
  private LocalDateTime timestamp;
}
