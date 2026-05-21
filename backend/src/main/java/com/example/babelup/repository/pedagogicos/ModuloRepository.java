package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, UUID> {
    List<Modulo> findByNivelIdOrderByOrdemAsc(UUID nivelId);
}
