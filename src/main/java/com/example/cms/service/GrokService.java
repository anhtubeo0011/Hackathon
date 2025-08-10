package com.example.cms.service;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.TaskDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GrokService {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public String callGrok(String systemPrompt, String userPrompt) {
        String url = "https://api.x.ai/v1/chat/completions";

        // Thiết lập headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // Thiết lập body
        Map<String, Object> body = Map.of(
                "model", "grok-3",
                "stream", false,
                "temperature", 0,
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", systemPrompt
                        ),
                        Map.of(
                                "role", "user",
                                "content", userPrompt
                        )
                )
        );


        Map<String, Object> response = webClient.post()
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

    public String smartTaskSegregation(List<TaskDTO> taskDTOList, List<EmployeeDTO> employeeDTOList) {
        try {
            String systemPrompt = """
                    Bạn là một chuyên gia quản lý dự án phần mềm.
                    Nhiệm vụ của bạn: phân công các task cho các lập trình viên sao cho:
                    - Kỹ năng của lập trình viên (strength) phù hợp với yêu cầu task.
                    - Tránh giao task trái với điểm yếu (skill) của lập trình viên.
                    - Cân nhắc loại task (kind) và độ khó (level) với năng lực hiện tại.
                    - Tránh giao quá nhiều task cùng lúc cho một người.
                    - Ưu tiên giao task đúng người để hoàn thành nhanh và chất lượng.
                                        
                    Đầu ra trả về là JSON theo dạng:
                    [
                      {
                        "taskId": <id của task>,
                        "taskDescription": "<mô tả task đầy đủ>",
                        "employeeId": <id của dev>,
                        "employeeName": "<tên nhân sự>",
                        "reason": "<lý do phân công chi tiết>"
                      }
                    ]
                                        
                    Phần "reason" hãy phân tích thật kỹ lý do lựa chọn, ưu nhược điểm của các lập trình viên khi giao task đó.
                    Chỉ trả về đúng JSON theo định dạng trên, không trả thêm thông tin hay lời giải thích nào khác.
                    """;

            String userPrompt = String.format("""
                            Dưới đây là danh sách task:
                            %s

                            Dưới đây là danh sách lập trình viên:
                            %s

                            Hãy phân công công việc theo yêu cầu ở trên.
                            """,
                    objectMapper.writeValueAsString(taskDTOList),
                    objectMapper.writeValueAsString(employeeDTOList)
            );

            return callGrok(systemPrompt, userPrompt);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }
}