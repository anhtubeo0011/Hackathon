package com.example.cms.controller;

import com.example.cms.dto.DashboardDTO;
import com.example.cms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  @Autowired
  private DashboardService dashboardService;

  @GetMapping
  public ResponseEntity<DashboardDTO> getDashboardDTO() {
    return ResponseEntity.ok(dashboardService.getDashboard());
  }
}
