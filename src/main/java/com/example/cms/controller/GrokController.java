package com.example.cms.controller;

import com.example.cms.dto.request.SmartTaskRequest;
import com.example.cms.service.GrokService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class GrokController {

    @Autowired
    private GrokService grokService;

    public GrokController(GrokService grokService) {
        this.grokService = grokService;
    }

    @PostMapping("/smart-segregate")
    public ResponseEntity<String> smartTaskSegregate(@RequestParam Long id) {
        String result = grokService.smartTaskSegregation(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/task-manage")
    public ResponseEntity<String> taskManage(@RequestBody SmartTaskRequest request) {
        String result = grokService.taskManage(request);
        return ResponseEntity.ok(result);
    }
}