package com.example.babelup.controller;

import com.example.babelup.entities.estruturaAcademica.Modulo;
import com.example.babelup.entities.estruturaAcademica.Nivel;
import com.example.babelup.service.ModuloService;
import com.example.babelup.service.ProgressoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModuloControllerTest {

    @Mock
    private ModuloService moduloService;

    @Mock
    private ProgressoService progressoService;

    @Test
    void listarModulosRetornaOk() {
        Nivel nivel = new Nivel("Ingles", "Iniciante", 1);
        nivel.setId(UUID.randomUUID());
        Modulo modulo = new Modulo("Modulo inicial", "Descricao", 1, 10, nivel);
        modulo.setId(UUID.randomUUID());

        when(moduloService.listarModulos()).thenReturn(List.of(modulo));

        ModuloController controller = new ModuloController(moduloService, progressoService);
        ResponseEntity<Object> response = controller.listarModulos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void obterModuloInexistenteRetornaNotFound() {
        UUID moduloId = UUID.randomUUID();
        when(moduloService.obterModulo(moduloId)).thenReturn(Optional.empty());

        ModuloController controller = new ModuloController(moduloService, progressoService);
        ResponseEntity<Object> response = controller.obterModulo(moduloId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
