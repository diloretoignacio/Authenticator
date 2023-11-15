package com.example.Autenticator.service;

import com.example.Autenticator.entity.Role;
import com.example.Autenticator.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role crearRole(Role nuevoRole) {
        return roleRepository.save(nuevoRole);
    }

    public Set<Role> obtenerTodosLosRoles() {
        return new HashSet<>(roleRepository.findAll());
    }

    public Role obtenerRolePorNombre(String nombre) {
        return roleRepository.findByName(nombre);
    }
}
