package br.ufpb.dcx.aps;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    void deveRealizarLoginComCredenciaisValidas() {

        UsuarioRepository repo = new UsuarioRepository();

        Usuario usuario = new Usuario(1,"Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);
        repo.salvar(usuario);

        AuthService authService = new AuthService(repo);

        String token = authService.login("ramon@email.com","123");

        assertNotNull(token);
    }

    @Test
    void naoDeveLogarComSenhaErrada(){

        UsuarioRepository repo = new UsuarioRepository();

        Usuario usuario = new Usuario(1,"Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);
        repo.salvar(usuario);

        AuthService authService = new AuthService(repo);

        String token = authService.login("ramon@email.com","999");

        assertNull(token);
    }
}