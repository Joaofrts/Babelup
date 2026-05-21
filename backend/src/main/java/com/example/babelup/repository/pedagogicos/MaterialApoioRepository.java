package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.MaterialApoio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialApoioRepository extends JpaRepository<MaterialApoio,UUID> {
    List<MaterialApoio> findByModuloId(UUID moduloId);
}
