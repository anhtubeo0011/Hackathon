package com.example.cms.entity;

import lombok.Data;

@Data
public class Strength {
  private Integer id;
  private String name;
  private String category;
  private Integer level;
  private boolean verified;
}
