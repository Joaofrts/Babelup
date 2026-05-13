package com.example.babelup.repository;

import com.example.babelup.entities.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo,Long> {
    List<Modulo> findByNivelIdOrderByOrdemSequencialAsc(Long nivelId);
}
