package br.ufpb.dcx.aps.repository;

import br.ufpb.dcx.aps.config.DatabaseConnection;
import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CursoRepository {
    public void salvar(Curso curso) {

        String sql = "INSERT INTO curso (nome, descricao, professor_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getProfessorId());

            stmt.executeUpdate();

            System.out.println("Curso salvo com sucesso!");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Curso> listar(){
        String sql = "SELECT * FROM curso";
        List<Curso> cursos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public Curso buscarPorNome(String nome){
        String sql = "SELECT * FROM curso WHERE nome = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nomeBanco = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int professorId = rs.getInt("professor_id");


                return new Curso(id, nomeBanco, descricao, professorId);
            } else   {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public void deletar(int id){
        String sql = "DELETE FROM curso WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                System.out.println("Nenhum curso encontrado com esse id.");
            } else {
                System.out.println("Curso deletado com sucesso.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Curso curso){
        String sql = "UPDATE curso SET nome=?, descricao=?, professor_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, curso.getNome());
            stmt.setString(2, curso.getDescricao());
            stmt.setInt(3, curso.getProfessorId());
            stmt.setInt(4, curso.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                System.out.println("Nenhum curso encontrado para atualizar.");
            } else {
                System.out.println("Curso atualizado com sucesso.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Curso buscarPorId(int id) {

        String sql = "SELECT * FROM curso WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                int idBanco = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                int professorId = rs.getInt("professor_id");

                return new Curso(idBanco, nome, descricao, professorId);

            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Curso> buscarPorProfessor(int professorId) {

        List<Curso> cursos = new ArrayList<>();

        String sql = "SELECT * FROM curso WHERE professor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, professorId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cursos.add(new Curso(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getInt("professor_id")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cursos;
    }
}
