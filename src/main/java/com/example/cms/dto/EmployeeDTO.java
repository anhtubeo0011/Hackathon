package com.example.cms.dto;

import com.example.cms.entity.Strength;
import com.example.cms.entity.Weakness;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeDTO {
  private Long id;
  private String name;
  private String mail;
  private Strength strength;
  private Weakness weakness;
  private List<TaskDTO> tasks;
}
