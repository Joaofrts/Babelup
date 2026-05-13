package com.example.babelup.controller;

import com.example.babelup.dto.UsuarioDto;
import com.example.babelup.entities.Usuario;
import com.example.babelup.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/add")
    public ResponseEntity<Object> addUsuario(@RequestBody UsuarioDto dto){
        try{
            service.cadastrarAluno(dto);
            return ResponseEntity.ok().body("Item adicionado com sucesso!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um erro inesperado");
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<Object> findAll(){
        try {
            List<UsuarioDto> dtos = service.findAll();
            return ResponseEntity.ok().body(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Houve um erro inesperado");
        }
    }

}
