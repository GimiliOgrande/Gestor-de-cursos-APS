package br.ufpb.dcx.aps.repository;

import br.ufpb.dcx.aps.config.DatabaseConnection;
import br.ufpb.dcx.aps.model.Notificacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoRepository {

    public void salvar(Notificacao notificacao) {

        String sql = "INSERT INTO notificacao (professor_id, mensagem) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, notificacao.getProfessorId());
            stmt.setString(2, notificacao.getMensagem());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Notificacao> buscarPorProfessor(int professorId) {

        List<Notificacao> notificacoes = new ArrayList<>();

        String sql = "SELECT * FROM notificacao WHERE professor_id = ? ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Notificacao notificacao = new Notificacao(
                        rs.getInt("id"),
                        rs.getInt("professor_id"),
                        rs.getString("mensagem")
                );

                notificacoes.add(notificacao);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notificacoes;
    }

    public List<Notificacao> buscarNaoLidas(int professorId) {

        List<Notificacao> notificacoes = new ArrayList<>();

        String sql = "SELECT * FROM notificacao WHERE professor_id = ? AND lida = FALSE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Notificacao notificacao = new Notificacao(
                        rs.getInt("id"),
                        rs.getInt("professor_id"),
                        rs.getString("mensagem")
                );

                notificacoes.add(notificacao);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notificacoes;
    }

    public void marcarComoLidas(int professorId) {

        String sql = "UPDATE notificacao SET lida = TRUE WHERE professor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}