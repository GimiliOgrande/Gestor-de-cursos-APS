package br.ufpb.dcx.aps;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.notification.Notificador;
import br.ufpb.dcx.aps.repository.CursoRepository;
import br.ufpb.dcx.aps.repository.MatriculaRepository;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.service.MatriculaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatriculaServiceTest {

    @Test
    void alunoDeveSerMatriculadoNoCurso(){

        UsuarioRepository usuarioRepo = new UsuarioRepository();
        CursoRepository cursoRepo = new CursoRepository();
        MatriculaRepository matriculaRepo = new MatriculaRepository();
        Notificador notificador = new Notificador();

        Usuario aluno = new Usuario(1,"Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);
        usuarioRepo.salvar(aluno);

        Usuario professor = new Usuario(2,"Prof","prof@email.com","123", CategoriaUsuario.PROFESSOR);
        usuarioRepo.salvar(professor);

        Curso curso = new Curso(1,"Java","Curso Java",2);
        cursoRepo.salvar(curso);

        MatriculaService service = new MatriculaService(
                matriculaRepo,
                usuarioRepo,
                cursoRepo,
                notificador
        );

        service.matricularAluno(1,1);

        assertTrue(matriculaRepo.jaMatriculado(1,1));
    }

    @Test
    void naoDevePermitirMatriculaDuplicada(){

        UsuarioRepository usuarioRepo = new UsuarioRepository();
        CursoRepository cursoRepo = new CursoRepository();
        MatriculaRepository matriculaRepo = new MatriculaRepository();
        Notificador notificador = new Notificador();

        Usuario aluno = new Usuario(1,"Ramon","ramon@email.com","123", CategoriaUsuario.ALUNO);
        usuarioRepo.salvar(aluno);

        Usuario professor = new Usuario(2,"Prof","prof@email.com","123", CategoriaUsuario.PROFESSOR);
        usuarioRepo.salvar(professor);

        Curso curso = new Curso(1,"Java","Curso Java",2);
        cursoRepo.salvar(curso);

        MatriculaService service = new MatriculaService(
                matriculaRepo,
                usuarioRepo,
                cursoRepo,
                notificador
        );

        service.matricularAluno(1,1);

        assertThrows(RuntimeException.class, () -> {
            service.matricularAluno(1,1);
        });
    }
}