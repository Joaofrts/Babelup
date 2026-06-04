package com.example.babelup.service;

import com.example.babelup.entities.enumEntities.EnumStatusSessao;
import com.example.babelup.entities.enumEntities.EnumModalidadeSessao;
import com.example.babelup.entities.enumEntities.EnumTipoSessao;
import com.example.babelup.entities.avaliacao.SessaoConversacao;
import com.example.babelup.entities.usuarios.Professor;
import com.example.babelup.repository.pedagogicos.ModuloRepository;
import com.example.babelup.repository.pedagogicos.SessaoConversacaoRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import com.example.babelup.repository.usuarios.ProfessorRepository;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.usuarios.Aluno;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Service
public class SessaoConversacaoService {

    private final SessaoConversacaoRepository sessaoRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final ModuloRepository moduloRepository;
    private final ProgressoService progressoService;

    public SessaoConversacaoService(SessaoConversacaoRepository sessaoRepository,
                                    ProfessorRepository professorRepository,
                                    AlunoRepository alunoRepository,
                                    ModuloRepository moduloRepository,
                                    ProgressoService progressoService) {
        this.sessaoRepository = sessaoRepository;
        this.professorRepository = professorRepository;
        this.alunoRepository = alunoRepository;
        this.moduloRepository = moduloRepository;
        this.progressoService = progressoService;
    }

    // 1. Professor (ou Admin) abre um horário na agenda
    @Transactional
    public SessaoConversacao abrirSessao(UUID professorId, UUID moduloId, EnumTipoSessao tipoSessao,
                                         EnumModalidadeSessao modalidade, LocalDateTime dataHora, Integer maxAlunos) {

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor não encontrado"));

        SessaoConversacao sessao = new SessaoConversacao();
        sessao.setProfessor(professor);
        sessao.setTipoSessao(tipoSessao);
        sessao.setModalidadeSessao(modalidade);
        sessao.setDataHora(dataHora);
        sessao.setMaxAlunos(maxAlunos);
        sessao.setStatus(EnumStatusSessao.AGENDADA);

        if (tipoSessao != EnumTipoSessao.NIVELAMENTO_INICIAL && moduloId != null) {
            Modulo modulo = moduloRepository.findById(moduloId)
                    .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado"));
            sessao.setModulo(modulo);
        }

        return sessaoRepository.save(sessao);
    }

    // 2. Aluno se inscreve em uma sessão disponível
    @Transactional
    public SessaoConversacao inscreverAluno(UUID sessaoId, UUID alunoId) {

        SessaoConversacao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        // Validação 1: Status da Sessão
        if (sessao.getStatus() != EnumStatusSessao.AGENDADA) {
            throw new IllegalStateException("Esta sessão não está mais aberta para agendamentos.");
        }

        // Validação 2: Limite de vagas
        if (sessao.getAlunos().size() >= sessao.getMaxAlunos()) {
            throw new IllegalStateException("A sessão já atingiu o número máximo de alunos.");
        }

        // Validação 3: Evitar inscrição duplicada
        if (sessao.getAlunos().contains(aluno)) {
            throw new IllegalStateException("O aluno já está inscrito nesta sessão.");
        }

        // Validação 4: Regra de Negócio (RB-001) - Check de Progresso para sessões práticas
        if (sessao.getTipoSessao() == EnumTipoSessao.PRATICA_MODULO) {
            boolean concluiuModulo = progressoService.moduloFoiConcluido(aluno.getId(), sessao.getModulo().getId());
            if (!concluiuModulo) {
                throw new IllegalStateException("Você precisa concluir as aulas e exercícios deste módulo antes de agendar a conversação.");
            }
        }

        // Tudo certo, inscreve o aluno
        sessao.getAlunos().add(aluno);
        return sessaoRepository.save(sessao);
    }

    // 3. Professor registra que a sessão ocorreu e envia gravação
    @Transactional
    public SessaoConversacao finalizarSessao(UUID sessaoId, String gravacaoUrl) {
        SessaoConversacao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        sessao.setStatus(EnumStatusSessao.REALIZADA);
        sessao.setGravacaoUrl(gravacaoUrl);

        return sessaoRepository.save(sessao);
    }

    // Listar turmas abertas por módulo
    public List<SessaoConversacao> listarSessoesAbertasPorModulo(UUID moduloId) {
        return sessaoRepository.findByModuloIdAndStatus(moduloId, EnumStatusSessao.AGENDADA);
    }

    public List<SessaoConversacao> listarTodas() {
        return sessaoRepository.findAll();
    }

    public Optional<SessaoConversacao> obterSessao(UUID sessaoId) {
        return sessaoRepository.findById(sessaoId);
    }

    public List<SessaoConversacao> listarPorModulo(UUID moduloId) {
        return sessaoRepository.findByModuloId(moduloId);
    }

    public List<SessaoConversacao> listarPorProfessor(UUID professorId) {
        return sessaoRepository.findByProfessorId(professorId);
    }

    @Transactional
    public SessaoConversacao atualizarDataHora(UUID sessaoId, LocalDateTime novaDataHora) {
        SessaoConversacao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("SessÃ£o nÃ£o encontrada"));
        sessao.setDataHora(novaDataHora);
        return sessaoRepository.save(sessao);
    }

    @Transactional
    public void removerAluno(UUID sessaoId, UUID alunoId) {
        SessaoConversacao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("SessÃ£o nÃ£o encontrada"));
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno nÃ£o encontrado"));

        if (!sessao.getAlunos().contains(aluno)) {
            throw new IllegalArgumentException("Aluno nÃ£o encontrado no agendamento");
        }

        sessao.removerAluno(aluno);
        sessaoRepository.save(sessao);
    }

    @Transactional
    public void deletarSessao(UUID sessaoId) {
        sessaoRepository.deleteById(sessaoId);
    }
}
