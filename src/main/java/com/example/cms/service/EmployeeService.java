package com.example.cms.service;

import com.example.cms.constant.Status;
import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.RecentActivity;
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
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final TaskRepository taskRepository;
  private final ProjectRepository projectRepository;

  public EmployeeDTO create(EmployeeDTO dto) {
    Employee employee = new Employee();
    employee.setName(dto.getName());
    employee.setMail(dto.getMail());
    employee.setStrength(dto.getSkills());
    Employee saved = employeeRepository.save(employee);
    return toDTO(saved);
  }

  public EmployeeDTO findById(Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    return employee.map(this::toDTO).orElse(null);
  }

  public List<EmployeeDTO> findAll() {
    return employeeRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
  }

  public List<EmployeeDTO> findAll(String name, String mail, String skill, Long projectId) {
    return employeeRepository.findAll(name, mail, skill, projectId).stream().map(this::toDTO).collect(Collectors.toList());
  }

  public EmployeeDTO update(Long id, EmployeeDTO dto) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      employee.get().setName(dto.getName());
      employee.get().setMail(dto.getMail());
      employee.get().setStrength(dto.getSkills());
      employee.get().setAvatar(dto.getAvatar());
      Employee updated = employeeRepository.save(employee.get());
      return toDTO(updated);
    }

    return null;
  }

  public void delete(Long id) {
    Optional<Employee> employee = employeeRepository.findById(id);
    employee.ifPresent(employeeRepository::delete);
  }

  private EmployeeDTO toDTO(Employee employee) {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setId(employee.getId());
    dto.setName(employee.getName());
    dto.setMail(employee.getMail());
    dto.setAvatar(employee.getAvatar());
    dto.setMaxCapacity(40);

    List<Task> tasks = taskRepository.findByEmployeeId(employee.getId()).stream().sorted((o1,o2) -> o1.getStartDate().isBefore(o2.getStartDate()) ? -1 : 1).toList();
    if (tasks.isEmpty()) {
      dto.setCompletionRate(100f);
    } else {
      int numberOfTasks = tasks.size();
      int numberOfCompletedTasks = tasks.stream().filter(task -> Status.COMPLETED.name().equals(task.getStatus())).toList().size();
      dto.setCompletionRate((float) numberOfCompletedTasks / numberOfTasks);
      List<Long> projectIds = tasks.stream().map(Task::getProjectId).collect(Collectors.toList());
      List<Project> projects = projectRepository.findAllById(projectIds);
      dto.setActiveProjects((projects.stream().map(Project::getName)).toList());
      RecentActivity recentActivity = new RecentActivity();
      recentActivity.setId(tasks.getFirst().getId());
      recentActivity.setType(tasks.getFirst().getStatus());
      recentActivity.setDescription(tasks.getFirst().getDescription());
      recentActivity.setTimestamp(tasks.getFirst().getUpdateTime());
      dto.setRecentActivity(List.of(recentActivity));
    }

    for(int i = 0; i < employee.getStrength().size(); i++) {
      employee.getStrength().get(i).setId(i + 1);
      employee.getStrength().get(i).setVerified(true);
    }

    dto.setSkills(employee.getStrength());
    return dto;
  }
}
