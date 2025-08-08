package com.example.cms.service.impl;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.entity.Employee;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;

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

  public void delete(@PathVariable Long id) {
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
    return dto;
  }
}
