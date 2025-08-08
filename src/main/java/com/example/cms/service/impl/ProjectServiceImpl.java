package com.example.cms.service.impl;

import com.example.cms.dto.ProjectDTO;
import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Project;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
import com.example.cms.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
  
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;
  private final EmployeeRepository employeeRepository;

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
    List<ProjectDTO> dtos = projectRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    for (ProjectDTO dto : dtos) {
      List<Task> taskList = taskRepository.findByProjectId(dto.getId());
      List<TaskDTO> taskDTOs = taskList.stream().map(this::toDTO).collect(Collectors.toList());
      for(TaskDTO taskDTO : taskDTOs) {
        taskDTO.setProjectName(dto.getName());
        taskDTO.setEmployeeName();
      }
      dto.setTasks(taskList.stream().map(this::toDTO).collect(Collectors.toList()));
    }
    return dtos;
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
}
