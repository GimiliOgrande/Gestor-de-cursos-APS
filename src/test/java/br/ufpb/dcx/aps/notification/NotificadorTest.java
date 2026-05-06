package br.ufpb.dcx.aps.notification;

import br.ufpb.dcx.aps.model.Notificacao;
import br.ufpb.dcx.aps.repository.NotificacaoRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificadorTest {

    @Test
    void deveCriarNotificacaoQuandoEventoForDisparado() {

        NotificacaoRepository repository = new NotificacaoRepository();

        NotificacaoServiceListener listener =
                new NotificacaoServiceListener(repository);

        Notificador notificador = new Notificador();

        // registra o listener
        notificador.adicionarListener(listener);

        int professorId = 1;

        NotificacaoEvento evento = new NotificacaoEvento(
                professorId,
                2,
                "Aluno Ramon se matriculou no curso Java"
        );

        // dispara o evento
        notificador.notificarEvento(evento);

        // busca notificações do professor
        List<Notificacao> notificacoes =
                repository.buscarPorProfessor(professorId);

        assertFalse(notificacoes.isEmpty());

        Notificacao notificacao = notificacoes.get(0);

        assertEquals(professorId, notificacao.getProfessorId());
        assertEquals(
                "Aluno Ramon se matriculou no curso Java",
                notificacao.getMensagem()
        );
    }
}