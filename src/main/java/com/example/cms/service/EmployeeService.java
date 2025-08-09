package com.example.cms.service;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.TaskDTO;
import com.example.cms.entity.Employee;
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
    employee.setStrength(dto.getStrength());
    employee.setWeakness(dto.getWeakness());
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

  public EmployeeDTO update(Long id, EmployeeDTO dto) {
    Optional<Employee> employee = employeeRepository.findById(id);
    if (employee.isPresent()) {
      employee.get().setName(dto.getName());
      employee.get().setMail(dto.getMail());
      employee.get().setStrength(dto.getStrength());
      employee.get().setWeakness(dto.getWeakness());
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
    dto.setStrength(employee.getStrength());
    dto.setWeakness(employee.getWeakness());
    dto.setTasks(taskRepository.findByEmployeeId(employee.getId()).stream().map(this::toDTO).collect(Collectors.toList()));
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
