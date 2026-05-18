package com.example.babelup.repository;

import com.example.babelup.entities.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo,Long> {
    List<Modulo> findByNivelIdOrderByOrdemSequencialAsc(Long nivelId);

    Optional<Modulo> findByIdAndNivelId(Long id, Long nivelId);

    @Query("SELECT m FROM Modulo m WHERE m.nivel.id = :nivelId AND m.ordemSequencial > :ordemSequencial ORDER BY m.ordemSequencial ASC LIMIT 1")
    Optional<Modulo> findNextModule(@Param("nivelId") Long nivelId, @Param("ordemSequencial") Integer ordemSequencial);
}

