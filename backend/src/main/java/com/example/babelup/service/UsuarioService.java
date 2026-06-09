package com.example.babelup.service;

import com.example.babelup.controller.GamificacaoController;
import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.UsuarioRespostaDTO;
import com.example.babelup.dto.AdicionarNivelDto;
import com.example.babelup.entities.enumEntities.EnumPerfil;
import com.example.babelup.entities.enumEntities.EnumTipoVideo;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.factory.UsuarioFactory;
import com.example.babelup.repository.usuarios.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final GamificacaoController gamificacaoController;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final NivelService nivelService;
    private final ModuloService moduloService;
    private final ConteudoAulaService conteudoAulaService;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioFactory usuarioFactory,
                         GamificacaoController gamificacaoController, NivelService nivelService,
                         ModuloService moduloService, ConteudoAulaService conteudoAulaService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioFactory = usuarioFactory;
        this.gamificacaoController = gamificacaoController;
        this.nivelService = nivelService;
        this.moduloService = moduloService;
        this.conteudoAulaService = conteudoAulaService;
    }


    public Usuario cadastrarUsuario(NovoUsuarioDto usuarioDto) {
        if (usuarioRepository.existsByEmail(usuarioDto.email())) {
            throw new IllegalArgumentException("Este e-mail já está em uso na BabelUp.");
        }
        Usuario usuario = usuarioFactory.fabricar(usuarioDto);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }


    public List<UsuarioRespostaDTO> findAll(){
        return usuarioRepository.findAll().stream()
                .map(u-> new UsuarioRespostaDTO(u.getNome(), u.getEmail(), u.getPerfil().name()))
                .toList();
    }


    public void startDb(){
        // ===== CRIAÇÃO DE USUÁRIOS INICIAIS =====
        List<NovoUsuarioDto> usuariosIniciais = Arrays.asList(
                new NovoUsuarioDto("João Marcelo", "joao@gmail.com", "Joao123456", EnumPerfil.ADMIN, null, true,false,null),
                new NovoUsuarioDto("Ludmila", "ludmila@gmail.com", "Ludmila123", EnumPerfil.PROFESSOR, null, true,false,null),
                new NovoUsuarioDto("Rodrigo Santos", "rodrigo@gmail.com", "Rodrigo123", EnumPerfil.ALUNO, null, true,false,null),
                new NovoUsuarioDto("Lucas Lima", "lucas@gmail.com", "Lucas123", EnumPerfil.ALUNO, null, true,false,null),
                new NovoUsuarioDto("Maria Silva", "maria@gmail.com", "Maria1234", EnumPerfil.ALUNO, null, true,false,null),
                new NovoUsuarioDto("Pedro Costa", "pedro@gmail.com", "Pedro1234", EnumPerfil.ALUNO, null, true,false,null),
                new NovoUsuarioDto("Ana Santos", "ana@gmail.com", "Ana123456", EnumPerfil.ALUNO, null, true,false,null)
        );

        for (NovoUsuarioDto u : usuariosIniciais) {
            if (!usuarioRepository.existsByEmail(u.email())) {
                cadastrarUsuario(u);
                System.out.println("Usuário criado na inicialização: " + u.nome());
            }
        }

        // ===== CRIAÇÃO DE NÍVEIS DE IDIOMA (PORTUGUÊS) =====
        inicializarNiveis();

        // ===== CRIAÇÃO DE MÓDULOS E CONTEÚDO =====
        inicializarModulosEConteudo();
    }

    /**
     * Inicializa os níveis de aprendizado de Português (A1, A2, B1, B2)
     */
    private void inicializarNiveis(){
        List<AdicionarNivelDto> niveis = Arrays.asList(
                new AdicionarNivelDto("Português", "A1", 1, 40,
                        "Nível básico - Introdução fundamental ao idioma português", new BigDecimal("99.90")),
                new AdicionarNivelDto("Português", "A2", 2, 60,
                        "Nível básico - Desenvolvimento de competências elementares", new BigDecimal("99.90")),
                new AdicionarNivelDto("Português", "B1", 3, 80,
                        "Nível intermediário - Comunicação em tópicos familiares", new BigDecimal("129.90")),
                new AdicionarNivelDto("Português", "B2", 4, 100,
                        "Nível intermediário - Fluência em comunicação geral", new BigDecimal("129.90"))
        );

        for (AdicionarNivelDto nivelDto : niveis) {
            try {
                nivelService.criarNivel(nivelDto);
                System.out.println("Nível criado: " + nivelDto.idioma() + " - " + nivelDto.nome());
            } catch (IllegalArgumentException e) {
                System.out.println("Nível já existe: " + nivelDto.idioma() + " - " + nivelDto.nome());
            }
        }
    }

    /**
     * Inicializa os módulos e conteúdo pedagógico para os níveis
     */
    private void inicializarModulosEConteudo(){
        // Obter os níveis criados
        var niveis = nivelService.listarNiveis();

        if (niveis.isEmpty()) {
            System.out.println("Nenhum nível encontrado. Pulando criação de módulos.");
            return;
        }

        // Iterar pelos níveis e criar módulos
        for (var nivel : niveis) {
            criarModulosParaNivel(nivel.getId(), nivel.getNome());
        }
    }

    /**
     * Cria módulos e conteúdo para um nível específico
     */
    private void criarModulosParaNivel(java.util.UUID nivelId, String nomeTipo){
        List<ModuloInfo> modulosInfo = Arrays.asList(
                new ModuloInfo(
                        1,
                        "Introdução e Cumprimentos",
                        "Aprenda as formas básicas de saudação e apresentação em português.",
                        10,
                        Arrays.asList(
                                new VideoAulaInfo("Olá! Como se apresentar", "https://example.com/video1.mp4", 5),
                                new VideoAulaInfo("Cumprimentos Comuns", "https://example.com/video2.mp4", 8)
                        ),
                        Arrays.asList(
                                "Guia de Saudações - PDF",
                                "Vocabulário Inicial - PDF"
                        )
                ),
                new ModuloInfo(
                        2,
                        "Números e Datas",
                        "Aprenda números, horas e expressões temporais em português.",
                        12,
                        Arrays.asList(
                                new VideoAulaInfo("Números de 0 a 100", "https://example.com/video3.mp4", 7),
                                new VideoAulaInfo("Dizendo a Hora", "https://example.com/video4.mp4", 6)
                        ),
                        Arrays.asList(
                                "Tabela de Números - PDF",
                                "Expressões Temporais - PDF"
                        )
                ),
                new ModuloInfo(
                        3,
                        "Verbos Comuns",
                        "Conheça os verbos mais utilizados no dia a dia.",
                        15,
                        Arrays.asList(
                                new VideoAulaInfo("Verbo Estar - Conjugações", "https://example.com/video5.mp4", 9),
                                new VideoAulaInfo("Verbo Ir - Usos Práticos", "https://example.com/video6.mp4", 8)
                        ),
                        Arrays.asList(
                                "Tabela de Conjugações - PDF",
                                "Exercícios de Verbos - PDF"
                        )
                )
        );

        for (ModuloInfo modInfo : modulosInfo) {
            var moduloExistente = moduloService.listarModulos().stream()
                    .filter(m -> m.getTitulo().equals(modInfo.titulo) &&
                               m.getNivel().getId().equals(nivelId))
                    .findFirst();

            if (moduloExistente.isEmpty()) {
                // Criar o módulo
                var modulo = moduloService.criarModulo(nivelId, modInfo.titulo,
                                                       modInfo.descricao, modInfo.ordem,
                                                       modInfo.cargaHorariaMinima);
                System.out.println("Módulo criado: " + modInfo.titulo + " para o nível " + nomeTipo);

                // Adicionar videoaulas
                for (VideoAulaInfo videoInfo : modInfo.videoAulas) {
                    try {
                        conteudoAulaService.adicionarVideoAula(modulo.getId(), videoInfo.titulo,
                                                              videoInfo.url, videoInfo.duracao,
                                                              EnumTipoVideo.GRAVADO, LocalDateTime.now());
                        System.out.println("  ✓ Videoaula adicionada: " + videoInfo.titulo);
                    } catch (Exception e) {
                        System.out.println("  ✗ Erro ao adicionar videoaula: " + videoInfo.titulo);
                    }
                }

                // Adicionar materiais de apoio
                for (String materialTitulo : modInfo.materiais) {
                    try {
                        conteudoAulaService.adicionarMaterialApoio(modulo.getId(), materialTitulo,
                                                                   "https://example.com/pdf/material.pdf");
                        System.out.println("  ✓ Material de apoio adicionado: " + materialTitulo);
                    } catch (Exception e) {
                        System.out.println("  ✗ Erro ao adicionar material: " + materialTitulo);
                    }
                }
            } else {
                System.out.println("Módulo já existe: " + modInfo.titulo);
            }
        }
    }

    /**
     * Classe interna para organizar informações de módulo
     */
    private static class ModuloInfo {
        Integer ordem;
        String titulo;
        String descricao;
        Integer cargaHorariaMinima;
        List<VideoAulaInfo> videoAulas;
        List<String> materiais;

        ModuloInfo(Integer ordem, String titulo, String descricao, Integer cargaHorariaMinima,
                  List<VideoAulaInfo> videoAulas, List<String> materiais) {
            this.ordem = ordem;
            this.titulo = titulo;
            this.descricao = descricao;
            this.cargaHorariaMinima = cargaHorariaMinima;
            this.videoAulas = videoAulas;
            this.materiais = materiais;
        }
    }

    /**
     * Classe interna para organizar informações de videoaula
     */
    private static class VideoAulaInfo {
        String titulo;
        String url;
        Integer duracao;

        VideoAulaInfo(String titulo, String url, Integer duracao) {
            this.titulo = titulo;
            this.url = url;
            this.duracao = duracao;
        }
    }
}
