package com.example.babelup.repository.sistema;

import com.example.babelup.entities.comunicacao.PostForum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostForumRepository extends JpaRepository<PostForum, UUID> {
    List<PostForum> findByUsuarioId(UUID usuarioId);
}
