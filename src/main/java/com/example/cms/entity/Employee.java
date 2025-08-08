package com.example.cms.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMPLOYEE")
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "MAIL")
  private String mail;

  @Column(name = "STRENGTH")
  private String strength;

  @Column(name = "WEAKNESS")
  private String weakness;
}