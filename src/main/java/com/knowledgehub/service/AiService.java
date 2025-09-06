package com.knowledgehub.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {
  @Value("${openrouter.api.key}")
  private String apiKey;

  private WebClient webClient;
  private ObjectMapper objectMapper;
  private DocumentReaderService documentReaderService;

  public String callGrok(String systemPrompt, String userPrompt) {
    String url = "https://api.x.ai/v1/chat/completions";

    // Thiết lập headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + apiKey);
    headers.set("Content-Type", "application/json");

    // Thiết lập body
    Map<String, Object> body =
        Map.of(
            "model",
            "grok-3",
            "stream",
            false,
            "temperature",
            0,
            "messages",
            List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)));

    Map<String, Object> response =
        webClient
            .post()
            .uri(url)
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

    // Lấy text từ JSON trả về
    if (response != null) {
      var choices = (java.util.List<Map<String, Object>>) response.get("choices");
      if (choices != null && !choices.isEmpty()) {
        var message = (Map<String, Object>) choices.get(0).get("message");

        return (String) message.get("content");
      }
    }
    return "No response from ChatGPT";
  }

  public String summerize(String filePath) throws IOException {
    String content = documentReaderService.readDocumentFromPath(filePath);
    try {
      String systemPrompt =
          """
                        Bạn là một chuyên gia tóm tắt nôi dung tài liệu.
                        Nhiệm vụ của bạn:
                        - Tóm tắt tài liệu.
                        - Trả lời bằng tiếng việt,
                        - Tôi chỉ cần câu trả lơi 500 chữ.
                    """;

      String userPrompt =
          String.format(
              """
                            Dưới đây là thông tin về nội dung:
                            %s
                            Hãy hỗ trợ tôi tóm tắt.
                            """,
              objectMapper.writeValueAsString(content));

      return callGrok(systemPrompt, userPrompt);
    } catch (Exception e) {
      e.printStackTrace();
      return e.getMessage().toString();
    }
  }
}
