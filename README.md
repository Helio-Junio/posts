# API RESTful Spring Boot com JWT
API RESTful completa desenvolvida com Spring Boot 4.0.1, incluindo autenticação JWT, operações CRUD e relacionamento entre entidades.
## 🚀 Tecnologias

Java 25
Spring Boot 4.0.1
Spring Security com JWT
Spring Data JPA
MySQL (Aiven)
Maven
Lombok

## 📋 Requisitos

Java 25 ou superior
Maven 3.6+
MySQL (Aiven)
Conta no Render (para deploy)

## 🔧 Configuração
1. Clone o repositório
bashgit clone <seu-repositorio>
cd demo
2. Configure o banco de dados
Edite o arquivo src/main/resources/application.properties:
propertiesspring.datasource.url=jdbc:mysql://<SEU_HOST_AIVEN>:<PORTA>/demo?useSSL=true&requireSSL=true
spring.datasource.username=<SEU_USUARIO>
spring.datasource.password=<SUA_SENHA>
3. Compile e execute
bashmvn clean install
mvn spring-boot:run
A API estará disponível em http://localhost:8080

## 📡 Endpoints da API
Autenticação (Públicos)
### Registrar novo usuário
httpPOST /auth/register
Content-Type: application/json

'''{
  "username": "joao",
  "email": "joao@email.com",
  "password": "senha123"
}'''
Resposta:
'''json{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "joao",
  "email": "joao@email.com"
}'''
### Login
httpPOST /auth/login
Content-Type: application/json

'''{
  "username": "joao",
  "password": "senha123"
}'''
Resposta:
'''json{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "joao",
  "email": "joao@email.com"
}'''
Posts (Requer Autenticação)
⚠️ Todos os endpoints abaixo requerem o header:
Authorization: Bearer <seu-token-jwt>
### Criar post
httpPOST /posts
Content-Type: application/json
Authorization: Bearer <token>

'''{
  "titulo": "Meu primeiro post",
  "conteudo": "Conteúdo do post aqui..."
}'''
### Listar todos os posts
httpGET /posts
Authorization: Bearer <token>
### Buscar post por ID
httpGET /posts/1
Authorization: Bearer <token>
### Listar meus posts
httpGET /posts/meus-posts
Authorization: Bearer <token>
### Atualizar post
httpPUT /posts/1
Content-Type: application/json
Authorization: Bearer <token>

'''{
  "titulo": "Título atualizado",
  "conteudo": "Conteúdo atualizado..."
}'''
### Deletar post
httpDELETE /posts/1
Authorization: Bearer <token>

## 🗄️ Estrutura do Banco de Dados
Tabela: usuarios
'''sqlCREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    data_criacao DATETIME
);'''
Tabela: posts
'''sqlCREATE TABLE posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT,
    data_publicacao DATETIME,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);'''

## 🔐 Segurança
Senhas são criptografadas com BCrypt
Autenticação via JWT (JSON Web Token)
Token expira em 24 horas (configurável)
Apenas endpoints /auth/** são públicos
Usuários só podem editar/deletar seus próprios posts

## 📁 Estrutura do Projeto
'''src/main/java/com/example/demo/
├── controller/
│   ├── AuthController.java      # Endpoints de autenticação
│   └── PostController.java      # CRUD de posts
├── dto/
│   ├── AuthDTO.java             # DTOs de autenticação
│   └── PostDTO.java             # DTOs de posts
├── entity/
│   ├── Usuario.java             # Entidade Usuario
│   └── Post.java                # Entidade Post
├── repository/
│   ├── UsuarioRepository.java   # Repository do Usuario
│   └── PostRepository.java      # Repository do Post
├── security/
│   ├── SecurityConfig.java      # Configuração do Spring Security
│   ├── JwtUtil.java             # Utilitários JWT
│   ├── JwtAuthenticationFilter.java  # Filtro JWT
│   └── CustomUserDetailsService.java # UserDetailsService
└── DemoApplication.java         # Classe principal'''

## 👨‍💻 Autores
 - Hélio Ferreira
 - Guilherme Salatiel
 - Oscar Lara
 - Matheus Lima
