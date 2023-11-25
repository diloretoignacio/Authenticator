package com.example.Autenticator.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import com.example.Autenticator.service.UsuarioService;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    private UsuarioService usuarioService;

    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.extraerTokenDesdeHeader(request);

        if (token != null && jwtUtil.validarToken(token)) {
            String username = jwtUtil.extraerUsuarioDelToken(token);
            List<GrantedAuthority> authorities = obtenerRolesDelUsuario(username);

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }


    private List<GrantedAuthority> obtenerRolesDelUsuario(String username) {
        // Lógica para obtener roles del usuario a partir de tu servicio o repositorio
        String rol = usuarioService.obtenerRolPorUsuario(username);

        // Lista de GrantedAuthority que representan los roles del usuario
        return Collections.singletonList(new SimpleGrantedAuthority(rol));
    }
}
