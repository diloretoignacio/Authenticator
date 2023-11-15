package com.example.Autenticator.response;

public class LoginResponse {
    private String token;
    private String username;
    private String rolName;
    private String mensaje;

    // Constructor vacío (necesario para la deserialización JSON)
    public LoginResponse() {
    }

    // Constructor con parámetros
    public LoginResponse(String token, String username, String rolName) {
        this.token = token;
        this.username = username;
        this.rolName = rolName;
    }

    public LoginResponse(String mensaje) {
        this.mensaje = mensaje;
    }

    // Getters y setters para confidence
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getRolName() {
        return rolName;
    }

    public void setRolName(String rolName) {
        this.rolName = rolName;
    }
}

