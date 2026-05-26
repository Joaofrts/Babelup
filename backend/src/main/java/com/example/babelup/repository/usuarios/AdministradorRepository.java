package com.example.babelup.repository.usuarios;

import com.example.babelup.entities.usuarios.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {
    Optional<Administrador> findByEmail(String email);
    boolean existsByEmail(String email);
}
