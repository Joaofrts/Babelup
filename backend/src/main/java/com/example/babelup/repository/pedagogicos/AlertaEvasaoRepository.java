package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.comunicacao.AlertaEvasao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertaEvasaoRepository extends JpaRepository<AlertaEvasao, UUID> {
    boolean existsByAlunoIdAndVisualizadoFalse(UUID id);
}
