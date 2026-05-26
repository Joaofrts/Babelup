package com.example.babelup.repository.sistema;

import com.example.babelup.entities.comunicacao.MensagemChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensagemChatRepository extends JpaRepository<MensagemChat, UUID> {
    List<MensagemChat> findByDestinatarioIdAndLidaFalse(UUID destinatarioId);
}
