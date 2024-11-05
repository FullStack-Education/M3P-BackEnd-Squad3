# Lab PCP - Gestão Educacional (Back-End)

## Descrição
O **Lab PCP - Gestão Educacional (Back-End)** é uma JAVA Rest API para gerenciar uma instituição educacional, como uma escola ou laboratório de informática.
Quando integrada à [parte Front-End](https://github.com/FullStack-Education/M3P-FrontEnd-Squad3), forma um projeto Fullstack de Gestão Educacional.

## Problema que Resolve
Buscando facilitar o dia a dia da rotinas administrativas, o Lab PCP permite:  

- Gerenciar corpo docente: professores, recrutadores, corpo pedagógico; 
- Gerenciar cursos e suas disciplinas;
- Gerenciar alunos, suas matriculas, notas e pontuação geral. 

O Lab PCP gerencia e manipula dados integrando-os ao seu Banco de Dados.

## Tecnologias Utilizadas
- Java Spring Boot ([Link Spring initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.5&packaging=jar&jvmVersion=17&groupId=br.com.fullstackedu&artifactId=labpcp&name=labpcp&description=Demo%20project%20for%20Spring%20Boot&packageName=br.com.fullstackedu.labpcp&dependencies=data-jpa,oauth2-resource-server,security,web,lombok,devtools,postgresql,validation))
- PostgreSQL
- Git e Gitflow (para versionamento)
- Trello (para gerenciamento de projeto)
- Kanban (metodologia ágil de gestão de projetos)

## Pré-requisitos

- **[Git](https://git-scm.com/downloads)** para clonar o repositório
- **[Docker](https://www.docker.com/)** para rodar o projeto em imagens
- **[JDK 21](https://www.oracle.com/br/java/technologies/downloads/#java21)** para rodar o código Java
- **[PostgreSQL](https://www.postgresql.org/download/)** para o banco de dados 
- **[APACHE Maven](https://maven.apache.org/download.cgi)** para compilação do projeto

## Como Baixar e Executar
Para baixar o projeto do Git e executá-lo localmente, siga os passos abaixo:

1. Clone o repositório do Git:
   ```bash
   git clone https://github.com/FullStack-Education/M3P-BackEnd-Squad3.git lab-pcp
2. Navegue até o diretório do projeto:   
    ```bash
    cd lab-pcp
3. Configure o banco de dados em docker e inicie-o.  
   Utilize o usuário `postgres` e a senha `123456` para o banco de dados.

4. Execute a aplicação usando Maven (certifique-se de ter o Maven instalado):
    ```bash
    mvn spring-boot:run
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

- Implementação de testes automatizados para garantir a qualidade do código.
- Implementação de classes genéricas para redução de código repetitivo
- Novas funcionalidades para a administração de uma instituição educacional.