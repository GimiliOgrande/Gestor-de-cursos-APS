package br.ufpb.dcx.aps;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.service.UsuarioService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioServiceTest {

    @Test
    void deveCadastrarUsuario(){

        UsuarioRepository repo = new UsuarioRepository();
        UsuarioService service = new UsuarioService(repo);

        service.cadastrar("Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);

        assertNotNull(repo.buscarPorEmail("ramon@email.com"));
    }

    @Test
    void naoDevePermitirEmailDuplicado(){

        UsuarioRepository repo = new UsuarioRepository();
        UsuarioService service = new UsuarioService(repo);

        service.cadastrar("Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);

        assertThrows(RuntimeException.class, () -> {
            service.cadastrar("Outro","ramon@email.com","123", CategoriaUsuario.ALUNO);
        });
    }
}