package br.ufpb.dcx.aps.notification;

import br.ufpb.dcx.aps.model.Notificacao;
import br.ufpb.dcx.aps.repository.NotificacaoRepository;

public class NotificacaoServiceListener implements NotificacaoListener {

    private NotificacaoRepository repository;

    public NotificacaoServiceListener(NotificacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void notificar(NotificacaoEvento evento) {

        Notificacao notificacao = new Notificacao(
                0,
                evento.getProfessorId(),
                evento.getMensagem()
        );

        repository.salvar(notificacao);
    }
}