package com.example.post_backend.controller;


import com.example.post_backend.dto.PostDTO;
import com.example.post_backend.entity.Post;
import com.example.post_backend.entity.Usuario;
import com.example.post_backend.repository.PostRepository;
import com.example.post_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostRepository postRepository;
    private final UsuarioRepository usuarioRepository;

    // CREATE - Criar um novo post
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO.PostRequest request,
                                        Authentication authentication) {
        try {
            String username = authentication.getName();
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Post post = new Post();
            post.setTitulo(request.getTitulo());
            post.setConteudo(request.getConteudo());
            post.setUsuario(usuario);

            Post savedPost = postRepository.save(post);

            PostDTO.PostResponse response = new PostDTO.PostResponse(
                    savedPost.getId(),
                    savedPost.getTitulo(),
                    savedPost.getConteudo(),
                    savedPost.getDataPublicacao(),
                    usuario.getUsername()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar post: " + e.getMessage());
        }
    }

    // READ - Listar todos os posts
    @GetMapping
    public ResponseEntity<List<PostDTO.PostResponse>> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO.PostResponse> response = posts.stream()
                .map(post -> new PostDTO.PostResponse(
                        post.getId(),
                        post.getTitulo(),
                        post.getConteudo(),
                        post.getDataPublicacao(),
                        post.getUsuario().getUsername()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // READ - Buscar post por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));

            PostDTO.PostResponse response = new PostDTO.PostResponse(
                    post.getId(),
                    post.getTitulo(),
                    post.getConteudo(),
                    post.getDataPublicacao(),
                    post.getUsuario().getUsername()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post não encontrado");
        }
    }

    // READ - Buscar posts do usuário logado
    @GetMapping("/meus-posts")
    public ResponseEntity<List<PostDTO.PostResponse>> getMyPosts(Authentication authentication) {
        String username = authentication.getName();
        List<Post> posts = postRepository.findByUsuarioUsername(username);

        List<PostDTO.PostResponse> response = posts.stream()
                .map(post -> new PostDTO.PostResponse(
                        post.getId(),
                        post.getTitulo(),
                        post.getConteudo(),
                        post.getDataPublicacao(),
                        post.getUsuario().getUsername()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // UPDATE - Atualizar um post
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody PostDTO.PostRequest request,
                                        Authentication authentication) {
        try {
            String username = authentication.getName();
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));

            if (!post.getUsuario().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem permissão para editar este post");
            }

            post.setTitulo(request.getTitulo());
            post.setConteudo(request.getConteudo());

            Post updatedPost = postRepository.save(post);

            PostDTO.PostResponse response = new PostDTO.PostResponse(
                    updatedPost.getId(),
                    updatedPost.getTitulo(),
                    updatedPost.getConteudo(),
                    updatedPost.getDataPublicacao(),
                    updatedPost.getUsuario().getUsername()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar post: " + e.getMessage());
        }
    }

    // DELETE - Deletar um post
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id,
                                        Authentication authentication) {
        try {
            String username = authentication.getName();
            Post post = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post não encontrado"));

            if (!post.getUsuario().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem permissão para deletar este post");
            }

            postRepository.delete(post);

            return ResponseEntity.ok("Post deletado com sucesso");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar post: " + e.getMessage());
        }
    }
}
