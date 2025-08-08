package com.example.cms.entity;

import jakarta.persistence.*;
import lombok.*;

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

  @Column(name = "PROJECT_ID")
  private Long projectId;

  @Column(name = "EMPLOYEE_ID")
  private Long employeeId;

  @Temporal(TemporalType.DATE)
  @Column(name = "START_DATE")
  private Date startDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "DUE_DATE")
  private Date dueDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "KIND")
  private String kind;

  @Column(name = "STAGE")
  private String stage;
}