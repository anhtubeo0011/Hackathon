package com.example.cms.service;

import com.example.cms.entity.Incident;
import com.example.cms.repository.IncidentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrokService {
  @Value("${openrouter.api.key}")
  private String apiKey;

  private final WebClient webClient;
  private final ObjectMapper objectMapper;
  private final IncidentRepository incidentRepository;

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

  public String incidentSuggestion(Long incidentId) {
    Optional<Incident> incident = incidentRepository.findById(incidentId);

    if (incident.isPresent()) {
      try {
        String systemPrompt = """
                        Bạn là một chuyên gia quản lý dự án phần mềm và hỗ trợ xử lý sự cố.
                        Nhiệm vụ của bạn:
                        - Hướng dẫn người dùng debug và sửa lỗi được.
                        - Bạn chỉ cần viết chỗ tôi cần kiểm tra ngắn gọn không dài dòng.
                        - Trả lời bằng tiếng việt,
                        - Tôi chỉ cần câu trả lơi 30 chữ.
                    """;

        String userPrompt = String.format("""
                            Dưới đây là thông tin về lỗi:
                            %s
                            Hãy hỗ trợ tôi xử lý lỗi trên.
                            """,
            objectMapper.writeValueAsString(incident.get().getDescription())
        );

        return callGrok(systemPrompt, userPrompt);
      } catch (Exception e) {
        e.printStackTrace();
        return e.getMessage().toString();
      }
    }
    return null;
  }

//  public String taskSuggestion(Long taskId) {
//    Task task = taskRepository.findById(taskId).orElse(null);
//    List<EmployeeDTO> employees = employeeService.findAll(); // Lấy cả task dev đang làm
//
//    if (task == null) {
//      return "Task không tồn tại";
//    }
//
//    try {
//      String systemPrompt = """
//            Bạn là một chuyên gia quản lý dự án phần mềm và tối ưu phân công nhân sự.
//            Nhiệm vụ:
//            - Dựa vào thông tin một task và danh sách lập trình viên (bao gồm task họ đang làm, kỹ năng, % hoàn thành, workload).
//            - Nếu task chưa được gán dev → đề xuất dev phù hợp nhất.
//            - Nếu task đã được gán dev:
//                + Đánh giá xem với workload hiện tại, kỹ năng và tốc độ hoàn thành của họ có đáp ứng được tiến độ task hay không.
//                + Nếu không đáp ứng → gợi ý dev thay thế hoặc hỗ trợ.
//            - Phân tích dựa trên:
//              + Kỹ năng phù hợp với yêu cầu của task (required skills).
//              + Mức độ thành thạo và kinh nghiệm với loại task tương tự.
//              + Workload hiện tại (ưu tiên người rảnh hơn).
//              + Tỷ lệ hoàn thành công việc (% completion).
//              + Thời gian có thể bắt đầu task (availability) hoặcenéu ông .
//            - Kết quả trả về cho mỗi dev gợi ý phải có dạng:
//              {
//                "id": "<id của dev gắn thực hiện task>",
//                "name": "<Tên của dev thực hiện task>",
//                "matchScore": "<% match, ví dụ: 87%>",
//                "email": "<Email của dev>",
//                "reason": "<Lý do dev này phù hợp với task, nêu lý do ngắn gọn thôi nhé nhưng phải rõ ràng và thông minh. Nếu dev đã
//                gán cho dev mà dev này đảm bảo tiến độ hoặc không thì cũng phải nêu lý do cụ thể>",
//                "availability": "<Có thể bắt đầu dev task này trong bao lâu nữa, ví dụ available now, availabel 2 days,
//                hoặc overload nếu không đáp ứng được công việc>"
//              }
//            - Nếu task đã có dev, mà đáp ứng được công việc thì trường availability để giá trị là availabled, còn nếu không đáp ứng được thì để là overload
//            - Chỉ trả về JSON array các dev gợi ý (và đánh giá dev hiện tại nếu có), không trả thêm text hoặc giải thích ngoài JSON.
//        """;
//
//      String userPrompt = String.format("""
//            Thông tin task:
//            %s
//
//            Danh sách lập trình viên:
//            %s
//
//            Hãy trả về danh sách gợi ý dev theo format yêu cầu.
//            """,
//          objectMapper.writeValueAsString(task),
//          objectMapper.writeValueAsString(employees)
//      );
//
//      return callGrok(systemPrompt, userPrompt);
//    } catch (Exception e) {
//      e.printStackTrace();
//      return e.getMessage();
//    }
//  }
}