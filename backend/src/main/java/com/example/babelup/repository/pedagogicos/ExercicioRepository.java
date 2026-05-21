package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExercicioRepository extends JpaRepository<Exercicio, UUID> {

    List<Exercicio> findByModuloId(UUID moduloId);

}
