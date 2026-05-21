package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, UUID> {
    Optional<Nivel> findByNome(String nome);

}
