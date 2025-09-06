package com.knowledgehub.dto;

import com.knowledgehub.entity.User;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private UserResponse user;

    public JwtResponse(String accessToken, User user) {
        this.token = accessToken;
        this.user = new UserResponse(user);
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public static class UserResponse {
        private String id;
        private String username;
        private String email;
        private String fullName;
        private String avatar;
        private String group;
        private String role;
        private String createdAt;

        public UserResponse(User user) {
            this.id = user.getId().toString();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.fullName = user.getFullName();
            this.avatar = user.getAvatar();
            this.group = user.getGroupName();
            this.role = user.getRole().name().toLowerCase();
            this.createdAt = user.getCreatedAt().toString();
        }

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }

        public String getGroup() { return group; }
        public void setGroup(String group) { this.group = group; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}