package com.example.Autenticator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.Autenticator.entity.Role;
import com.example.Autenticator.entity.Usuario;
import com.example.Autenticator.repository.RoleRepository;
import com.example.Autenticator.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;

@Component
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${default.user.usernameAdmin}")
    private String defaultUsernameAdmin;

    @Value("${default.user.usernameClasificator}")
    private String defaultUsernameClasificator;

    @Value("${default.user.password}")
    private String defaultPassword;

    @Value("${default.user.phone}")
    private String defaultPhone;

    @Value("${default.user.email}")
    private String defaultEmail;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Obtener el rol de administrador
        Role adminRole = roleRepository.findByName("ADMIN");

        if (adminRole == null) {
            // Si el rol no existe se crea
            adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        // Insertar el usuario administrador si no existe
        if (!usuarioRepository.existsByUsername(defaultUsernameAdmin)) {
            Usuario adminUsuario = new Usuario();
            adminUsuario.setUsername(defaultUsernameAdmin);
            adminUsuario.setPassword(defaultPassword);
            adminUsuario.setPhone(defaultPhone);
            adminUsuario.setEmail(defaultEmail);

            // Agregar el rol al usuario y viceversa
            adminUsuario.addRole(adminRole);

            // Guardar el usuario en la base de datos
            usuarioRepository.save(adminUsuario);
        }

        // Obtener el rol de Clasificator
        Role clasificatorRole = roleRepository.findByName("CLASIFICATOR");

        if (clasificatorRole == null) {
            // Si el rol no existe, se crea
            clasificatorRole = new Role();
            clasificatorRole.setName("CLASIFICATOR");
            roleRepository.save(clasificatorRole);
        }

        // Insertar el usuario Clasificator si no existe
        if (!usuarioRepository.existsByUsername(defaultUsernameClasificator)) {
            Usuario clasificatorUsuario = new Usuario();
            clasificatorUsuario.setUsername(defaultUsernameClasificator);
            clasificatorUsuario.setPassword(defaultPassword);
            clasificatorUsuario.setPhone(defaultPhone);
            clasificatorUsuario.setEmail(defaultEmail);

            // Agregar el rol al usuario y viceversa
            clasificatorUsuario.addRole(clasificatorRole);

            // Guardar el usuario en la base de datos
            usuarioRepository.save(clasificatorUsuario);
        }
    }
}
