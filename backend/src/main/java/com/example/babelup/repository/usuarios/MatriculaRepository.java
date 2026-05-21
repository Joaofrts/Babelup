package com.example.babelup.repository.usuarios;

import com.example.babelup.entities.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {
    List<Matricula> findByAlunoId(UUID alunoId);

}
