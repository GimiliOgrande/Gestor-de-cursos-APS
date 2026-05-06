## Sistema de Gestão de Cursos e Treinamentos
## Descrição

Este projeto consiste em um Sistema de Gestão de Cursos e Treinamentos, desenvolvido para gerenciar cursos, usuários, matrículas e notificações em uma plataforma educacional.

A aplicação foi desenvolvida utilizando arquitetura em camadas (Controller, Service, Repository e Model), com o objetivo de manter o código organizado, modular e de fácil manutenção.

O sistema também implementa um sistema de notificações baseado no padrão de projeto Observer, permitindo que eventos importantes do sistema gerem notificações automaticamente para os usuários.

Este projeto foi desenvolvido para a disciplina de Análise e Projeto de Sistemas (APS).

## Funcionalidades
Cadastro e gerenciamento de cursos
Matrícula de alunos em cursos
Controle de usuários (Administrador, Professor e Aluno)
Visualização de cursos matriculados
Sistema de notificações baseado em eventos
Autenticação de usuários com JWT
Tecnologias Utilizadas

## Backend

Java
Javalin
JDBC
JWT

## Frontend

HTML
CSS
JavaScript

## Banco de dados

Banco de dados relacional (SQL)
Padrão de Projeto Utilizado

## O sistema de notificações foi implementado utilizando o padrão Observer.

Quando um aluno realiza matrícula em um curso:

Um evento é criado no sistema
O Notificador dispara esse evento
Os listeners registrados recebem a notificação
Uma notificação é salva e exibida para o professor responsável pelo curso

Essa abordagem permite desacoplar o sistema de notificações da lógica principal da aplicação.

## Estrutura do Projeto
controller/
service/
model/
repository/
notification/

## Como Executar o Projeto:
Abra o projeto na sua IDE (ex: IntelliJ).
Execute a classe App.
Acesse o sistema pelo navegador no endereço:
http://localhost:7000/

## Vídeo do projeto: 
https://youtu.be/s9rrOCLnqUU