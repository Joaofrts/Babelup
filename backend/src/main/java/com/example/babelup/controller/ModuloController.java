package com.example.babelup.controller;

import com.example.babelup.dto.ModuloComProgressoDto;
import com.example.babelup.dto.ModuloDto;
import com.example.babelup.dto.ProgressoDto;
import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Progresso;
import com.example.babelup.entities.Usuario;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.UsuarioRepository;
import com.example.babelup.service.ModuloService;
import com.example.babelup.service.ProgressoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    @Autowired
    private ProgressoService progressoService;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // ========================
    // ENDPOINTS DO ALUNO
    // ========================

    @GetMapping("/nivel/{nivelId}/disponiveis/{alunoId}")
    public ResponseEntity<Object> listarModulosDisponiveis(@PathVariable Long nivelId, @PathVariable Long alunoId) {
        try {
            usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            List<Modulo> modulos = moduloService.listarModulosDisponiveis(alunoId, nivelId);
            List<ModuloComProgressoDto> dtos = modulos.stream()
                    .map(m -> {
                        Optional<Progresso> progresso = progressoService.obterProgresso(alunoId, m.getId());
                        return new ModuloComProgressoDto(m, progresso.orElse(null), true);
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok().body(dtos);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar módulos");
        }
    }

    @PostMapping("/{moduloId}/assistir-aula/{alunoId}")
    public ResponseEntity<Object> assistirAula(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            Usuario aluno = usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            if (!moduloService.podeAcessarModulo(alunoId, modulo)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem acesso a este módulo ainda");
            }

            Progresso progresso = progressoService.marcarAulaAssistida(alunoId, modulo, aluno);
            return ResponseEntity.ok().body(new ProgressoDto(progresso));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registrar visualização de aula");
        }
    }

    @PostMapping("/{moduloId}/realizar-exercicio/{alunoId}")
    public ResponseEntity<Object> realizarExercicio(@PathVariable Long moduloId, @PathVariable Long alunoId,
                                                     @RequestBody ExercicioRequest request) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            Usuario aluno = usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            if (!moduloService.podeAcessarModulo(alunoId, modulo)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem acesso a este módulo ainda");
            }

            Progresso progresso = progressoService.submeterExercicio(alunoId, modulo, aluno, request.getNota());
            return ResponseEntity.ok().body(new ProgressoDto(progresso));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao submeter exercício");
        }
    }

    @GetMapping("/{moduloId}/pdf/{alunoId}")
    public ResponseEntity<Object> lerConteudoPdf(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            
            usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            if (!moduloService.podeAcessarModulo(alunoId, modulo)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem acesso a este módulo ainda");
            }

            if (modulo.getUrlPdf() == null || modulo.getUrlPdf().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("PDF não disponível para este módulo");
            }

            progressoService.marcarAulaAssistida(alunoId, modulo, usuarioRepository.findById(alunoId).get());

            return ResponseEntity.ok().body(new PdfResponse(modulo.getUrlPdf(), modulo.getTitulo()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao acessar PDF");
        }
    }

    @PostMapping("/{moduloId}/teste-final/{alunoId}")
    public ResponseEntity<Object> submeterTesteFinal(@PathVariable Long moduloId, @PathVariable Long alunoId,
                                                      @RequestBody TesteFinalRequest request) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            Usuario aluno = usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            if (!moduloService.podeAcessarModulo(alunoId, modulo)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem acesso a este módulo ainda");
            }

            Progresso progresso = progressoService.submeterTesteFinal(alunoId, modulo, aluno, request.getNota());
            ProgressoDto dto = new ProgressoDto(progresso);

            if (progresso.getExercicioConcluido()) {
                return ResponseEntity.ok()
                        .body(new TesteFinalResponse(
                                dto,
                                true,
                                "Parabéns! Você foi aprovado e pode prosseguir para o próximo módulo."
                        ));
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(new TesteFinalResponse(
                                dto,
                                false,
                                "Nota insuficiente. Mínimo necessário: 7.0"
                        ));
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao submeter teste final");
        }
    }

    @GetMapping("/{moduloId}/progresso/{alunoId}")
    public ResponseEntity<Object> obterProgresso(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            moduloService.obterPorId(moduloId);
            usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            Optional<Progresso> progresso = progressoService.obterProgresso(alunoId, moduloId);
            if (progresso.isPresent()) {
                return ResponseEntity.ok().body(new ProgressoDto(progresso.get()));
            } else {
                return ResponseEntity.ok().body("Aluno ainda não iniciou este módulo");
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter progresso");
        }
    }

    @GetMapping("/{moduloId}/pode-agendar/{alunoId}")
    public ResponseEntity<Object> verificarPodeAgendarConversacao(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            moduloService.obterPorId(moduloId);
            usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalStateException("Aluno não encontrado"));

            boolean podeAgendar = progressoService.verificarModuloConcluido(alunoId, moduloId);
            return ResponseEntity.ok().body(new CanAgendResponse(
                    podeAgendar,
                    podeAgendar ? "Você pode agendar uma conversação!" : 
                                  "Complete o módulo antes de agendar uma conversação."
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao verificar permissão de agendamento");
        }
    }

    // ========================
    // ENDPOINTS DO PROFESSOR
    // ========================

    @PostMapping("/{moduloId}/upload-video")
    public ResponseEntity<Object> uploadVideoAula(@PathVariable Long moduloId, @RequestBody UploadRequest request) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            modulo.setUrlVideoaula(request.getUrl());
            Modulo atualizado = moduloService.atualizar(modulo);
            return ResponseEntity.ok().body(new ModuloDto(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao fazer upload do vídeo");
        }
    }

    @PostMapping("/{moduloId}/upload-pdf")
    public ResponseEntity<Object> uploadPdf(@PathVariable Long moduloId, @RequestBody UploadRequest request) {
        try {
            Modulo modulo = moduloService.obterPorId(moduloId);
            modulo.setUrlPdf(request.getUrl());
            Modulo atualizado = moduloService.atualizar(modulo);
            return ResponseEntity.ok().body(new ModuloDto(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao fazer upload do PDF");
        }
    }

    @PutMapping("/{moduloId}")
    public ResponseEntity<Object> atualizarModulo(@PathVariable Long moduloId, @RequestBody Modulo modulo) {
        try {
            modulo.setId(moduloId);
            Modulo atualizado = moduloService.atualizar(modulo);
            return ResponseEntity.ok().body(new ModuloDto(atualizado));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar módulo");
        }
    }

    // Inner classes para request/response
    static class ExercicioRequest {
        private Double nota;

        public ExercicioRequest() {}

        public Double getNota() {
            return nota;
        }

        public void setNota(Double nota) {
            this.nota = nota;
        }
    }

    static class TesteFinalRequest {
        private Double nota;

        public TesteFinalRequest() {}

        public Double getNota() {
            return nota;
        }

        public void setNota(Double nota) {
            this.nota = nota;
        }
    }

    static class UploadRequest {
        private String url;

        public UploadRequest() {}

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    static class PdfResponse {
        private String url;
        private String titulo;

        public PdfResponse(String url, String titulo) {
            this.url = url;
            this.titulo = titulo;
        }

        public String getUrl() {
            return url;
        }

        public String getTitulo() {
            return titulo;
        }
    }

    static class TesteFinalResponse {
        private ProgressoDto progresso;
        private Boolean aprovado;
        private String mensagem;

        public TesteFinalResponse(ProgressoDto progresso, Boolean aprovado, String mensagem) {
            this.progresso = progresso;
            this.aprovado = aprovado;
            this.mensagem = mensagem;
        }

        public ProgressoDto getProgresso() {
            return progresso;
        }

        public Boolean getAprovado() {
            return aprovado;
        }

        public String getMensagem() {
            return mensagem;
        }
    }

    static class CanAgendResponse {
        private Boolean podeAgendar;
        private String mensagem;

        public CanAgendResponse(Boolean podeAgendar, String mensagem) {
            this.podeAgendar = podeAgendar;
            this.mensagem = mensagem;
        }

        public Boolean getPodeAgendar() {
            return podeAgendar;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}

