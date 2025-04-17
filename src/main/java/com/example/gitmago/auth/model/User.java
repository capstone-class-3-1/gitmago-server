package com.example.gitmago.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String password;
    private String school;
    private String email;
    private boolean emailVerified = false;

    public User() {
    }

    public User(String username, String password, String school) {
        this.username = username;
        this.password = password;
        this.school = school;
        this.email = email;
        this.emailVerified = false;
    }

    public String getEmail(){
        return email;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSchool() {
        return school;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
