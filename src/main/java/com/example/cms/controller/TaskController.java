package com.example.cms.controller;

import com.example.cms.dto.TaskDTO;
import com.example.cms.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  @Autowired
  private TaskService service;

  @PostMapping
  public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<TaskDTO>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDTO> update(@PathVariable Long id, @RequestBody TaskDTO dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
