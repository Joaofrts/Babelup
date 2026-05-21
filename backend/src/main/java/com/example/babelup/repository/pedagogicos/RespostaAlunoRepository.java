package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.RespostaAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RespostaAlunoRepository extends JpaRepository<RespostaAluno, UUID> {
    List<RespostaAluno> findByAlunoId(UUID alunoId);
}
