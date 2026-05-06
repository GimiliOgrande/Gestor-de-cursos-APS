package br.ufpb.dcx.aps.service;

import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.CursoRepository;
import br.ufpb.dcx.aps.repository.UsuarioRepository;

import java.util.List;

public class CursoService {

    private CursoRepository cursoRepository;
    private UsuarioRepository usuarioRepository;

    public CursoService(CursoRepository cursoRepository, UsuarioRepository usuarioRepository) {
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public void cadastrarCurso(String nome, String descricao, int professorId) {

        Usuario professor = usuarioRepository.buscarPorId(professorId);

        if (professor == null) {
            throw new RuntimeException("Professor não encontrado");
        }

        if (!professor.getCategoria().name().equals("PROFESSOR")) {
            throw new RuntimeException("Usuário informado não é um professor");
        }

        Curso curso = new Curso(0, nome, descricao, professorId);

        cursoRepository.salvar(curso);
    }

    public List<Curso> listarCursos(){
        return cursoRepository.listar();
    }

    public Curso buscarCursoNome(String nome){
        return cursoRepository.buscarPorNome(nome);
    }

    public Curso buscarCurso(int id){
        return cursoRepository.buscarPorId(id);
    }

    public void atualizarCurso(Curso curso){
        cursoRepository.atualizar(curso);
    }

    public void removerCurso(int id){
        cursoRepository.deletar(id);
    }

    public List<Curso> buscarPorProfessor(int professorId) {
        return cursoRepository.buscarPorProfessor(professorId);
    }

}
