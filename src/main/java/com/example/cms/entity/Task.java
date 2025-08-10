package com.example.cms.entity;

import com.example.cms.util.JsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TASK")
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "PROJECT_ID")
  private Long projectId;

  @Column(name = "EMPLOYEE_ID")
  private Long employeeId;

  @Temporal(TemporalType.DATE)
  @Column(name = "START_DATE")
  private LocalDate startDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "DUE_DATE")
  private LocalDate dueDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PRIORITY")
  private String priority;

  @Column(name = "REQUIRED_SKILLS")
//  @Convert(converter = JsonConverter.SkillConverter.class)
  private String requiredSkill;

  @Column(name = "ESTIMATE_HOURS")
  private Integer estimateHours;
}