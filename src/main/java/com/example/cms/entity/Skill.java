package com.example.cms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Skill {
  private List<String> skills;
}
