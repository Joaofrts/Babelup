package com.example.babelup.controller;

import com.example.babelup.dto.AdicionarModuloDto;
import com.example.babelup.dto.RespostaModuloDto;
import com.example.babelup.dto.RespostaProgressoDto;
import com.example.babelup.dto.UpdateModuloDto;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.progressoGamificacao.ProgressoAluno;
import com.example.babelup.service.ModuloService;
import com.example.babelup.service.ProgressoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private static final Logger logger = LoggerFactory.getLogger(ModuloController.class);

    private final ModuloService moduloService;
    private final ProgressoService progressoService;

    public ModuloController(ModuloService moduloService, ProgressoService progressoService) {
        this.moduloService = moduloService;
        this.progressoService = progressoService;
    }

    @GetMapping({"/listar"})
    public ResponseEntity<Object> listarModulos() {
        try {
            List<RespostaModuloDto> dtos = moduloService.listarModulos().stream()
                    .map(RespostaModuloDto::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar mÃ³dulos: " + e.getMessage());
        }
    }

    @GetMapping("/nivel/{nivelId}")
    public ResponseEntity<Object> obterModulosPorNivel(@PathVariable UUID nivelId) {
        try {
            return ResponseEntity.ok(moduloService.obterModulosPorNivel(nivelId).stream()
                    .map(RespostaModuloDto::new)
                    .toList());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("ID de nÃ­vel invÃ¡lido: " + e.getMessage());
        } catch (Exception e) {
            logger.warn("Erro ao obter mÃ³dulos por nÃ­vel: nivelId={}, erro={}", nivelId, e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter mÃ³dulos do nÃ­vel: " + e.getMessage());
        }
    }

    @GetMapping("/{moduloId}/progresso/{alunoId}")
    public ResponseEntity<Object> obterProgresso(@PathVariable UUID moduloId, @PathVariable UUID alunoId) {
        ProgressoAluno progresso = progressoService.obterProgresso(alunoId, moduloId);
        if (progresso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Progresso nÃ£o encontrado");
        }
        return ResponseEntity.ok(new RespostaProgressoDto(progresso));
    }

    @GetMapping("/{moduloId}/pode-acessar/{alunoId}")
    public ResponseEntity<Object> podeAcessar(@PathVariable UUID moduloId, @PathVariable UUID alunoId) {
        return ResponseEntity.ok(Map.of("pode_acessar", progressoService.moduloFoiConcluido(alunoId, moduloId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> obterModulo(@PathVariable UUID id) {
        try {
            return moduloService.obterModulo(id)
                    .<ResponseEntity<Object>>map(modulo -> ResponseEntity.ok(new RespostaModuloDto(modulo)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("MÃ³dulo nÃ£o encontrado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao obter mÃ³dulo: " + e.getMessage());
        }
    }

    @PostMapping({"/criar"})
    public ResponseEntity<Object> criarModulo(@RequestBody AdicionarModuloDto dto) {
        try {
            if (dto.getNivelId() == null || dto.getTitulo() == null || dto.getTitulo().isBlank()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("nivelId e tÃ­tulo sÃ£o obrigatÃ³rios");
            }

            Modulo modulo = moduloService.criarModulo(dto.getNivelId(), dto.getTitulo(), dto.getDescricao(),
                    dto.getOrdem(), dto.getCargaHorariaMinima());
            return ResponseEntity.status(HttpStatus.CREATED).body(new RespostaModuloDto(modulo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarModulo(@PathVariable UUID id, @RequestBody UpdateModuloDto dto) {
        try {
            Modulo modulo = moduloService.atualizarModulo(id, dto.getTitulo(), dto.getDescricao(),
                    dto.getOrdem(), dto.getCargaHorariaMinima());
            return ResponseEntity.ok(new RespostaModuloDto(modulo));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarModulo(@PathVariable UUID id) {
        moduloService.deletarModulo(id);
        return ResponseEntity.noContent().build();
    }
}
