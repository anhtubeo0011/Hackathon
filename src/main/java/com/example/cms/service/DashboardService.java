package com.example.cms.service;

import com.example.cms.dto.DashboardDTO;
import com.example.cms.entity.Employee;
import com.example.cms.entity.Project;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final TaskRepository taskRepository;
  private final ProjectRepository projectRepository;
  private final EmployeeRepository employeeRepository;

  public DashboardDTO getDashboard(Long projectId, String status) {
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    DashboardDTO dashboardDTO = new DashboardDTO();
    List<Task> tasks = taskRepository.findByProjectId(projectId);

    List<Employee> overloadEmployeeTasks = employeeRepository.countOverloadEmployeeTasks(startOfWeek, endOfWeek, projectId, status);
    Integer numberOfHighRiskProject = projectRepository.numberOfHighRiskLevels(projectId, status);
    Float completionRatePercent = projectRepository.completionRatePercent(projectId, status);
    dashboardDTO.setTotalTasks(tasks.size());
    dashboardDTO.setOnTimeRate(completionRatePercent == null ? 0 : completionRatePercent);
    dashboardDTO.setOverDueTasks(taskRepository.countOverdueTasks(projectId, status));
    dashboardDTO.setOverloadedDevs(overloadEmployeeTasks.size());
    dashboardDTO.setProjectAtRisk(numberOfHighRiskProject == null ? 0 : numberOfHighRiskProject);
    return dashboardDTO;
  }
}
