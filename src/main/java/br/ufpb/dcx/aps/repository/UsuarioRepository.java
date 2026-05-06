package br.ufpb.dcx.aps.repository;

import br.ufpb.dcx.aps.config.DatabaseConnection;
import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    public void salvar(Usuario usuario) {

        String sql = "INSERT INTO usuario (nome, email, senha, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            //categoria do enum transformada em string:
            stmt.setString(4, usuario.getCategoria().name());

            stmt.executeUpdate();

            System.out.println("Usuário salvo com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                String categoriaStr = rs.getString("categoria");
                String emailBanco = rs.getString("email");

                CategoriaUsuario categoria = CategoriaUsuario.valueOf(categoriaStr);

                return new Usuario(id, nome, emailBanco, senha, categoria);
            } else  {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idBanco = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String categoriaStr = rs.getString("categoria");

                CategoriaUsuario categoria = CategoriaUsuario.valueOf(categoriaStr);

                return new Usuario(idBanco, nome, email, senha, categoria);
            } else   {
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String categoriaStr = rs.getString("categoria");

                CategoriaUsuario categoria = CategoriaUsuario.valueOf(categoriaStr);

                Usuario usuario = new Usuario(id, nome, email, senha, categoria);

                usuarios.add(usuario);
            }

            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(int id){
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            //ao invés de colocar apenas o stmt.executeUpdate(), isso verifica se foi mesmo deletado:
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas == 0) {
                System.out.println("Nenhum usuário encontrado com esse id.");
            } else {
                System.out.println("Usuário deletado com sucesso.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Usuario usuario){
        String sql = "UPDATE usuario SET nome=?, email=?, senha=?, categoria=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getCategoria().name());
            stmt.setInt(5, usuario.getId());

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                System.out.println("Nenhum usuário encontrado para atualizar.");
            } else {
                System.out.println("Usuário atualizado com sucesso.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Usuario> buscarPorCategoria(CategoriaUsuario categoria) {

        String sql = "SELECT * FROM usuario WHERE categoria = ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria.name());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String senha = rs.getString("senha");
                String categoriaStr = rs.getString("categoria");

                CategoriaUsuario categoriaUsuario = CategoriaUsuario.valueOf(categoriaStr);

                Usuario usuario = new Usuario(id, nome, email, senha, categoriaUsuario);

                usuarios.add(usuario);
            }

            return usuarios;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
