package com.example.babelup.service;

import com.example.babelup.entities.Enum.EnumTipoTeste;
import com.example.babelup.entities.avaliacao.SessaoConversacao;
import com.example.babelup.entities.avaliacao.TesteDiagnostico;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.repository.pedagogicos.NivelRepository;
import com.example.babelup.repository.pedagogicos.SessaoConversacaoRepository;
import com.example.babelup.repository.pedagogicos.TesteDiagnosticoRepository;
import com.example.babelup.repository.usuarios.AlunoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AvaliacaoNivelamentoService {

    private final TesteDiagnosticoRepository avaliacaoRepository;
    private final SessaoConversacaoRepository sessaoRepository;
    private final AlunoRepository alunoRepository;
    private final NivelRepository nivelRepository;

    public AvaliacaoNivelamentoService(TesteDiagnosticoRepository avaliacaoRepository,
                                       SessaoConversacaoRepository sessaoRepository,
                                       AlunoRepository alunoRepository,
                                       NivelRepository nivelRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.sessaoRepository = sessaoRepository;
        this.alunoRepository = alunoRepository;
        this.nivelRepository = nivelRepository;
    }

    @Transactional
    public TesteDiagnostico avaliarAluno(UUID sessaoId, UUID alunoId, UUID nivelRecomendadoId,
                                         EnumTipoTeste tipoTeste, Double nota, String parecerProfessor) {

        SessaoConversacao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada."));

        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado."));

        Nivel nivel = nivelRepository.findById(nivelRecomendadoId)
                .orElseThrow(() -> new IllegalArgumentException("Nível recomendado não encontrado."));

        if (!sessao.getAlunos().contains(aluno)) {
            throw new IllegalStateException("O aluno não está inscrito nesta sessão e não pode ser avaliado.");
        }

        Optional<TesteDiagnostico> avaliacaoExistente = avaliacaoRepository.findBySessaoConversacaoIdAndAlunoId(sessaoId, alunoId);

        TesteDiagnostico avaliacao;
        if (avaliacaoExistente.isPresent()) {
            avaliacao = avaliacaoExistente.get();
        } else {
            avaliacao = new TesteDiagnostico();
            avaliacao.setSessaoConversacao(sessao);
            avaliacao.setAluno(aluno);
        }

        // Preenche os dados do parecer do professor
        avaliacao.setNivel(nivel);
        avaliacao.setTipo(tipoTeste);
        avaliacao.setResultado(nota);
        avaliacao.setParecerProfessor(parecerProfessor);

        // Se for um nivelamento de entrada, aqui no futuro você pode injetar o AlunoService
        // para atualizar o "nivelAtual" do aluno no cadastro dele.

        return avaliacaoRepository.save(avaliacao);
    }

    public TesteDiagnostico obterAvaliacaoDoAluno(UUID sessaoId, UUID alunoId) {
        return avaliacaoRepository.findBySessaoConversacaoIdAndAlunoId(sessaoId, alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Avaliação não encontrada para este aluno nesta sessão."));
    }
}
