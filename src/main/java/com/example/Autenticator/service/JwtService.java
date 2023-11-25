package com.example.Autenticator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Autenticator.config.JwtUtil;

@Service
public class JwtService {

    @Autowired
    private JwtUtil jwtUtil;

    public String generateToken(String username, String role) {
        return jwtUtil.generarToken(username, role);
    }
}
