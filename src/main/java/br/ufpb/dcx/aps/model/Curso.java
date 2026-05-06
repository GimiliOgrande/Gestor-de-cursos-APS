package br.ufpb.dcx.aps.model;

import java.util.List;

public class Curso {
    private int id;
    private String nome;
    private String descricao;
    private int professorId;

    public Curso(int id, String nome, String descricao, int professorId) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.professorId = professorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessor(int professorId) {
        this.professorId = professorId;
    }

}
