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
public class ProjectService {
  
  private final ProjectRepository projectRepository;
  private final EmployeeRepository employeeRepository;
  private final TaskRepository taskRepository;

  public ProjectDTO create(ProjectDTO dto) {
    Project project = new Project();
    project.setName(dto.getName());
    project.setStartDate(dto.getStartDate());
    project.setEndDate(dto.getEndDate());
    project.setDescription(dto.getDescription());
    Project saved = projectRepository.save(project);
    return toDTO(saved);
  }

  public List<ProjectDTO> findAll() {
    return projectRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
  }

  public ProjectDTO findById(Long id) {
    Optional<Project> project = projectRepository.findById(id);
    return project.map(this::toDTO).orElse(null);
  }

  public ProjectDTO update(Long id, ProjectDTO dto) {
    Optional<Project> projectOptional = projectRepository.findById(id);
    if (projectOptional.isPresent()) {
      Project project = projectOptional.get();
      project.setName(dto.getName());
      project.setStartDate(dto.getStartDate());
      project.setEndDate(dto.getEndDate());
      project.setDescription(dto.getDescription());
      Project updated = projectRepository.save(project);
      return toDTO(updated);
    }

    return null;
  }

  public void delete(Long id) {
    Optional<Project> projectOptional = projectRepository.findById(id);
    projectOptional.ifPresent(projectRepository::delete);
  }

  private ProjectDTO toDTO(Project project) {
    ProjectDTO dto = new ProjectDTO();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setStartDate(project.getStartDate());
    dto.setEndDate(project.getEndDate());
    dto.setDescription(project.getDescription());
    List<TaskDTO> taskList = taskRepository.findByProjectId(project.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    for(TaskDTO taskDTO : taskList) {
      taskDTO.setEmployeeDTO(toDTO(employeeRepository.findById(taskDTO.getEmployeeId()).get()));
    }
    dto.setTasks(taskList);
    return dto;
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

  private EmployeeDTO toDTO(Employee employee) {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setId(employee.getId());
    dto.setName(employee.getName());
    dto.setMail(employee.getMail());
    dto.setStrength(employee.getStrength());
    dto.setWeakness(employee.getWeakness());
    dto.setTasks(taskRepository.findByEmployeeId(employee.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
    return dto;
  }
}
