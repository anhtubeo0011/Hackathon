package com.example.cms.service;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.ProjectDTO;
import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Employee;
import com.example.cms.entity.Project;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
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
    task.setProjectId(projectRepository.findById(dto.getProjectId()).get().getId());
    task.setEmployeeId(employeeRepository.findById(dto.getEmployeeId()).get().getId());
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
      task.setProjectId(projectRepository.findById(dto.getProjectId()).get().getId());
      task.setEmployeeId(employeeRepository.findById(dto.getEmployeeId()).get().getId());
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

  public void delete(Long id) {
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
    dto.setProjectDTO(toDTO(projectRepository.findById(dto.getProjectId()).get()));
    dto.setEmployeeDTO(toDTO(employeeRepository.findById(dto.getEmployeeId()).get()));
    return dto;
  }

  private EmployeeDTO toDTO(Employee employee) {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setId(employee.getId());
    dto.setName(employee.getName());
    dto.setMail(employee.getMail());
    dto.setStrength(employee.getStrength());
    dto.setWeakness(employee.getWeakness());
    return dto;
  }

  private ProjectDTO toDTO(Project project) {
    ProjectDTO dto = new ProjectDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setStartDate(project.getStartDate());
    dto.setEndDate(project.getEndDate());
    dto.setDescription(project.getDescription());
    return dto;
  }
}
