package br.ufpb.dcx.aps.model;

import java.time.LocalDateTime;

public class Matricula {

    private int id;
    private int alunoId;
    private int cursoId;
    private LocalDateTime dataMatricula;

    public Matricula(int id, int alunoId, int cursoId, LocalDateTime dataMatricula) {
        this.id = id;
        this.alunoId = alunoId;
        this.cursoId = cursoId;
        this.dataMatricula = dataMatricula;
    }

    public Matricula(int alunoId, int cursoId) {
        this.alunoId = alunoId;
        this.cursoId = cursoId;
    }

    public int getId() {
        return id;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public int getCursoId() {
        return cursoId;
    }

    public LocalDateTime getDataMatricula() {
        return dataMatricula;
    }
}