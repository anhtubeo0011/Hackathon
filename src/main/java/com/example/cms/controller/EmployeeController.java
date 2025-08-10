package com.example.cms.controller;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

  @Autowired
  private EmployeeService service;

  @PostMapping
  public ResponseEntity<EmployeeDTO> create(@RequestBody EmployeeDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<EmployeeDTO>> findAll(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String mail,
                                                   @RequestParam(required = false) String skill,
                                                   @RequestParam(required = false) Long projectId) {
    return ResponseEntity.ok(service.findAll(name, mail, skill, projectId));
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
