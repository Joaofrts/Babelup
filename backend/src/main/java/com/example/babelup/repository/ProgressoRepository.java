package com.example.babelup.repository;

import com.example.babelup.entities.ProgressoAluno;
import com.example.babelup.repository.pedagogicos.ProgressoAlunoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressoRepository
