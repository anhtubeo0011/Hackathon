package com.knowledgehub.config;

import com.knowledgehub.entity.Document;
import com.knowledgehub.entity.User;
import com.knowledgehub.repository.DocumentRepository;
import com.knowledgehub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create demo users if they don't exist
        if (userRepository.count() == 0) {
            createDemoUsers();
            createDemoDocuments();
        }
    }

    private void createDemoUsers() {
        // Admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@company.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setFullName("Nguyễn Văn Admin");
        admin.setAvatar("https://images.pexels.com/photos/2379004/pexels-photo-2379004.jpeg?auto=compress&cs=tinysrgb&w=150");
        admin.setGroupName("IT");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        // HR user
        User hrUser = new User();
        hrUser.setUsername("user1");
        hrUser.setEmail("user1@company.com");
        hrUser.setPassword(passwordEncoder.encode("password123"));
        hrUser.setFullName("Trần Thị User");
        hrUser.setAvatar("https://images.pexels.com/photos/1239291/pexels-photo-1239291.jpeg?auto=compress&cs=tinysrgb&w=150");
        hrUser.setGroupName("HR");
        hrUser.setRole(User.Role.USER);
        userRepository.save(hrUser);

        // IT user
        User itUser = new User();
        itUser.setUsername("user2");
        itUser.setEmail("user2@company.com");
        itUser.setPassword(passwordEncoder.encode("password123"));
        itUser.setFullName("Lê Văn Developer");
        itUser.setAvatar("https://images.pexels.com/photos/697509/pexels-photo-697509.jpeg?auto=compress&cs=tinysrgb&w=150");
        itUser.setGroupName("IT");
        itUser.setRole(User.Role.USER);
        userRepository.save(itUser);
    }

    private void createDemoDocuments() {
        User admin = userRepository.findByUsername("admin").orElse(null);
        User hrUser = userRepository.findByUsername("user1").orElse(null);
        User itUser = userRepository.findByUsername("user2").orElse(null);

        if (admin != null) {
            // Public document
            Document doc1 = new Document();
            doc1.setTitle("Hướng dẫn sử dụng React Hooks");
            doc1.setContent("Đây là tài liệu hướng dẫn chi tiết về cách sử dụng React Hooks trong phát triển ứng dụng web...");
            doc1.setSummary("Tài liệu hướng dẫn toàn diện về React Hooks, bao gồm useState, useEffect, và các custom hooks.");
            doc1.setTags(Arrays.asList("React", "JavaScript", "Frontend", "Hooks"));
            doc1.setAuthor(admin);
            doc1.setPrivacy(Document.Privacy.PUBLIC);
            doc1.setViews(156);
            documentRepository.save(doc1);

            // Private document
            Document doc4 = new Document();
            doc4.setTitle("Tài liệu bảo mật nội bộ");
            doc4.setContent("Hướng dẫn về các chính sách bảo mật...");
            doc4.setSummary("Tài liệu nội bộ về chính sách bảo mật thông tin của công ty.");
            doc4.setTags(Arrays.asList("Security", "Policy", "Internal"));
            doc4.setAuthor(admin);
            doc4.setPrivacy(Document.Privacy.PRIVATE);
            doc4.setViews(45);
            documentRepository.save(doc4);
        }

        if (hrUser != null) {
            // Group document
            Document doc2 = new Document();
            doc2.setTitle("Quy trình tuyển dụng mới 2024");
            doc2.setContent("Tài liệu mô tả quy trình tuyển dụng nhân sự mới được áp dụng từ năm 2024...");
            doc2.setSummary("Quy trình tuyển dụng được cập nhật với các bước mới và tiêu chí đánh giá ứng viên.");
            doc2.setTags(Arrays.asList("HR", "Tuyển dụng", "Quy trình"));
            doc2.setAuthor(hrUser);
            doc2.setPrivacy(Document.Privacy.GROUP);
            doc2.setViews(89);
            documentRepository.save(doc2);
        }

        if (itUser != null) {
            // Public document
            Document doc3 = new Document();
            doc3.setTitle("Best Practices cho Database Design");
            doc3.setContent("Tổng hợp các best practices trong thiết kế cơ sở dữ liệu...");
            doc3.setSummary("Các nguyên tắc và phương pháp hay nhất trong thiết kế cơ sở dữ liệu cho hiệu suất tốt nhất.");
            doc3.setTags(Arrays.asList("Database", "MySQL", "Architecture"));
            doc3.setAuthor(itUser);
            doc3.setPrivacy(Document.Privacy.PUBLIC);
            doc3.setViews(234);
            documentRepository.save(doc3);
        }
    }
}