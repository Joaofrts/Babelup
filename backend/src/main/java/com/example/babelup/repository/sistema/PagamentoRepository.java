package com.example.babelup.repository.sistema;

import com.example.babelup.entities.EnumStatusPagamento;
import com.example.babelup.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
    List<Pagamento> findByMatriculaId(UUID matriculaId);
    List<Pagamento> findByStatus(EnumStatusPagamento status);
}
