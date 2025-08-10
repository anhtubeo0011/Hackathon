package com.example.cms.service;

import com.example.cms.constant.Status;
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
    project.setPm(dto.getPm());
    project.setStatus(dto.getStatus());
    Project saved = projectRepository.save(project);
    return toDTO(saved);
  }

  public List<ProjectDTO> findAll(String name, String status, String pm) {
    return projectRepository.findAll(name, status, pm).stream().map(this::toDTO).collect(Collectors.toList());
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
      project.setPm(dto.getPm());
      project.setStatus(dto.getStatus());
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
    dto.setPm(project.getPm());
    dto.setStatus(project.getStatus());
    List<Task> taskList = taskRepository.findByProjectId(project.getId());
    if (taskList.isEmpty()) {
      dto.setTeamSize(0);
      dto.setTaskCount(0);
    } else {
      dto.setTeamSize(taskList.stream().map(Task::getEmployeeId).toList().stream().distinct().toList().size());
      dto.setTaskCount(taskList.size());
      dto.setProgress((float) taskList.stream().filter(task -> Status.COMPLETED.name().equals(task.getStatus())).toList().size() / (float) taskList.size());
    }
    dto.setRiskLevel(projectRepository.riskLevel(project.getId()));
    return dto;
  }
}
