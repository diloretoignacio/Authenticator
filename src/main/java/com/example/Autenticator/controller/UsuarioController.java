package com.example.Autenticator.controller;

import com.example.Autenticator.config.JwtUtil;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.Autenticator.service.UsuarioService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

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

            usuarioService.crearUsuario(nuevoUsuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar el usuario");
        }
    }


    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerTodosLosUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> optionalUsuario = usuarioService.actualizarUsuario(id, usuarioActualizado);

        return optionalUsuario.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);

        return eliminado ?
                new ResponseEntity<>(HttpStatus.NO_CONTENT) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
