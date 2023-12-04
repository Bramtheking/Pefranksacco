package com.example.pefranksacco;

public class UserCredential {
    private long id;
    private String email;
    private String password;

    // Constructors
    public UserCredential() {
        // Default constructor
    }

    public UserCredential(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
