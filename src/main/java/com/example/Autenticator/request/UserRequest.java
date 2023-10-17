package com.example.Autenticator.request;

public class UserRequest {
    private String username;
    private String password;
    private String phone;
    private String email;

    //Phone
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    //Email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    //Username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    //Password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
