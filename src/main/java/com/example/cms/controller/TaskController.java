package com.example.cms.controller;

import com.example.cms.dto.TaskDTO;
import com.example.cms.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

  @Autowired private TaskService service;

  @PostMapping
  public ResponseEntity<TaskDTO> create(@RequestBody TaskDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<List<TaskDTO>> findAll(
      @RequestParam(required = false ) String name,
      @RequestParam(required = false ) String requiredSkills,
      @RequestParam(required = false ) Long projectId,
      @RequestParam(required = false ) Long employeeId,
      @RequestParam(required = false ) String status) {
    return ResponseEntity.ok(service.findAll(name, requiredSkills, projectId, employeeId, status));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDTO> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PutMapping("")
  public ResponseEntity<TaskDTO> update(@RequestBody TaskDTO dto) {
    return ResponseEntity.ok(service.update(dto.getId(), dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
