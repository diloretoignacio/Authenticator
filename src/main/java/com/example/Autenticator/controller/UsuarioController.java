package com.example.Autenticator.controller;

import com.example.Autenticator.config.JwtUtil;
import com.example.Autenticator.entity.Role;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.request.UserRequest;
import com.example.Autenticator.response.UsuarioResponse;
import com.example.Autenticator.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.Autenticator.service.UsuarioService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @PostMapping("register")
    public ResponseEntity<String> registrarUsuario(@RequestBody UserRequest userRequest) {
        try {
            usuarioService.crearUsuario(userRequest);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<UsuarioResponse> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return usuarios.stream()
                .map(this::mapUsuarioToUsuarioResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponse mapUsuarioToUsuarioResponse(Usuario usuario) {
        Role role = usuario.getRoles().iterator().next();
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPhone(),
                usuario.getEmail(),
                role.getName()
        );
    }

    @RequestMapping(value = "/", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().allow(HttpMethod.GET).build();
    }
}
