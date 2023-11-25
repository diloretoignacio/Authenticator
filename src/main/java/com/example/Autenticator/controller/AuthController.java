package com.example.Autenticator.controller;

import com.example.Autenticator.config.JwtUtil;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.request.UserRequest;
import com.example.Autenticator.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.Autenticator.service.UsuarioService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Autenticator.request.AuthRequest;
import com.example.Autenticator.service.AuthService;
import com.example.Autenticator.service.JwtService;
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {

        try {
            // Autenticar al usuario usando el servicio de autenticación
            UserDetails userDetails = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());

            // Generar el token JWT con el servicio de JWT y obtener el rol del usuario
            String token = authService.generateToken(userDetails);

            // Devolver el token y detalles del usuario en la respuesta
            LoginResponse loginResponse = new LoginResponse(token, userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            // Manejar la excepción de manera adecuada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Las credenciales ingresadas son incorrectas"));
        }
    }
}