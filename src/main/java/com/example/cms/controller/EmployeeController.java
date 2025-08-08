package com.example.cms.controller;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.service.impl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

  @Autowired
  private EmployeeServiceImpl service;

  @PostMapping
  public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<EmployeeDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @RequestBody EmployeeDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
