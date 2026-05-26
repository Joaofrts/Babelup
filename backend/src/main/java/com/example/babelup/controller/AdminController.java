package com.example.babelup.controller;

import com.example.babelup.dto.NovoUsuarioDto;
import com.example.babelup.dto.ProfessorCadastroDto;
import com.example.babelup.dto.UsuarioRespostaDTO;
import com.example.babelup.entities.usuarios.Usuario;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/cadastroProfessor")
    public ResponseEntity<Object> addProfessor(@RequestBody NovoUsuarioDto dto){
        try{
            Usuario resposta = service.cadastrarUsuario(dto);
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
            List<UsuarioRespostaDTO> dtos = service.findAll();
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um erro inesperado");
        }
    }

    @GetMapping("/teste")
    public ResponseEntity<String> testeRotaProtegida() {
        return ResponseEntity.ok("Você conseguiu acessar uma área VIP da BabelUp!");
    }

}
