package com.example.post_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostRequest {
        private String titulo;
        private String conteudo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostResponse {
        private Long id;
        private String titulo;
        private String conteudo;
        private LocalDateTime dataPublicacao;
        private String username;
    }
}
