package br.ufpb.dcx.aps.notification;

public class NotificacaoEvento {

    private int professorId;
    private int alunoId;
    private String mensagem;

    public NotificacaoEvento(int professorId, int alunoId, String mensagem) {
        this.professorId = professorId;
        this.alunoId = alunoId;
        this.mensagem = mensagem;
    }

    public int getProfessorId() {
        return professorId;
    }

    public int getAlunoId() {
        return alunoId;
    }

    public String getMensagem() {
        return mensagem;
    }
}