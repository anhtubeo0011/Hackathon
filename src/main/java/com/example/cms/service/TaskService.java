package com.example.cms.service;

import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final EmployeeRepository employeeRepository;
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;

  public TaskDTO create(TaskDTO dto) {
    Task task = new Task();
    task.setName(dto.getTitle());
    task.setDescription(dto.getDescription());
    task.setProjectId(projectRepository.findById(dto.getProjectId()).get().getId());
    task.setEmployeeId(employeeRepository.findById(dto.getAssigneeId()).get().getId());
    task.setStatus(dto.getStatus());
    task.setPriority(dto.getPriority());
    task.setEstimateHours(dto.getEstimateHours());
    task.setStartDate(dto.getStartDate());
    task.setDueDate(dto.getDeadline());
    task.setRequiredSkill(dto.getRequiredSkills());
    task.setUpdateTime(LocalDateTime.now());
    Task saved = taskRepository.save(task);
    return toDTO(saved);
  }

  public List<TaskDTO> findAll(String name, String requiredSkills, Long projectId, Long employeeId, String status) {
    return taskRepository.findAll(name, requiredSkills, projectId, employeeId, status).stream().map(this::toDTO).collect(Collectors.toList());
  }

  public TaskDTO findById(Long id) {
    Optional<Task> task = taskRepository.findById(id);
    return task.map(this::toDTO).orElse(null);
  }

  public TaskDTO update(Long id, TaskDTO dto) {
    Optional<Task> taskOptional = taskRepository.findById(id);
    if (taskOptional.isPresent()) {
      Task task = taskOptional.get();
      task.setName(dto.getTitle());
      task.setDescription(dto.getDescription());
      if (dto.getProjectId() != null) {
        task.setProjectId(projectRepository.findById(dto.getProjectId()).get().getId());
      }
      if (dto.getAssigneeId() != null) {
        task.setEmployeeId(employeeRepository.findById(dto.getAssigneeId()).get().getId());
      }
      task.setStatus(dto.getStatus());
      task.setPriority(dto.getPriority());
      task.setEstimateHours(dto.getEstimateHours());
      task.setStartDate(dto.getStartDate());
      task.setDueDate(dto.getDeadline());
      task.setRequiredSkill(dto.getRequiredSkills());
      task.setUpdateTime(LocalDateTime.now());
      Task updated = taskRepository.save(task);
      return toDTO(updated);
    }
    return null;
  }

  public void delete(Long id) {
    Optional<Task> task = taskRepository.findById(id);
    task.ifPresent(taskRepository::delete);
  }

  private TaskDTO toDTO(Task task) {
    TaskDTO dto = new TaskDTO();
    dto.setId(task.getId());
    dto.setTitle(task.getName());
    dto.setDescription(task.getDescription());
    dto.setProjectId(task.getProjectId());
    dto.setAssigneeId(task.getEmployeeId());
    dto.setStatus(task.getStatus());
    dto.setPriority(task.getPriority());
    dto.setEstimateHours(task.getEstimateHours());
    dto.setStartDate(task.getStartDate());
    dto.setDeadline(task.getDueDate());
    dto.setRequiredSkills(task.getRequiredSkill());
    dto.setUpdateTime(task.getUpdateTime());

    if (task.getProjectId() != null) {
      dto.setProjectName(projectRepository.findById(task.getProjectId()).get().getName());
    }
    if (task.getEmployeeId() != null) {
      dto.setAssigneeName(employeeRepository.findById(task.getEmployeeId()).get().getName());
    }
    return dto;
  }
}
