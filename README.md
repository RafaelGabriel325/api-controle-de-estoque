# Controle de Estoque

## Descrição
Aplicação back-end em Spring Boot com banco de dados PostgreSQL, rodando em Docker. Esta aplicação permite a comunicação cliente-servidor através da criação de APIs, onde o cliente é a aplicação web e o servidor armazena o banco de dados com as informações do sistema.

## Requisitos
- Docker instalado no computador.

## Como Rodar a Aplicação
1. Entre no terminal (CMD) na pasta do projeto.
2. Execute o comando:
   ```bash
   docker-compose up


# Tecnologias Utilizadas
## Spring Boot: Framework para a criação do back-end e APIs RESTful.
## PostgreSQL: Sistema gerenciador de banco de dados objeto-relacional, escolhido pela praticidade nas consultas e segurança.
Arquitetura do Sistema
A arquitetura do sistema é dividida em camadas responsáveis pela troca de informações entre a interface do usuário e os dados no banco.

# Camadas e Pacotes - Back-End
## Config
Contém classes de configuração da aplicação, como configuração de segurança, banco de dados, CORS, etc.

## Controller
Responsável por receber as requisições HTTP do cliente, processar essas requisições e retornar as respostas apropriadas. Contém classes que atuam como controladores REST, mapeando endpoints para métodos específicos.

## Exception
Define as exceções personalizadas usadas na aplicação. Contém classes para diferentes tipos de erros que podem ocorrer durante a execução da aplicação.

## Handler
Contém classes que lidam com exceções e erros globais. Utilizado para capturar exceções de forma centralizada e retornar respostas consistentes para o cliente.

## Model
1. DTO (Data Transfer Object): Contém classes usadas para transferir dados entre as camadas da aplicação, especialmente entre o cliente e o servidor.
2. Entity: Define as entidades JPA que mapeiam tabelas do banco de dados.
3. Mapper: Contém classes que mapeiam entidades para DTOs e vice-versa, facilitando a conversão de dados.

## Repository
Contém interfaces que estendem JpaRepository ou outras interfaces do Spring Data JPA. Responsável pela comunicação com o banco de dados, permitindo operações CRUD e consultas personalizadas.

## Security
Contém classes relacionadas à segurança da aplicação, como configuração de autenticação e autorização. Inclui filtros de segurança, provedores de autenticação e outras implementações necessárias para proteger a aplicação.

## Service
1. Service: Define interfaces para os serviços que implementam a lógica de negócios da aplicação.
2. Service Impl: Implementa as interfaces de serviço, contendo a lógica de negócios real e chamando métodos do repositório conforme necessário.

## Shared
Constant: Contém constantes usadas em toda a aplicação, facilitando a manutenção e evitando a duplicação de valores estáticos.

## Utils
Contém classes utilitárias e funções auxiliares.

## Docker-Config
Inicialização dos Dockerfile.

## Tests
Contém os testes unitários e integração do projeto
