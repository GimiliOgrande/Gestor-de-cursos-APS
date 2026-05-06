package br.ufpb.dcx.aps;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.CursoRepository;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.service.CursoService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CursoServiceTest {

    @Test
    void professorDeveCadastrarCurso(){

        UsuarioRepository usuarioRepo = new UsuarioRepository();
        CursoRepository cursoRepo = new CursoRepository();

        Usuario professor = new Usuario(1,"Professor","prof@email.com","123", CategoriaUsuario.PROFESSOR);
        usuarioRepo.salvar(professor);

        CursoService service = new CursoService(cursoRepo,usuarioRepo);

        service.cadastrarCurso("Java","Curso de Java",1);

        assertEquals(1,cursoRepo.listar().size());
    }

    @Test
    void alunoNaoPodeCadastrarCurso(){

        UsuarioRepository usuarioRepo = new UsuarioRepository();
        CursoRepository cursoRepo = new CursoRepository();

        Usuario aluno = new Usuario(1,"Aluno","aluno@email.com","123", CategoriaUsuario.ALUNO);
        usuarioRepo.salvar(aluno);

        CursoService service = new CursoService(cursoRepo,usuarioRepo);

        assertThrows(RuntimeException.class, () -> {
            service.cadastrarCurso("Java","Curso de Java",1);
        });
    }
}