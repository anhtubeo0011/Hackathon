package com.example.cms.controller;

import com.example.cms.service.GrokService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GrokController {

  @Autowired
  private GrokService grokService;

  public GrokController(GrokService grokService) {
    this.grokService = grokService;
  }

  @GetMapping("/grok")
  public ResponseEntity<String> getGrokResponse(@RequestParam Long projectId) {
    return ResponseEntity.ok(grokService.callGrok(projectId));
  }
}