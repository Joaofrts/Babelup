package com.example.babelup.controller;

import com.example.babelup.dto.ModuloDto;
import com.example.babelup.dto.ProgressoDto;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.repository.usuarios.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    // GET /api/modulos/listar - Listar todos os módulos
    @GetMapping("/listar")
    public ResponseEntity<Object> listarModulos() {
        try {
            List<Modulo> modulos = moduloService.listarModulos();
            List<ModuloDto> dtos = modulos.stream()
                    .map(m -> new ModuloDto(m.getId(), m.getTitulo(), m.getUrlVideoaula(), 
                            m.getUrlPdf(), m.getNivel().getId(), m.getOrdemSequencial()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar módulos: " + e.getMessage());
        }
    }

    // GET /api/modulos/{id} - Obter módulo específico
    @GetMapping("/{id}")
    public ResponseEntity<Object> obterModulo(@PathVariable Long id) {
        try {
            Optional<Modulo> modulo = moduloService.obterModulo(id);
            if (modulo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Módulo não encontrado");
            }
            Modulo m = modulo.get();
            ModuloDto dto = new ModuloDto(m.getId(), m.getTitulo(), m.getUrlVideoaula(), 
                    m.getUrlPdf(), m.getNivel().getId(), m.getOrdemSequencial());
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter módulo: " + e.getMessage());
        }
    }

    // GET /api/modulos/nivel/{nivelId} - Obter módulos de um nível
    @GetMapping("/nivel/{nivelId}")
    public ResponseEntity<Object> obterModulosPorNivel(@PathVariable Long nivelId) {
        try {
            List<Modulo> modulos = moduloService.obterModulosPorNivel(nivelId);
            List<ModuloDto> dtos = modulos.stream()
                    .map(m -> new ModuloDto(m.getId(), m.getTitulo(), m.getUrlVideoaula(), 
                            m.getUrlPdf(), m.getNivel().getId(), m.getOrdemSequencial()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter módulos do nível: " + e.getMessage());
        }
    }

    // POST /api/modulos - Criar novo módulo
    @PostMapping("/criar")
    public ResponseEntity<Object> criarModulo(@RequestBody ModuloDto dto) {
        try {
            if (dto.getNivelId() == null || dto.getTitulo() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("nivelId e título são obrigatórios");
            }

            Modulo modulo = moduloService.criarModulo(dto.getNivelId(), dto.getTitulo(), 
                    dto.getUrlVideoaula(), dto.getUrlPdf(), dto.getOrdemSequencial());
            
            ModuloDto resposta = new ModuloDto(modulo.getId(), modulo.getTitulo(), 
                    modulo.getUrlVideoaula(), modulo.getUrlPdf(), modulo.getNivel().getId(), 
                    modulo.getOrdemSequencial());
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar módulo: " + e.getMessage());
        }
    }

    // PUT /api/modulos/{id} - Atualizar módulo
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarModulo(@PathVariable Long id, @RequestBody ModuloDto dto) {
        try {
            Modulo modulo = moduloService.atualizarModulo(id, dto.getTitulo(), 
                    dto.getUrlVideoaula(), dto.getUrlPdf());
            
            ModuloDto resposta = new ModuloDto(modulo.getId(), modulo.getTitulo(), 
                    modulo.getUrlVideoaula(), modulo.getUrlPdf(), modulo.getNivel().getId(), 
                    modulo.getOrdemSequencial());
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar módulo: " + e.getMessage());
        }
    }

    // DELETE /api/modulos/{id} - Deletar módulo
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarModulo(@PathVariable Long id) {
        try {
            moduloService.deletarModulo(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar módulo: " + e.getMessage());
        }
    }

    // POST /api/modulos/{moduloId}/submeter-exercicio - Registrar conclusão de exercício
    @PostMapping("/{moduloId}/submeter-exercicio")
    public ResponseEntity<Object> submeterExercicio(
            @PathVariable Long moduloId,
            @RequestParam Long alunoId,
            @RequestParam Double nota) {
        try {
            Usuario aluno = usuarioRepository.findById(alunoId)
                    .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
            
            Modulo modulo = moduloService.obterModulo(moduloId)
                    .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado"));

            Progresso progresso = progressoService.registrarConclusaoModulo(aluno, modulo, true, nota);
            
            ProgressoDto dto = new ProgressoDto(progresso.getId(), progresso.getAluno().getId(), 
                    progresso.getModulo().getId(), progresso.getExercicioConcluido(), 
                    progresso.getNotaExercicio(), progresso.getDataConclusao());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao submeter exercício: " + e.getMessage());
        }
    }

    // GET /api/modulos/{moduloId}/progresso/{alunoId} - Obter progresso do aluno
    @GetMapping("/{moduloId}/progresso/{alunoId}")
    public ResponseEntity<Object> obterProgresso(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            Progresso progresso = progressoService.obterProgresso(alunoId, moduloId);
            if (progresso == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Progresso não encontrado");
            }

            ProgressoDto dto = new ProgressoDto(progresso.getId(), progresso.getAluno().getId(), 
                    progresso.getModulo().getId(), progresso.getExercicioConcluido(), 
                    progresso.getNotaExercicio(), progresso.getDataConclusao());
            
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter progresso: " + e.getMessage());
        }
    }

    // GET /api/modulos/{moduloId}/pode-acessar/{alunoId} - Validar se aluno pode acessar módulo
    @GetMapping("/{moduloId}/pode-acessar/{alunoId}")
    public ResponseEntity<Object> podeAcessarModulo(@PathVariable Long moduloId, @PathVariable Long alunoId) {
        try {
            boolean pode = moduloService.podeAcessarModulo(alunoId, moduloId);
            return ResponseEntity.ok().body(new Object() {
                public final boolean pode_acessar = pode;
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao validar acesso: " + e.getMessage());
        }
    }
}

