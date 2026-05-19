package com.example.babelup.service;

import com.example.babelup.entities.Modulo;
import com.example.babelup.entities.Nivel;
import com.example.babelup.repository.ModuloRepository;
import com.example.babelup.repository.NivelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private ProgressoService progressoService;

    // Criar novo módulo
    public Modulo criarModulo(Long nivelId, String titulo, String urlVideoaula, String urlPdf, Integer ordemSequencial) {
        Nivel nivel = nivelRepository.findById(nivelId)
                .orElseThrow(() -> new IllegalArgumentException("Nível não encontrado"));

        Modulo modulo = new Modulo();
        modulo.setNivel(nivel);
        modulo.setTitulo(titulo);
        modulo.setUrlVideoaula(urlVideoaula);
        modulo.setUrlPdf(urlPdf);
        modulo.setOrdemSequencial(ordemSequencial);
        
        return moduloRepository.save(modulo);
    }

    // Obter todos os módulos
    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }

    // Obter módulo por ID
    public Optional<Modulo> obterModulo(Long id) {
        return moduloRepository.findById(id);
    }

    // Obter módulos de um nível
    public List<Modulo> obterModulosPorNivel(Long nivelId) {
        return moduloRepository.findByNivelIdOrderByOrdemSequencialAsc(nivelId);
    }

    // Atualizar módulo
    public Modulo atualizarModulo(Long id, String titulo, String urlVideoaula, String urlPdf) {
        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado"));

        modulo.setTitulo(titulo);
        modulo.setUrlVideoaula(urlVideoaula);
        modulo.setUrlPdf(urlPdf);

        return moduloRepository.save(modulo);
    }

    // Deletar módulo
    public void deletarModulo(Long id) {
        moduloRepository.deleteById(id);
    }

    // Validar se aluno pode acessar o módulo
    // - Primeiro módulo (ordem_sequencial = 1) pode ser acessado livremente
    // - Outros módulos exigem conclusão do módulo anterior
    public boolean podeAcessarModulo(Long alunoId, Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo não encontrado"));

        // Primeiro módulo do nível pode ser acessado
        if (modulo.getOrdemSequencial() == 1) {
            return true;
        }

        // Buscar módulo anterior
        List<Modulo> modulosNivel = moduloRepository.findByNivelIdOrderByOrdemSequencialAsc(modulo.getNivel().getId());
        
        for (Modulo m : modulosNivel) {
            if (m.getOrdemSequencial() == modulo.getOrdemSequencial() - 1) {
                // Validar se módulo anterior foi concluído
                return progressoService.podeAcessarModulo(alunoId, moduloId, m.getId());
            }
        }

        return false;
    }

    // Obter próximo módulo na sequência
    public Optional<Modulo> obterProximoModulo(Long nivelId, Integer ordemAtual) {
        List<Modulo> modulos = moduloRepository.findByNivelIdOrderByOrdemSequencialAsc(nivelId);
        
        for (Modulo modulo : modulos) {
            if (modulo.getOrdemSequencial() == ordemAtual + 1) {
                return Optional.of(modulo);
            }
        }

        return Optional.empty();
    }
}
