package br.ufpb.dcx.aps.model;

public class Notificacao {

    private int id;
    private int professorId;
    private String mensagem;

    public Notificacao(int id, int professorId, String mensagem) {
        this.id = id;
        this.professorId = professorId;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public String getMensagem() {
        return mensagem;
    }
}