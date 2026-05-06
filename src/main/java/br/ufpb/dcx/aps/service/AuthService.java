package br.ufpb.dcx.aps.service;

import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.security.JwtUtil;

public class AuthService {

    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String login(String email, String senha){

        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            return JwtUtil.generateToken(usuario);
        }

        return null;
    }

}