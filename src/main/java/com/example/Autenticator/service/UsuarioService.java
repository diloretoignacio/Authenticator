package com.example.Autenticator.service;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



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

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleService roleService;

    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario crearUsuario(UserRequest userRequest) {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Verificar si el nombre de usuario ya existe
        if (existeUsuario(userRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Verificar que los valores no sean nulos ni vacíos
        if (userRequest.getUsername() == null || userRequest.getPassword() == null ||
                userRequest.getUsername().isEmpty() || userRequest.getPassword().isEmpty()) {
            throw new RuntimeException("El nombre de usuario y la contraseña son obligatorios");
        }

        // Registra el nuevo usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(userRequest.getUsername());

        // Encriptar la contraseña y almacenarla
        String passwordEncriptada = passwordEncoder.encode(userRequest.getPassword());
        nuevoUsuario.setPassword(passwordEncriptada);

        nuevoUsuario.setPhone(userRequest.getPhone());
        nuevoUsuario.setEmail(userRequest.getEmail());

        // Obtener el rol por nombre (ajustar según tu lógica)
        Role rolUsuario = roleService.obtenerRolePorNombre("CLASIFICATOR");

        // Asignar el rol al usuario
        nuevoUsuario.addRole(rolUsuario);

        // Crear el usuario con el rol asociado
        return usuarioRepository.save(nuevoUsuario);
    }

    public boolean existeUsuario(String nombreUsuario) {
        // Verificar si existe un usuario con el nombre dado
        return usuarioRepository.findByUsername(nombreUsuario).isPresent();
    }

    public Optional<Usuario> actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuarioExistente = optionalUsuario.get();
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
            usuarioExistente.setPassword(usuarioActualizado.getPassword());
            return Optional.of(usuarioRepository.save(usuarioExistente));
        } else {
            return Optional.empty();
        }
    }

    public boolean eliminarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);

        return usuarioOptional.map(usuario -> {
            List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toList());

            System.out.println("Usuario: " + usuario.getUsername() + ", Roles: " + authorities);

            return new org.springframework.security.core.userdetails.User(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    authorities);
        }).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public String obtenerRolPorUsuario(String username) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        Usuario usuario = usuarioOptional.orElse(null);

        if (usuario != null && usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            return usuario.getRoles().iterator().next().getName();
        }

        return "USER"; // Rol predeterminado si no se encuentra el rol del usuario
    }
}
