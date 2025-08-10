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

  public DashboardDTO getDashboard() {
    LocalDate today = LocalDate.now();
    LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    DashboardDTO dashboardDTO = new DashboardDTO();
    List<Task> tasks = taskRepository.findAll();

    Integer overloadEmployeeTasks = taskRepository.countOverloadEmployeeTasks(startOfWeek, endOfWeek);
    Integer numberOfHighRiskProject = projectRepository.numberOfHighRiskLevels();

    dashboardDTO.setTotalTasks(tasks.size());
    dashboardDTO.setOnTimeRate(projectRepository.completionRatePercent());
    dashboardDTO.setOverDueTasks(taskRepository.countOverdueTasks());
    dashboardDTO.setOverloadedDevs(overloadEmployeeTasks == null ? 0 : overloadEmployeeTasks);
    dashboardDTO.setProjectAtRisk(numberOfHighRiskProject == null ? 0 : numberOfHighRiskProject);
    return dashboardDTO;
  }
}
