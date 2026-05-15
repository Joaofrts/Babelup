package com.example.babelup.controller;

import com.example.babelup.dto.UsuarioDto;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/cadastro")
    public ResponseEntity<Object> addUsuario(@RequestBody UsuarioDto dto){
        try{
            UsuarioDto resposta = new UsuarioDto(service.cadastrarAluno(dto));
            return ResponseEntity.ok().body(resposta);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
    }

    @GetMapping("/listarUsuarios")
    public ResponseEntity<Object> listarUsuarios(){
        try {
            List<UsuarioDto> dtos = service.findAll();
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
