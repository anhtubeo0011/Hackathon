//package com.example.cms.config;
//
//import com.example.cms.entity.*;
//import com.example.cms.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private DeveloperRepository developerRepository;
//
//    @Autowired
//    private IncidentRepository incidentRepository;
//
//    @Autowired
//    private DocumentRepository documentRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Initialize Developers
//        Developer dev1 = new Developer("Nguyễn Văn An", "an.nguyen@company.com");
//        dev1.setTotalResolved(45);
//
//        Developer dev2 = new Developer("Trần Thị Bình", "binh.tran@company.com");
//        dev2.setTotalResolved(38);
//
//        Developer dev3 = new Developer("Lê Minh Cường", "cuong.le@company.com");
//        dev3.setTotalResolved(52);
//
//        Developer dev4 = new Developer("Phạm Thu Dung", "dung.pham@company.com");
//        dev4.setTotalResolved(29);
//
//        List<Developer> developers = Arrays.asList(dev1, dev2, dev3, dev4);
//        developerRepository.saveAll(developers);
//
//        // Initialize Incidents
//        Incident incident1 = new Incident("Lỗi đăng nhập không thành công",
//                "Người dùng không thể đăng nhập vào hệ thống sau khi nhập đúng thông tin",
//                IncidentStatus.IN_PROGRESS, Priority.HIGH, "Authentication");
//        incident1.setIncidentId("INC001");
//        incident1.setAssignedDev(dev1);
//        incident1.setDueDate(LocalDateTime.now().plusDays(2));
//
//        Incident incident2 = new Incident("Database connection timeout",
//                "Kết nối đến database bị timeout trong giờ cao điểm",
//                IncidentStatus.NEW, Priority.CRITICAL, "Database");
//        incident2.setIncidentId("INC002");
//        incident2.setAssignedDev(dev3);
//        incident2.setDueDate(LocalDateTime.now().plusDays(1));
//
//        Incident incident3 = new Incident("UI hiển thị sai trên mobile",
//                "Giao diện bị vỡ layout khi hiển thị trên thiết bị mobile",
//                IncidentStatus.RESOLVED, Priority.MEDIUM, "Frontend");
//        incident3.setIncidentId("INC003");
//        incident3.setAssignedDev(dev2);
//        incident3.setDueDate(LocalDateTime.now().plusDays(3));
//
//        Incident incident4 = new Incident("API response chậm",
//                "Thời gian phản hồi của API quá chậm, ảnh hưởng đến trải nghiệm người dùng",
//                IncidentStatus.IN_PROGRESS, Priority.HIGH, "Backend");
//        incident4.setIncidentId("INC004");
//        incident4.setAssignedDev(dev4);
//        incident4.setDueDate(LocalDateTime.now().plusDays(1));
//
//        Incident incident5 = new Incident("Email không gửi được",
//                "Hệ thống email notification không hoạt động",
//                IncidentStatus.NEW, Priority.MEDIUM, "Email");
//        incident5.setIncidentId("INC005");
//        incident5.setAssignedDev(dev1);
//        incident5.setDueDate(LocalDateTime.now().plusDays(2));
//
//        List<Incident> incidents = Arrays.asList(incident1, incident2, incident3, incident4, incident5);
//        incidentRepository.saveAll(incidents);
//
//        // Initialize Documents
//        Document doc1 = new Document("Hướng dẫn xử lý lỗi Authentication", "Authentication",
//                "## Nguyên nhân thường gặp:\n\n1. Token hết hạn\n2. Sai thông tin đăng nhập\n3. Session timeout\n4. Lỗi cấu hình JWT\n\n## Các bước xử lý:\n\n1. **Kiểm tra token**: Verify token expiration và signature\n2. **Reset session**: Clear session và redirect về login\n3. **Check database**: Kiểm tra user credentials trong DB\n4. **Log analysis**: Xem log để identify root cause",
//                Priority.HIGH, "Nguyễn Văn An");
//        doc1.setDocumentId("DOC001");
//        doc1.setViewPermissions(Arrays.asList("all"));
//
//        Document doc2 = new Document("Tối ưu Database Performance", "Database",
//                "## Nguyên nhân database chậm:\n\n1. Missing indexes\n2. N+1 query problem\n3. Connection pool exhausted\n4. Large dataset without pagination\n\n## Giải pháp:\n\n1. **Tạo indexes**: Thêm indexes cho các trường thường query\n2. **Query optimization**: Sử dụng JOIN thay vì multiple queries\n3. **Connection pooling**: Cấu hình connection pool phù hợp\n4. **Caching**: Implement Redis caching cho data ít thay đổi",
//                Priority.CRITICAL, "Lê Minh Cường");
//        doc2.setDocumentId("DOC002");
//        doc2.setViewPermissions(Arrays.asList("senior", "lead"));
//
//        Document doc3 = new Document("Frontend Responsive Design Issues", "Frontend",
//                "## Common responsive issues:\n\n1. Fixed width elements\n2. Improper CSS media queries\n3. Image sizing problems\n4. Text overflow\n\n## Solutions:\n\n1. **Use flexbox/grid**: Modern CSS layout techniques\n2. **Mobile-first approach**: Design for mobile then scale up\n3. **Test on real devices**: Don't rely only on browser dev tools\n4. **CSS frameworks**: Consider using Tailwind or Bootstrap",
//                Priority.MEDIUM, "Trần Thị Bình");
//        doc3.setDocumentId("DOC003");
//        doc3.setViewPermissions(Arrays.asList("all"));
//
//        List<Document> documents = Arrays.asList(doc1, doc2, doc3);
//        documentRepository.saveAll(documents);
//    }
//}