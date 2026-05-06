package br.ufpb.dcx.aps.repository;

import br.ufpb.dcx.aps.config.DatabaseConnection;
import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Matricula;
import br.ufpb.dcx.aps.model.Usuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatriculaRepository {

    public void matricular(int alunoId, int cursoId) {

        String sql = "INSERT INTO matricula (aluno_id, curso_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean jaMatriculado(int alunoId, int cursoId) {

        String sql = "SELECT 1 FROM matricula WHERE aluno_id = ? AND curso_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setInt(2, cursoId);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Matricula> listarPorAluno(int alunoId) {

        String sql = "SELECT * FROM matricula WHERE aluno_id = ?";
        List<Matricula> matriculas = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                int cursoId = rs.getInt("curso_id");
                Timestamp timestamp = rs.getTimestamp("data_matricula");

                LocalDateTime data = timestamp != null
                        ? timestamp.toLocalDateTime()
                        : null;

                matriculas.add(new Matricula(id, alunoId, cursoId, data));
            }

            return matriculas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removerMatricula(int alunoId, int cursoId) {

        String sql = "DELETE FROM matricula WHERE aluno_id = ? AND curso_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);
            stmt.setInt(2, cursoId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> buscarAlunosPorCurso(int cursoId) {

        List<Usuario> alunos = new ArrayList<>();

        String sql = """
        SELECT u.id, u.nome, u.email, u.categoria
        FROM matricula m
        JOIN usuario u ON m.aluno_id = u.id
        WHERE m.curso_id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cursoId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                alunos.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        "",
                        CategoriaUsuario.valueOf(rs.getString("categoria"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 🔥 IMPORTANTE PRA DEBUG
            throw new RuntimeException(e);
        }

        return alunos;
    }

    public List<Curso> buscarCursosPorAluno(int alunoId) {

        String sql = """
        SELECT c.*
        FROM matricula m
        JOIN curso c ON m.curso_id = c.id
        WHERE m.aluno_id = ?
    """;

        List<Curso> cursos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, alunoId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int professorId = rs.getInt("professor_id");

                Curso curso = new Curso(id, nome, descricao, professorId);

                cursos.add(curso);
            }

            return cursos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}