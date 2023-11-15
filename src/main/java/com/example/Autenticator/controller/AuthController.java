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

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {

        try {
            // Crear el token de autenticación
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

            // Autenticar el token
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            // Obtener detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Obtener el rol del usuario
            String userRole = usuarioService.obtenerRolPorUsuario(userDetails.getUsername());

            // Generar el token JWT con el rol
            String token = jwtUtil.generarToken(userDetails.getUsername(), userRole);

            // Devolver el token y detalles del usuario en la respuesta
            LoginResponse loginResponse = new LoginResponse(token, userDetails.getUsername(), userRole);

            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            // Manejar la excepción de manera adecuada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse("Las credenciales ingresadas son incorrectas"));
        }
    }
}
