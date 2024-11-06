# Lab PCP - Gestão Educacional (Back-End)

## Descrição
O **Lab PCP - Gestão Educacional (Back-End)** é parte de um sistema web de gestão educacional desenvolvido como parte do projeto avaliativo do módulo 3.

Este sistema proporciona funcionalidades essenciais para a administração de instituições educacionais, permitindo o cadastro de turmas, conteúdos, docentes e alunos, além de oferecer recursos para alocação de professores, gerenciamento de notas e controle de acesso dos usuários.

Desenvolvido utilizando Java, Spring, API REST, PostgreSQL e Docker, o Educação FullStack emprega tecnologias modernas e práticas de desenvolvimento, incluindo Spring Security para controle de segurança, PostgreSQL para armazenamento de dados e GitHub para versionamento do código.

Além disso, a metodologia de desenvolvimento utiliza o Kanban no Trello para organização das tarefas, garantindo uma abordagem ágil e eficiente.

O sistema atende a uma variedade de papéis de usuários, como administradores, docentes e alunos, cada um com suas permissões específicas de acesso e funcionalidades disponíveis.

Com uma arquitetura sólida e uma implementação cuidadosa das regras de negócio, o **Lab PCP - Gestão Educacional (Back-End)** é uma solução completa para a gestão educacional, proporcionando uma experiência integrada e eficaz para todas as partes envolvidas no processo educativo.

Quando integrada à [parte Front-End](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3), forma um projeto Fullstack de Gestão Educacional.

---
## Problema que Resolve
Buscando facilitar o dia a dia da rotinas administrativas, o Lab PCP permite:  

- Gerenciar corpo docente: professores, recrutadores, corpo pedagógico; 
- Gerenciar cursos e suas disciplinas;
- Gerenciar alunos, suas matriculas, notas e pontuação geral. 

O Lab PCP gerencia e manipula dados integrando-os ao seu Banco de Dados.
---
## Tecnologias Utilizadas
- Java Spring Boot ([Link Spring initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.5&packaging=jar&jvmVersion=17&groupId=br.com.fullstackedu&artifactId=labpcp&name=labpcp&description=Demo%20project%20for%20Spring%20Boot&packageName=br.com.fullstackedu.labpcp&dependencies=data-jpa,oauth2-resource-server,security,web,lombok,devtools,postgresql,validation))
- PostgreSQL
- Git e Gitflow (para versionamento)
- Trello (para gerenciamento de projeto)
- Kanban (metodologia ágil de gestão de projetos)
---
## Pré-requisitos

