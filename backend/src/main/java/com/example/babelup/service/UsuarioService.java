package com.example.babelup.service;

import com.example.babelup.controller.GamificacaoController;
import com.example.babelup.dto.AdicionarNivelDto;
import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.UsuarioRespostaDTO;
import com.example.babelup.entities.enumEntities.EnumPerfil;
import com.example.babelup.entities.enumEntities.EnumStatusMatricula;
import com.example.babelup.entities.enumEntities.EnumTipoVideo;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.financeiro.Matricula;
import com.example.babelup.entities.usuarios.Aluno;
import com.example.babelup.entities.usuarios.Professor;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.factory.UsuarioFactory;
import com.example.babelup.repository.usuarios.MatriculaRepository;
import com.example.babelup.repository.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final GamificacaoController gamificacaoController;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final NivelService nivelService;
    private final ModuloService moduloService;
    private final ConteudoAulaService conteudoAulaService;
    private final MatriculaRepository matriculaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioFactory usuarioFactory,
                          GamificacaoController gamificacaoController, NivelService nivelService,
                          ModuloService moduloService, ConteudoAulaService conteudoAulaService,
                          MatriculaRepository matriculaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioFactory = usuarioFactory;
        this.gamificacaoController = gamificacaoController;
        this.nivelService = nivelService;
        this.moduloService = moduloService;
        this.conteudoAulaService = conteudoAulaService;
        this.matriculaRepository = matriculaRepository;
    }

    public Usuario cadastrarUsuario(NovoUsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.email())) {
            throw new IllegalArgumentException("Este e-mail ja esta em uso na BabelUp.");
        }

        Usuario usuario = usuarioFactory.fabricar(usuarioDto);
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        vincularUsuarioAoPrimeiroNivelDisponivel(usuarioSalvo);

        return usuarioRepository.save(usuarioSalvo);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<UsuarioRespostaDTO> findAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioRespostaDTO(
                        u.getId(),
                        u.getNome(),
                        u.getEmail(),
                        u.getPerfil().name(),
                        obterCursoDoUsuario(u)))
                .toList();
    }

    public void startDb() {
        inicializarUsuariosMinimos();
        inicializarConteudoMinimo();
    }

    private void inicializarUsuariosMinimos() {
        List<NovoUsuarioDto> usuariosIniciais = List.of(
                new NovoUsuarioDto("Joao", "joao@babelup.com", "Joao123456", EnumPerfil.ADMIN, null, true, false, null),
                new NovoUsuarioDto("Rodrigo", "rodrigo@babelup.com", "Rodrigo123456", EnumPerfil.ALUNO, null, true, false, null),
                new NovoUsuarioDto("Lucas", "lucas@babelup.com", "Lucas123456", EnumPerfil.ALUNO, null, true, false, null),
                new NovoUsuarioDto("Ludmila", "ludmila@babelup.com", "Ludmila123456", EnumPerfil.PROFESSOR, null, true, false, null),
                new NovoUsuarioDto("Carol", "carol@babelup.com", "Carol123456", EnumPerfil.PROFESSOR, null, true, false, null)
        );

        for (NovoUsuarioDto usuario : usuariosIniciais) {
            if (!usuarioRepository.existsByEmail(usuario.email())) {
                cadastrarUsuario(usuario);
                System.out.println("Usuario criado na inicializacao: " + usuario.nome());
            }
        }
    }

    private void inicializarConteudoMinimo() {
        AdicionarNivelDto nivelDto = new AdicionarNivelDto(
                "Ingles",
                "Ingles Basico A1",
                1,
                40,
                "Nivel basico de introducao ao ingles.",
                new BigDecimal("99.90")
        );

        try {
            nivelService.criarNivel(nivelDto);
            System.out.println("Nivel criado: " + nivelDto.idioma() + " - " + nivelDto.nome());
        } catch (IllegalArgumentException e) {
            System.out.println("Nivel ja existe: " + nivelDto.idioma() + " - " + nivelDto.nome());
        }

        var nivel = nivelService.listarNiveis().stream()
                .filter(n -> "Ingles".equals(n.getIdioma()) && "Ingles Basico A1".equals(n.getNome()))
                .findFirst();

        if (nivel.isEmpty()) {
            System.out.println("Nivel base nao encontrado. Pulando seed de modulo e conteudo.");
            return;
        }

        UUID nivelId = nivel.get().getId();
        var modulo = moduloService.obterModulosPorNivel(nivelId).stream()
                .filter(m -> "Introducao ao Ingles".equals(m.getTitulo()))
                .findFirst()
                .orElseGet(() -> moduloService.criarModulo(
                        nivelId,
                        "Introducao ao Ingles",
                        "Primeiro modulo do nivel A1.",
                        1,
                        10
                ));

        if (conteudoAulaService.listarMateriaisPorModulo(modulo.getId()).stream()
                .noneMatch(material -> "Material de apoio A1".equals(material.getTitulo()))) {
            conteudoAulaService.adicionarMaterialApoio(
                    modulo.getId(),
                    "Material de apoio A1",
                    "https://example.com/pdf/material-a1.pdf"
            );
            System.out.println("PDF criado para o modulo: " + modulo.getTitulo());
        }

        var aula = conteudoAulaService.listarAulasPorModulo(modulo.getId()).stream()
                .filter(videoAula -> "Boas-vindas ao Ingles A1".equals(videoAula.getTitulo()))
                .findFirst()
                .orElseGet(() -> {
                    var novaAula = conteudoAulaService.adicionarVideoAula(
                            modulo.getId(),
                            "Boas-vindas ao Ingles A1",
                            "https://example.com/video/boas-vindas-a1.mp4",
                            8,
                            EnumTipoVideo.GRAVADO,
                            LocalDateTime.now()
                    );
                    System.out.println("Videoaula criada para o modulo: " + modulo.getTitulo());
                    return novaAula;
                });

        if (conteudoAulaService.listarExerciciosPorAula(aula.getId()).stream()
                .noneMatch(exercicio -> "Qual saudacao usamos para dizer ola?".equals(exercicio.getEnunciado()))) {
            conteudoAulaService.adicionarExercicioAula(
                    aula.getId(),
                    "Qual saudacao usamos para dizer ola?",
                    List.of("Ola", "Adeus", "Obrigado"),
                    "Ola"
            );
            System.out.println("Exercicio criado para a videoaula: " + aula.getTitulo());
        }

        vincularUsuariosAoNivelBase(nivel.get());
    }

    private void vincularUsuariosAoNivelBase(Nivel nivel) {
        vincularAlunoAoNivel("rodrigo@babelup.com", nivel);
        vincularAlunoAoNivel("lucas@babelup.com", nivel);
        vincularProfessorAoNivel("ludmila@babelup.com", nivel);
        vincularProfessorAoNivel("carol@babelup.com", nivel);
    }

    private void vincularUsuarioAoPrimeiroNivelDisponivel(Usuario usuario) {
        nivelService.listarNiveis().stream()
                .filter(nivel -> nivel.getOrdem() != null)
                .min(Comparator.comparing(Nivel::getOrdem))
                .ifPresent(nivel -> {
                    if (usuario instanceof Aluno aluno) {
                        vincularAlunoAoNivel(aluno, nivel);
                    } else if (usuario instanceof Professor professor) {
                        vincularProfessorAoNivel(professor, nivel);
                    }
                });
    }

    private void vincularAlunoAoNivel(String email, Nivel nivel) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            if (usuario instanceof Aluno aluno) {
                vincularAlunoAoNivel(aluno, nivel);
            }
        });
    }

    private void vincularAlunoAoNivel(Aluno aluno, Nivel nivel) {
        if (aluno.getNivelAtual() == null) {
            aluno.setNivelAtual(nivel);
            aluno.setProgressoGeral(0.0);
            usuarioRepository.save(aluno);
        }

        if (!matriculaRepository.existsByAlunoIdAndNivelId(aluno.getId(), nivel.getId())) {
            Matricula matricula = new Matricula();
            matricula.setAluno(aluno);
            matricula.setNivel(nivel);
            matricula.setIdioma(nivel.getIdioma());
            matricula.setPlano("Mensal");
            matricula.setStatus(EnumStatusMatricula.ATIVA);
            matriculaRepository.save(matricula);
        }
    }

    private void vincularProfessorAoNivel(String email, Nivel nivel) {
        usuarioRepository.findByEmail(email).ifPresent(usuario -> {
            if (usuario instanceof Professor professor) {
                vincularProfessorAoNivel(professor, nivel);
            }
        });
    }

    private void vincularProfessorAoNivel(Professor professor, Nivel nivel) {
        boolean alterado = false;

        if (professor.getNivelAtuacao() == null) {
            professor.setNivelAtuacao(nivel);
            alterado = true;
        }

        if (professor.getIdiomasLecionados() == null || professor.getIdiomasLecionados().isBlank()) {
            professor.setIdiomasLecionados(nivel.getIdioma());
            alterado = true;
        }

        if (professor.getDisponibilidade() == null || professor.getDisponibilidade().isBlank()) {
            professor.setDisponibilidade("Segunda a sexta, horario comercial");
            alterado = true;
        }

        if (alterado) {
            usuarioRepository.save(professor);
        }
    }

    private String obterCursoDoUsuario(Usuario usuario) {
        if (usuario instanceof Aluno aluno && aluno.getNivelAtual() != null) {
            return descreverNivel(aluno.getNivelAtual());
        }

        if (usuario instanceof Professor professor && professor.getNivelAtuacao() != null) {
            return descreverNivel(professor.getNivelAtuacao());
        }

        return null;
    }

    private String descreverNivel(Nivel nivel) {
        return nivel.getIdioma() + " - " + nivel.getNome();
    }
}
