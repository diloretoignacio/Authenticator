package com.example.Autenticator.response;


public class UsuarioResponse {
    private long id;
    private String username;
    private String phone;
    private String email;
    private String rol;

    // Constructor
    public UsuarioResponse(long id, String username, String phone, String email, String rol) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.rol = rol;
    }

    public UsuarioResponse() {
    }
    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

