package com.example.babelup.repository;

import com.example.babelup.entities.Nivel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface NivelRepository extends JpaRepository<Nivel,Long> {
    Optional<Nivel> findByNome(String nome);

}
