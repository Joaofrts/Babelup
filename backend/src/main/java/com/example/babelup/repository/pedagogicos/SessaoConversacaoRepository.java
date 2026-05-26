package com.example.babelup.repository.pedagogicos;

import com.example.babelup.entities.Enum.EnumStatusSessao;
import com.example.babelup.entities.avaliacao.SessaoConversacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessaoConversacaoRepository extends JpaRepository<SessaoConversacao, UUID> {
    List<SessaoConversacao> findByProfessorId(UUID professorId);
    List<SessaoConversacao> findByModuloId(UUID moduloId);

    List<SessaoConversacao> findByModuloIdAndStatus(UUID moduloId, EnumStatusSessao enumStatusSessao);
}

