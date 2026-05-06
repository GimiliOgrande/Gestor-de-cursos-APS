package br.ufpb.dcx.aps.service;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.notification.NotificacaoEvento;
import br.ufpb.dcx.aps.notification.Notificador;
import br.ufpb.dcx.aps.repository.CursoRepository;
import br.ufpb.dcx.aps.repository.MatriculaRepository;
import br.ufpb.dcx.aps.repository.UsuarioRepository;

import java.util.List;

public class MatriculaService {

    private MatriculaRepository matriculaRepository;
    private UsuarioRepository usuarioRepository;
    private CursoRepository cursoRepository;
    private Notificador notificador;

    public MatriculaService(MatriculaRepository matriculaRepository,
                            UsuarioRepository usuarioRepository,
                            CursoRepository cursoRepository,
                            Notificador notificador) {

        this.matriculaRepository = matriculaRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
        this.notificador = notificador;
    }

    public void matricularAluno(int alunoId, int cursoId) {

        Usuario aluno = usuarioRepository.buscarPorId(alunoId);

        if (aluno == null)
            throw new RuntimeException("Aluno não encontrado");

        if (aluno.getCategoria() != CategoriaUsuario.ALUNO)
            throw new RuntimeException("Usuário não é um aluno");

        Curso curso = cursoRepository.buscarPorId(cursoId);

        if (curso == null)
            throw new RuntimeException("Curso não encontrado");

        if (matriculaRepository.jaMatriculado(alunoId, cursoId))
            throw new RuntimeException("Aluno já está matriculado neste curso");

        matriculaRepository.matricular(alunoId, cursoId);

        Usuario alunoUsuario = usuarioRepository.buscarPorId(alunoId);

        String mensagem = alunoUsuario.getNome() +
                " se matriculou no curso " + curso.getNome();

        NotificacaoEvento evento = new NotificacaoEvento(
                curso.getProfessorId(),
                alunoId,
                mensagem
        );

        notificador.notificarEvento(evento);
    }

    public void cancelarMatricula(int alunoId, int cursoId) {

        if (!matriculaRepository.jaMatriculado(alunoId, cursoId))
            throw new RuntimeException("Matrícula não encontrada");

        matriculaRepository.removerMatricula(alunoId, cursoId);
    }

    public List<Usuario> buscarAlunosPorCurso(int cursoId) {
        return matriculaRepository.buscarAlunosPorCurso(cursoId);
    }

    public List<Curso> buscarCursosPorAluno(int alunoId){
        return matriculaRepository.buscarCursosPorAluno(alunoId);
    }
}