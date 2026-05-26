package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.pratica.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExercicioRepository extends JpaRepository<Exercicio, UUID> {

    List<Exercicio> findByVideoAulaId(UUID videoAulaId);

    long countByVideoAulaModuloId(UUID id);
}
