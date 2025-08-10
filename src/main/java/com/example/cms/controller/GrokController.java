package com.example.cms.controller;

import com.example.cms.dto.request.SmartTaskRequest;
import com.example.cms.service.GrokService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@CrossOrigin(origins = "*")
public class GrokController {

    @Autowired
    private GrokService grokService;

    public GrokController(GrokService grokService) {
        this.grokService = grokService;
    }

    // API này là call AI gợi ý cho project
    @PostMapping("/smart-segregate")
    public ResponseEntity<String> smartTaskSegregate(@RequestParam Long id) {
        String result = grokService.smartTaskSegregation(id);
        return ResponseEntity.ok(result);
    }

    // API này là call AI gợi ý cho task
    @PostMapping("/task-suggestion")
    public ResponseEntity<String> taskManage(@RequestParam Long id) {
        String result = grokService.taskSuggestion(id);
        return ResponseEntity.ok(result);
    }
}