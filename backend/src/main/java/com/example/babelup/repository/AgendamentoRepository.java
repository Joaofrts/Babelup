package com.example.babelup.repository;

import com.example.babelup.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {
    List<Agendamento> findByModuloId(Long moduloId);
}

