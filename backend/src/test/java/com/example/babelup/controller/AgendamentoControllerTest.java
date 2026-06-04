package com.example.babelup.controller;

import com.example.babelup.dto.AbrirSessaoDto;
import com.example.babelup.entities.avaliacao.SessaoConversacao;
import com.example.babelup.entities.enumEntities.EnumModalidadeSessao;
import com.example.babelup.entities.enumEntities.EnumStatusSessao;
import com.example.babelup.entities.enumEntities.EnumTipoSessao;
import com.example.babelup.entities.usuarios.Professor;
import com.example.babelup.service.SessaoConversacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgendamentoControllerTest {

    @Mock
    private SessaoConversacaoService sessaoService;

    @Test
    void obterAgendamentoInexistenteRetornaNotFound() {
        UUID sessaoId = UUID.randomUUID();
        when(sessaoService.obterSessao(sessaoId)).thenReturn(Optional.empty());

        AgendamentoController controller = new AgendamentoController(sessaoService);
        ResponseEntity<Object> response = controller.obter(sessaoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void criarAgendamentoRetornaCreated() {
        UUID professorId = UUID.randomUUID();
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);
        AbrirSessaoDto dto = new AbrirSessaoDto(professorId, null, EnumTipoSessao.NIVELAMENTO_INICIAL,
                EnumModalidadeSessao.INDIVIDUAL, dataHora, 5);

        Professor professor = new Professor("Professor Teste", "professor@babelup.com", "Senha12345");
        professor.setId(professorId);
        SessaoConversacao sessao = new SessaoConversacao();
        sessao.setId(UUID.randomUUID());
        sessao.setProfessor(professor);
        sessao.setTipoSessao(EnumTipoSessao.NIVELAMENTO_INICIAL);
        sessao.setModalidadeSessao(EnumModalidadeSessao.INDIVIDUAL);
        sessao.setDataHora(dataHora);
        sessao.setMaxAlunos(5);
        sessao.setStatus(EnumStatusSessao.AGENDADA);

        when(sessaoService.abrirSessao(professorId, null, EnumTipoSessao.NIVELAMENTO_INICIAL,
                EnumModalidadeSessao.INDIVIDUAL, dataHora, 5)).thenReturn(sessao);

        AgendamentoController controller = new AgendamentoController(sessaoService);
        ResponseEntity<Object> response = controller.criar(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
