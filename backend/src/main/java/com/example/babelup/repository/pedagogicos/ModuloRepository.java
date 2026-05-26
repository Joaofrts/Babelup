package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.estruturaAcademica.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, UUID> {
    List<Modulo> findByNivelIdOrderByOrdemAsc(UUID nivelId);

    Optional<Modulo> findByNivelIdAndOrdem(UUID id, int i);
}
