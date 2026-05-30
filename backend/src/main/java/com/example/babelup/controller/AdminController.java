package com.example.babelup.controller;

import com.example.babelup.dto.*;
import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.service.ModuloService;
import com.example.babelup.service.NivelService;
import com.example.babelup.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final NivelService nivelService;
    private final ModuloService moduloService;


    private final UsuarioService usuarioService;

    public AdminController(ModuloService moduloService, UsuarioService usuarioService, NivelService nivelService){
        this.moduloService = moduloService;
        this.usuarioService = usuarioService;
        this.nivelService = nivelService;
    }

    @PostMapping("/cadastroProfessor")
    public ResponseEntity<Object> addProfessor(@RequestBody NovoUsuarioDto dto){
        try{
            Usuario resposta = usuarioService.cadastrarUsuario(dto);
            return ResponseEntity.ok().body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar novo usuario.");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/listarUsuarios")
    public ResponseEntity<Object> listarUsuarios(){
        try {
            List<UsuarioRespostaDTO> dtos = usuarioService.findAll();
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um erro inesperado");
        }
    }

    @GetMapping("/teste")
    public ResponseEntity<String> testeRotaProtegida() {
        return ResponseEntity.ok("Você conseguiu acessar uma área VIP da BabelUp!");
    }

    // POST /admin/niveis/criar - Criar novo nível
    @PostMapping("/niveis/criar")
    public ResponseEntity<Object> criarNivel(@RequestBody AdicionarNivelDto dto) {
        try {
            if (dto.getNome() == null || dto.getNome().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome do nível é obrigatório");
            }

            Nivel nivel = nivelService.criarNivel(dto.getNome(), dto.getCargaHorariaEstimada());
            AdicionarNivelDto resposta = new AdicionarNivelDto(nivel.getId(), nivel.getNome(), nivel.getCargaHoraria());
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar nível: " + e.getMessage());
        }
    }

    // PUT /admin/niveis/{id} - Atualizar nível
    @PutMapping("/niveis/{id}")
    public ResponseEntity<Object> atualizarNivel(@PathVariable UUID id, @RequestBody AdicionarNivelDto dto) {
        try {
            Nivel nivel = nivelService.atualizarNivel(id, dto.getNome(), dto.getCargaHorariaEstimada());
            AdicionarNivelDto resposta = new AdicionarNivelDto(nivel.getId(), nivel.getNome(), nivel.getCargaHoraria());
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar nível: " + e.getMessage());
        }
    }

    // DELETE /admin/niveis/{id} - Deletar nível
    @DeleteMapping("/niveis/{id}")
    public ResponseEntity<Object> deletarNivel(@PathVariable UUID id) {
        try {
            nivelService.deletarNivel(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar nível: " + e.getMessage());
        }
    }

    @DeleteMapping("/modulo/{id}")
    public ResponseEntity<Object> deletarModulo(@PathVariable UUID id) {
        try {

            moduloService.deletarModulo(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar módulo: " + e.getMessage());
        }
    }
    @PostMapping("/modulo/criar")
    public ResponseEntity<Object> criarModulo(@RequestBody AdicionarModuloDto dto) {
        try {
            if (dto.getNivelId() == null || dto.getTitulo() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("nivelId e título são obrigatórios");
            }

            Modulo modulo = moduloService.criarModulo(dto.getNivelId(), dto.getTitulo(),dto.getDescricao(), dto.getOrdem(),dto.getCargaHorariaMinima());

            RespostaModuloDto resposta = new RespostaModuloDto( modulo.getId(),modulo.getTitulo(),modulo.getDescricao(),
                    modulo.getVideoAulas(), modulo.getMateriaisApoio(), modulo.getNivel().getId(),
                    modulo.getOrdem());
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar módulo: " + e.getMessage());
        }
    }

    @PutMapping("/modulo/{id}")
    public ResponseEntity<Object> atualizarModulo(@PathVariable UUID id, @RequestBody UpdateModuloDto dto) {
        try {
            Modulo modulo = moduloService.atualizarModulo(id, dto.getTitulo(),dto.getDescricao(),
                    dto.getOrdem(), dto.getCargaHorariaMinima());

            RespostaModuloDto resposta = new RespostaModuloDto(modulo);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar módulo: " + e.getMessage());
        }
    }

}
