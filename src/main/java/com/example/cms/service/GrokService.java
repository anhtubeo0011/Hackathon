package com.example.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GrokService {

  private RestTemplate restTemplate = new RestTemplate();

  @Value("${openrouter.api.key}")
  private String apiKey;

  public String callGrok(Long projectId) {
    String url = "https://openrouter.ai/api/v1/chat/completions";

    // Thiết lập headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.set("Content-Type", "application/json");

    String prompt = "";

    // Thiết lập body
    String body =
        String.format(
            """
            {
                 "messages": [
                     {
                         "role": "system",
                         "content": "Bạn là 1 người hỗ trợ tôi phân công công việc"
                     },
                     {
                         "role": "user",
                         "content": "%s"
                     }
                 ],
                 "model": "grok-3",
                 "stream": false,
                 "temperature": 0
             }
            """,
            prompt);

    HttpEntity<String> request = new HttpEntity<>(body, headers);

    // Gọi API
    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    return response.getBody();
  }
}