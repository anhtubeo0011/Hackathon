package com.example.cms.service;

import com.example.cms.dto.EmployeeDTO;
import com.example.cms.dto.request.SmartTaskRequest;
import com.example.cms.entity.Project;
import com.example.cms.entity.Task;
import com.example.cms.repo.EmployeeRepository;
import com.example.cms.repo.ProjectRepository;
import com.example.cms.repo.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
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
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final ProjectRepository projectRepository;

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

    public String smartTaskSegregation(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<EmployeeDTO> employees = employeeService.findAll();
        try {
            String systemPrompt = """
                        Bạn là một chuyên gia quản lý dự án phần mềm và tối ưu nhân sự.
                        Nhiệm vụ của bạn:
                        - Đánh giá tiến độ của dự án dựa trên danh sách các task và thông tin lập trình viên.
                        - Mỗi lập trình viên có thể có các thông tin: kỹ năng, mức độ thành thạo, hiệu suất trung bình (% hoàn thành), 
                        số task đang làm, loại task đã từng làm, workload hiện tại (%).
                        - Đưa ra nhận định xem tiến độ hiện tại có đảm bảo hoàn thành dự án đúng hạn hay không.
                        - Nếu không đảm bảo, đưa ra đề xuất thay đổi nhân sự hoặc phân công lại task:
                          + "Thay thế nhân sự": Thay lập trình viên hiện tại bằng người khác phù hợp hơn.
                          + "Thêm nhân sự": Thêm lập trình viên mới hoặc phân công lại task cho dev có năng lực phù hợp.
                        - Mỗi đề xuất phải kèm:
                          + Mức độ ưu tiên: Cao | Trung bình | Thấp
                          + Lý do chi tiết (ví dụ: Low performance on similar tasks, workload quá cao, thiếu kỹ năng phù hợp).
                          + Đề xuất cụ thể: thay bằng ai, thêm ai, hoặc gán lại task cho ai.
                          + Khi phân tích phải chỉ ra cụ thể những ông dev nào có tech stack phù hợp với cái task này nhưng vì lý do nào đó mà
                          chọn ông phù họp nhất, phải đưa ra lý do không chọn các ông còn lại (workload quá nhiều, chưa đủ trình để làm,...
                        - Khi trả lời thì nói tên task và ông dev đó ra đừng nó nói kiểu Task + ID (task 101,...) rất khó nắm bắt cho người dùng
                        - PHân công hợp lý, tránh để một nguời phải làm quá nhiêù công việc. Nếu người đáp ứng được yêu cầu của công việc này đang
                        phải làm workload quá nhiều thì phải gán sang cho dev khác rảnh hơn, có thể kém hơn, chưa đáp ứng được yêu cầu công việc
                         nhưng nhờ ông đáp ứng được yêu cầu này lead hướng dẫn cũng được (ông dev lead sẽ không làm mà đứng trên vai trò hướng dẫn).
                        
                        Đầu ra trả về là JSON theo dạng:
                         {
                        "summary": "<Tóm tắt ngắn gọn tình hình dự án hiện tại và đánh giá khả năng hoàn thành đúng tiến độ>",
                            [
                          {
                            "action": "<Thay thế nhân sự | Thêm nhân sự>",
                            "priority": "<Cao | Trung bình | Thấp>",
                            "details": "<Lý do và phân tích chi tiết>",
                            "recommendation": "<Đề xuất cụ thể>"
                          }
                        ]
                        }
                        
                         Trả lời bằng tiếng việt,
                        Chỉ trả về đúng JSON theo định dạng trên, không trả thêm thông tin hay lời giải thích nào khác ngoài JSON.
                    """;

            String userPrompt = String.format("""
                            Dưới đây là thông tin về tiến độ dự án:
                            %s
                            Dưới đây là danh sách task:
                            %s

                            Dưới đây là danh sách lập trình viên:
                            %s

                            Hãy phân công công việc theo yêu cầu ở trên.
                            """,
                    objectMapper.writeValueAsString(project),
                    objectMapper.writeValueAsString(tasks),
                    objectMapper.writeValueAsString(employees)
            );

            return callGrok(systemPrompt, userPrompt);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }

    public String taskManage(SmartTaskRequest request) {
//        List<Task> tasks = taskRepository.findByProjectId(projectId);
//        List<EmployeeDTO> employees = employeeService.findAll();
        try {
            String systemPrompt = """
                        Bạn là một chuyên gia quản lý dự án phần mềm và tối ưu nhân sự.
                        Nhiệm vụ của bạn:
                        - Đánh giá tiến độ của dự án dựa trên danh sách các task và thông tin lập trình viên.
                        - Mỗi lập trình viên có thể có các thông tin: kỹ năng, mức độ thành thạo, hiệu suất trung bình (% hoàn thành), số task đang làm, loại task đã từng làm, workload hiện tại (%).
                        - Đưa ra nhận định xem tiến độ hiện tại có đảm bảo hoàn thành dự án đúng hạn hay không.
                        - Nếu không đảm bảo, đưa ra đề xuất thay đổi nhân sự hoặc phân công lại task:
                          + "Thay thế nhân sự": Thay lập trình viên hiện tại bằng người khác phù hợp hơn.
                          + "Thêm nhân sự": Thêm lập trình viên mới hoặc phân công lại task cho dev có năng lực phù hợp.
                        - Mỗi đề xuất phải kèm:
                          + Mức độ ưu tiên: Cao | Trung bình | Thấp
                          + Lý do chi tiết (ví dụ: Low performance on similar tasks, workload quá cao, thiếu kỹ năng phù hợp).
                          + Đề xuất cụ thể: thay bằng ai, thêm ai, hoặc gán lại task cho ai.
                          + Khi phân tích phải chỉ ra cụ thể những ông dev nào có tech stack phù hợp với cái task này nhưng vì lý do nào đó mà
                          chọn ông phù họp nhất, phải đưa ra lý do không chọn các ông còn lại (workload quá nhiều, chưa đủ trình để làm,...
                        - Khi trả lời thì nói tên task và ông dev đó ra đừng nó nói kiểu Task + ID (task 101,...) rất khó nắm bắt cho người dùng
                        
                        Đầu ra trả về là JSON theo dạng:
                         {
                        "summary": "<Tóm tắt ngắn gọn tình hình dự án hiện tại và đánh giá khả năng hoàn thành đúng tiến độ>",
                            [
                          {
                            "action": "<Thay thế nhân sự | Thêm nhân sự>",
                            "priority": "<Cao | Trung bình | Thấp>",
                            "details": "<Lý do và phân tích chi tiết>",
                            "recommendation": "<Đề xuất cụ thể>"
                          }
                        ]
                        }
                        
                         Trả lời bằng tiếng việt,
                        Chỉ trả về đúng JSON theo định dạng trên, không trả thêm thông tin hay lời giải thích nào khác ngoài JSON.
                    """;

            String userPrompt = String.format("""
                            Dưới đây là thông tin về tiến độ dự án:
                            %s
                            Dưới đây là danh sách task:
                            %s

                            Dưới đây là danh sách lập trình viên:
                            %s

                            Hãy phân công công việc theo yêu cầu ở trên.
                            """,
                    objectMapper.writeValueAsString(request.getProject()),
                    objectMapper.writeValueAsString(request.getTaskDTOList()),
                    objectMapper.writeValueAsString(request.getEmployeeDTOList())
            );

            return callGrok(systemPrompt, userPrompt);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage().toString();
        }
    }
}