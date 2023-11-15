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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<String> registrarUsuario(@RequestBody UserRequest userRequest) {

        try {
            // Verificar si el nombre de usuario ya existe
            if (usuarioService.existeUsuario(userRequest.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario ya está en uso");
            }

            // Verificar que los valores no sean nulos ni vacíos
            if (userRequest.getUsername() == null || userRequest.getPassword() == null ||
                    userRequest.getUsername().isEmpty() || userRequest.getPassword().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario y la contraseña son obligatorios");
            }

            //Registra el nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(userRequest.getUsername());

            // Encriptar la contraseña y almacenarla
            String passwordEncriptada = passwordEncoder.encode(userRequest.getPassword());
            nuevoUsuario.setPassword(passwordEncriptada);

            nuevoUsuario.setPhone(userRequest.getPhone());
            nuevoUsuario.setEmail(userRequest.getEmail());

            // Obtener el rol por nombre (puedes ajustar esto según tu lógica)
            Role rolUsuario = roleService.obtenerRolePorNombre("CLASIFICATOR");

            // Asignar el rol al usuario
            nuevoUsuario.addRole(rolUsuario);

            // Crear el usuario con el rol asociado
            usuarioService.crearUsuario(nuevoUsuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario");
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
