package com.example.cms.controller;

import com.example.cms.dto.ProjectDTO;
import com.example.cms.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

  @Autowired
  private ProjectService service;

  @PostMapping
  public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<ProjectDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProjectDTO> update(@PathVariable Long id, @RequestBody ProjectDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
