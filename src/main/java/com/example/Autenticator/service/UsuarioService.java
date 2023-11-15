package com.example.Autenticator.service;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements UserDetailsService{

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

    public Usuario crearUsuario(Usuario nuevoUsuario) {
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


