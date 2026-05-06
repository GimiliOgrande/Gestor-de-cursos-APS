package br.ufpb.dcx.aps.service;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository repository;

    //Construtor que recebe o repository
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(String nome, String email, String senha, CategoriaUsuario categoria) {

        // Verificar se já existe usuário com o mesmo email
        Usuario existente = repository.buscarPorEmail(email);
        if (existente != null) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(0, nome, email, senha, categoria);
        repository.salvar(usuario);
    }
}