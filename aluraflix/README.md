<h1 align="center">Aluraflix</h1>

<div align="center">

[![Status](https://img.shields.io/badge/status-active-success.svg)]() <br><br> <!-- Depends if is published or not-->
[Português](#pt) / [English](#en) 
</div>

---
# Português <a name = "pt"></a>

## 📝 Conteúdos

- [Sobre](#about_pt)
- [Tecnologias](#built_using_pt)
- [Iniciando a aplicação](#getting_started_pt)
- [Estrutura do projeto](#project_structure_pt)
- [Utilização](#usage_pt)

## 🧐 Sobre <a name = "about_pt"></a>
Esse projeto back-end é uma API que permite que usuários possam criar Videos sobre os assuntos que desejar para uma plataforma ficticia de streaming. <br />
Ele foi construído com propósitos de estudo seguindo o modelo de desafio proposto pela Alura, que simula um ambiente profissional de trabalho, através do Alura Challenges. <br />
A aplicação foi construída utilizando Spring Boot, Java 17 e MySQL. <br />


## ⛏️ Tecnologias <a name = "built_using_pt"></a>
- [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot é uma ferramenta que facilita e agiliza o desenvolvimento de aplicativos da web e de microsserviços com o Spring Framework.
- [Java](https://www.oracle.com/java/technologies/downloads/archive/) - Linguagem de programação orientada a objetos.
- [MySQL](https://www.mysql.com) - Sistema de gerenciamento de banco de dados.


## 🏁 Iniciando a aplicação <a name = "getting_started_pt"></a>
Essas instruções vão te permitir obter uma cópia do projeto e rodar a aplicação localmente para propósitos de desenvolvimento e teste.

### Pre-requisitos
Para rodar a aplicação, você precisa ter o Java 17, Maven 3.6.3+ e MySQL instalado na sua máquina. Você pode baixar o Java 17 [aqui](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), Maven [aqui](https://maven.apache.org/docs/history.html) e o MySQL [aqui](https://dev.mysql.com/downloads/).


### Instalação
Para acessar o projeto, basta clonar o repositório ou realizar o download dos arquivos do projeto.<br>
Após clonar o repositório, acesse a pasta do projeto e siga primeiramente para a configuração do banco de dados no arquivo application.properties, inserindo o nome do database, usuário e senha.

Ao finalizar essa configuração, você poderá buildar o projeto utilizando Maven via linha de comando (Ou utilizando sua IDE):
```sh
mvn clean install
```

Depois de realizar o build do projeto com sucesso, use o seguinte comando para inicializar a API (Ou utilizando sua IDE):
```sh
mvn spring-boot:run
```

Com isso, a aplicação estará rodando em <code>http://localhost:8080</code>. As rotas estão mapeadas no arquivo "Aluraflix.postman_collection.json" e podem ser importadas para uso no Postman.

## 📁 Estrutura do projeto <a name = "project_structure_pt"></a>
A estrutura do projeto é a seguinte:
```
├── src: pasta com arquivos do projeto.
│   ├── main/java: pasta com arquivos java.
│   │   ├── config: pasta com arquivos para configurações para o Spring Boot.

│   │   ├── controller: pasta com arquivos responsáveis pelas RestControllers/Gerenciamento dos endpoints.

│   │   ├── dto: pasta com os arquivos de transferência de dados.

│   │   ├── entities: pasta com os arquivos de modelos de banco de dados.

│   │   ├── filters: pasta com os arquivos relacioandos a camadas de filtros do Spring.

│   │   ├── repositores: pasta com os arquivos de comunicação com banco de dados.

│   │   ├── service: pasta com os arquivos relacionados a regra de negócio.

│   │   ├── util: pasta com arquivos de utilidades do projeto.

│   ├── main/resources: pasta com arquivos de propriedades e migrations de banco.
```

## 🎈 Utilização <a name="usage_pt"></a>
Para usar a aplicação, basta clonar o projeto e rodar a aplicação localmente, seguindo as instruções de Instalação acima.

Vale ressaltar antes das serem executadas as chamadas que todas as rotas possuem autenticação, então durante o processo será necessário realizar a autorização antes de executar as chamadas.

O fluxo ideal seria realizar a criação de uma Categoria e após isso a criação de um Video, pois será necessário anexar o Video a uma Categoria.

---
# English <a name = "en"></a>

## 📝 Table of Contents <a name = "en"></a>
- [About](#about_en)
- [Technologies](#built_using_en)
- [Getting Started](#getting_started_en)
- [Project Structure](#project_structure_en)
- [Usage](#usage_en)

## 🧐 About <a name = "about_en"></a>
This back-end project is an API that allows users to create Videos on any subject they want for a fictional streaming platform. <br />
It was built with study purpose following the challenge model proposed by Alura, that simulates a professional work environment, through Alura Challenges. <br />
The application was built using Spring Boot, Java 17 and MySQL. <br />


## ⛏️ Technologies <a name = "built_using_en"></a>
- [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot is a tool that makes it easier and faster to develop web applications and microservices with the Spring Framework.
- [Java](https://www.oracle.com/java/technologies/downloads/archive/) - Object-oriented programming language.
- [MySQL](https://www.mysql.com) - Database management system.


## 🏁 Getting Started <a name = "getting_started_en"></a>
These instructions will allow you to get a copy of the project and run the application locally for development and testing purposes.

### Prerequisites
To run the application, you need to have Java 17, Maven 3.6.3+ and MySQL installed on your machine. You can download Java 17 [here](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), Maven [here](https://maven.apache.org/docs/history.html) and MySQL [here](https://dev.mysql.com/downloads/).

### Installation
To access the project, either clone the repository or download the project files.<br>
After cloning the repository, navigate to the project folder and follow to the first step to configure the api, you will need access the application.properties file and replace the properties for db_name, username, password to your own configuration.

After finish the first step, you can build the project using Maven through command line or using you IDE:
```sh
mvn clean install
```

After building the project with success, use the following command to start the application or use you IDE:
```sh
mvn spring-boot:run
```

This will run the application at <code>http://localhost:8080</code>. You can finde the API routes mapped on the "Aluraflix.postman_collection.json" file that can be imported on Postman.


## 📁 Project Structure <a name = "project_structure_en"></a>
The project structure is as follows:
```
├── src: folder with project files.
│   ├── main/java: folder with Java files.
│   │   ├── config: folder with Spring Boot configuration files.

│   │   ├── controller: folder with RestControllers files.

│   │   ├── dto: folder with data transfer files.

│   │   ├── entities: folder with database models.

│   │   ├── filters: folder with Spring filter layer.

│   │   ├── repositores: folder with database communication files.

│   │   ├── service: folder with business logic files.

│   │   ├── util: folder with project utilities files.

│   ├── main/resources: folder with database migrations and properties files.
```

## 🎈 Usage <a name="usage_en"></a>
To use the application, just clone the project and run the application locally, following the Installation instructions above.

It is worth mentioning before making calls that all routes have authentication, so during the process it will be needed to get authorized before making calls.
All the flows was prepared on postman collection, you will just need change the values and enjoy using this API.

The ideal flow would be to create a Category and then create a Video, because will be necessary to attach the Video to a Category.
