package com.example.cms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PROJECT")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Temporal(TemporalType.DATE)
  @Column(name = "START_DATE")
  private Date startDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "DESCRIPTION")
  private String description;
}
