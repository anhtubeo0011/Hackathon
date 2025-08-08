package com.example.cms.service.impl;

import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
import com.example.cms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final EmployeeRepository employeeRepository;
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;

  public TaskDTO create(TaskDTO dto) {
    Task task = new Task();
    task.setProjectId(projectRepository.findByName(dto.getProjectName()));
    task.setEmployeeId(employeeRepository.findByName(dto.getEmployeeName()));
    task.setStartDate(dto.getStartDate());
    task.setDueDate(dto.getDueDate());
    task.setStatus(dto.getStatus());
    task.setDescription(dto.getDescription());
    task.setKind(dto.getKind());
    task.setStage(dto.getLevel());
    Task saved = taskRepository.save(task);
    return toDTO(saved);
  }

  public List<TaskDTO> findAll() {
    return taskRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
  }

  public TaskDTO findById(Long id) {
    Optional<Task> task = taskRepository.findById(id);
    return task.map(this::toDTO).orElse(null);
  }

  public TaskDTO update(Long id, TaskDTO dto) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
      Task task = taskOptional.get();
      task.setProjectId(projectRepository.findByName(dto.getProjectName()));
      task.setEmployeeId(employeeRepository.findByName(dto.getEmployeeName()));
      task.setStartDate(dto.getStartDate());
      task.setDueDate(dto.getDueDate());
      task.setStatus(dto.getStatus());
      task.setDescription(dto.getDescription());
      task.setKind(dto.getKind());
      task.setStage(dto.getLevel());
      Task updated = taskRepository.save(task);
      return toDTO(updated);
    }
    return null;
  }

  public void delete(@PathVariable Long id) {
    Optional<Task> task = taskRepository.findById(id);
    task.ifPresent(taskRepository::delete);
  }

  private TaskDTO toDTO(Task task) {
    TaskDTO dto = new TaskDTO();
    dto.setId(task.getId());
    dto.setProjectId(task.getProjectId());
    dto.setEmployeeId(task.getEmployeeId());
    dto.setStartDate(task.getStartDate());
    dto.setDueDate(task.getDueDate());
    dto.setStatus(task.getStatus());
    dto.setDescription(task.getDescription());
    dto.setKind(task.getKind());
    dto.setLevel(task.getStage());
    return dto;
  }
}
