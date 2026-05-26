package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.avaliacao.TesteDiagnostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TesteDiagnosticoRepository extends JpaRepository<TesteDiagnostico, UUID> {
    List<TesteDiagnostico> findByAlunoId(UUID alunoId);

    Optional<TesteDiagnostico> findBySessaoConversacaoIdAndAlunoId(UUID sessaoId, UUID alunoId);
}
