package com.example.cms.entity;

import com.example.cms.util.JsonConverter;
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

  @Column(name = "STRENGTH", columnDefinition = "VARCHAR2(4000)")
  @Convert(converter = JsonConverter.StrengthConverter.class)
  private Strength strength;

  @Column(name = "WEAKNESS", columnDefinition = "VARCHAR2(4000)")
  @Convert(converter = JsonConverter.WeaknessConverter.class)
  private Weakness weakness;
}