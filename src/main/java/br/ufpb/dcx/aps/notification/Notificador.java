package br.ufpb.dcx.aps.notification;

import java.util.ArrayList;
import java.util.List;

public class Notificador {

    private List<NotificacaoListener> listeners = new ArrayList<>();

    public void adicionarListener(NotificacaoListener listener) {
        listeners.add(listener);
    }

    public void notificarEvento(NotificacaoEvento evento) {
        for (NotificacaoListener listener : listeners) {
            listener.notificar(evento);
        }
    }
}