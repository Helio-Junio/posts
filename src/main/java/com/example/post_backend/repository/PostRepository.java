package com.example.post_backend.repository;

import com.example.post_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUsuarioId(Long usuarioId);
    List<Post> findByUsuarioUsername(String username);
}
