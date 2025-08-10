package com.example.cms.controller;

import com.example.cms.dto.DashboardDTO;
import com.example.cms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
  @Autowired private DashboardService dashboardService;

  @GetMapping
  public ResponseEntity<DashboardDTO> getDashboardDTO(
      @RequestParam(required = false) Long projectId,
      @RequestParam(required = false) String status) {
    return ResponseEntity.ok(dashboardService.getDashboard(projectId, status));
  }
}
