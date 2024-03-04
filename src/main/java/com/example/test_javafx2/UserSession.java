package com.example.test_javafx2;

public class UserSession {
    private String email;
    private String role;
    private static UserSession instance;

    private UserSession() {
        // Private constructor to prevent instantiation from outside
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(String role) {this.role = role;}
    public String getRole() {return role;}
    public static synchronized void destroyInstance() {
        instance = null;
    }
}