- **[Git](https://git-scm.com/downloads)** para clonar o repositório
- **[Docker](https://www.docker.com/)** para rodar o projeto em imagens
- **[JDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21)** para rodar o código Java
- **[PostgreSQL](https://www.postgresql.org/download/)** para o banco de dados 
- **[APACHE Maven](https://maven.apache.org/download.cgi)** para compilação do projeto
---
# Como Baixar e Executar 
Para baixar o projeto do Git e executá-lo localmente, siga os passos abaixo:

## Executanto pelo IntelliJ IDEA
1. Clone o repositório do Git:
   ```bash
   git clone https://github.com/FullStack-Education/M3P-BackEnd-Squad3.git lab-pcp
   
2. Navegue até o diretório do projeto:   
    ```bash
    cd lab-pcp
3. Execute o subir os serviços do docker.  
   ```bash
   docker compose up -d
   
6. *(Opcional)* Rodar a [aplicação Front-End](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3) para visualizar o projeto. *[Veja mais](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3)*

### *Obs:* Garanta que as portas `8081` e `5432` estajam diponiveis no seu dispositivo


## Executanto pelo Docker compose
1. Clone o repositório do Git:
   ```bash
   git clone https://github.com/FullStack-Education/M3P-BackEnd-Squad3.git lab-pcp
2. Navegue até o diretório do projeto:
    ```bash
    cd lab-pcp
3. Execute o subir serviço de banco de dados.
   ```bash
   docker compose up -d labpcp-db

6. *(Opcional)* Rodar a [aplicação Front-End](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3) para visualizar o projeto. *[Veja mais](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3)*

### *Obs:* Garanta que as portas `8081` e `5432` estajam diponiveis no seu dispositivo


## Docementação visual do Swagger

1. Excute o tutorial do IntelliJ IDEA para subir a aplicação

2.  Acesso a url [swagger-ui](http://localhost:8081/swagger-ui.html)


## Dados Predefinidos

- ADMIN
  - Login: admin@mail.com
  - Senha: admin@mail.com
  
- PEDAGOGICO
  - Login: pedagogico@mail.com
  - Senha: pedagogico@mail.com

- RECRUITER (Recrutador)
  - Login: recruiter@mail.com
  - Senha: recruiter@mail.com

- PROFESSOR
  - Login: professor@mail.com
  - Senha: professor@mail.com

- ALUNO
  - Login: aluno@mail.com
  - Senha: aluno@mail.com
---

# Documentação de Endpoints da API

## Aluno - CRUD

### **GET /alunos**
- **Descrição**: Retorna todos os alunos cadastrados.
- **Resposta**:
    - **200 OK**: Retorna uma lista de alunos.
        - `alunoData` (array de `AlunoEntity`):
            - `id`: ID do aluno
            - `nome`: Nome do aluno
            - `dataNascimento`: Data de nascimento
            - `telefone`: Telefone
            - `genero`, `estadoCivil`, `email`, `cpf`, `rg`, `cidade`, etc.

### **GET /alunos/{alunoId}**
- **Descrição**: Retorna um aluno pelo seu ID.
- **Parâmetros**:
    - `alunoId` (path): ID do aluno (inteiro, obrigatório)
- **Resposta**:
    - **200 OK**: Retorna os dados do aluno com o ID fornecido.
        - Campos: iguais ao objeto `AlunoEntity` acima.
    - **400 Bad Request**: Erro de requisição inválida.

### **POST /alunos**
- **Descrição**: Cria um novo aluno.
- **Corpo da Requisição** (`AlunoRequest`):
    - `nome`: Nome do aluno
    - `data_nascimento`: Data de nascimento (formato `date`)
    - `telefone`, `genero`, `estadoCivil`, `email`, `cpf`, `rg`, etc.
- **Resposta**:
    - **201 Created**: Aluno criado com sucesso.
    - **400 Bad Request**: Requisição inválida ou dados incompletos.

### **PUT /alunos/{alunoId}**
- **Descrição**: Atualiza um aluno pelo ID.
- **Parâmetros**:
    - `alunoId` (path): ID do aluno (obrigatório)
- **Corpo da Requisição** (`AlunoUpdateRequest`):
    - Campos semelhantes ao `AlunoRequest` para dados que serão atualizados.
- **Resposta**:
    - **200 OK**: Aluno atualizado.
    - **400 Bad Request**: Requisição inválida.

### **DELETE /alunos/{alunoId}**
- **Descrição**: Exclui um aluno pelo ID.
- **Parâmetros**:
    - `alunoId` (path): ID do aluno
- **Resposta**:
    - **204 No Content**: Aluno excluído.
    - **400 Bad Request**: Requisição inválida.

---

## Docente - CRUD

### **GET /docentes**
- **Descrição**: Retorna todos os docentes cadastrados.
- **Resposta**:
    - **200 OK**: Lista de docentes.
        - `docenteData` (array de `DocenteEntity`):
            - `id`: ID do docente
            - `nome`, `dataEntrada`, `telefone`, `email`, `cpf`, etc.

### **GET /docentes/{docenteId}**
- **Descrição**: Retorna um docente pelo ID.
- **Parâmetros**:
    - `docenteId` (path): ID do docente
- **Resposta**:
    - **200 OK**: Dados do docente.
    - **400 Bad Request**: Requisição inválida.

### **POST /docentes**
- **Descrição**: Cadastra um novo docente.
- **Corpo da Requisição** (`DocenteCreateRequest`):
    - `nome`, `dataNascimento`, `telefone`, `genero`, `cpf`, etc.
- **Resposta**:
    - **201 Created**: Docente criado.
    - **400 Bad Request**: Requisição inválida.

### **PUT /docentes/{docenteId}**
- **Descrição**: Atualiza dados de um docente.
- **Corpo da Requisição** (`DocenteUpdateRequest`): Campos a serem atualizados.
- **Resposta**:
    - **200 OK**: Docente atualizado.
    - **400 Bad Request**: Requisição inválida.

---

## Curso - CRUD

### **GET /cursos**
- **Descrição**: Retorna todos os cursos cadastrados.
- **Resposta**:
    - **200 OK**: Lista de cursos.
        - `cursoData` (array de `CursoEntity`): com `id`, `nome`, etc.

### **GET /cursos/{cursoId}**
- **Descrição**: Retorna um curso específico.
- **Parâmetros**:
    - `cursoId` (path): ID do curso.
- **Resposta**:
    - **200 OK**: Dados do curso.
    - **400 Bad Request**: Requisição inválida.

### **POST /cursos**
- **Descrição**: Cadastra um novo curso.
- **Corpo da Requisição** (`CursoRequest`):
    - `nome`: Nome do curso.
- **Resposta**:
    - **201 Created**: Curso cadastrado.
    - **400 Bad Request**: Requisição inválida.

### **PUT /cursos/{cursoId}**
- **Descrição**: Atualiza um curso específico.
- **Corpo da Requisição** (`CursoRequest`): Campos atualizados.
- **Resposta**:
    - **200 OK**: Curso atualizado.
    - **400 Bad Request**: Requisição inválida.

---

## Turma - CRUD

### **GET /turmas**
- **Descrição**: Retorna todas as turmas cadastradas.
- **Resposta**:
    - **200 OK**: Lista de turmas.
        - `turmaData` (array de `TurmaEntity`): com `id`, `nome`, `dataInicio`, `dataFim`, `hora`, etc.

### **GET /turmas/{turmaId}**
- **Descrição**: Retorna dados de uma turma.
- **Parâmetros**:
    - `turmaId` (path): ID da turma.
- **Resposta**:
    - **200 OK**: Dados da turma.
    - **400 Bad Request**: Requisição inválida.

### **POST /turmas**
- **Descrição**: Cadastra uma nova turma.
- **Corpo da Requisição** (`TurmaCreateRequest`):
    - `nome`, `id_curso`, `dataInicio`, `dataFim`, `hora`, etc.
- **Resposta**:
    - **201 Created**: Turma cadastrada.
    - **400 Bad Request**: Requisição inválida.

### **PUT /turmas/{turmaId}**
- **Descrição**: Atualiza uma turma.
- **Corpo da Requisição** (`TurmaUpdateRequest`): Dados a serem atualizados.
- **Resposta**:
    - **200 OK**: Turma atualizada.
    - **400 Bad Request**: Requisição inválida.

### **DELETE /turmas/{turmaId}**
- **Descrição**: Exclui uma turma.
- **Parâmetros**:
    - `turmaId` (path): ID da turma.
- **Resposta**:
    - **204 No Content**: Turma excluída.
    - **400 Bad Request**: Requisição inválida.

---

## Como Contribuir

Agradecemos o seu interesse em contribuir para o projeto :) 
Para participar do desenvolvimento e melhora-lo o projeto, siga estas diretrizes:

### Guia de Estilo de Código

- Mantenha um estilo de código consistente com as convenções do Java.
- Utilize boas práticas de programação, como nomenclatura descritiva.
- Evite códigos duplicados quando possivel.

### Padrões de Commit (GitFlow)

Este projeto segue o modelo de branch GitFlow para gerenciamento de branches e versionamento. Para contribuir:

1. Crie uma nova branch a partir da branch `develop` para desenvolver novos recursos ou correções:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/nome-da-feature

2. Faça commits na sua branch e envie as alterações para o repositório remoto:
    ```bash
    git add .
    git commit -m "Implementação da feature X"
    git push origin feature/nome-da-feature
 
3. Abra um Pull Request (PR) para mesclar suas alterações na branch develop após a revisão e aprovação:
- Descreva claramente o propósito das suas alterações no PR.
- Mantenha um histórico de commits limpo e organizado.

## Possíveis Melhorias
Algumas melhorias que podem ser implementadas no projeto incluem:

- Implementação de classes genéricas para redução de código repetitivo
- Novas funcionalidades para a administração de uma instituição educacional.