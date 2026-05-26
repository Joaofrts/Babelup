package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.pratica.VideoAula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoAulaRepository extends JpaRepository<VideoAula, UUID> {
    List<VideoAula> findByModuloId(UUID moduloId);
}
