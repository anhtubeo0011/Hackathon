package com.knowledgehub.controller;

import com.knowledgehub.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ai")
public class AiController {

  @Autowired
  private AiService aiService;

  @GetMapping()
  public ResponseEntity<String> sumerize(@RequestParam String filePath) throws IOException {
    return ResponseEntity.ok(aiService.summerize(filePath));
  }
}
