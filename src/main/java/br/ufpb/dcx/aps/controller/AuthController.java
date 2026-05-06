package br.ufpb.dcx.aps.controller;

import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.security.JwtUtil;
import br.ufpb.dcx.aps.service.AuthService;
import io.javalin.Javalin;

public class AuthController {

    public static void register(Javalin app, AuthService authService, UsuarioRepository usuarioRepository) {

        app.post("/login", ctx -> {

            String email = ctx.formParam("email");
            String senha = ctx.formParam("senha");

            try {
                String token = authService.login(email, senha);

                if (token == null) {
                    ctx.status(401).result("Email ou senha inválidos");
                    return;
                }

                //salva token no cookie
                ctx.cookie("token", token);

                //redireciona pro dashboard
                ctx.redirect("/dashboard");

            } catch (Exception e) {
                ctx.status(500).result("Erro ao fazer login");
                e.printStackTrace();
            }
        });
        app.get("/usuario-logado", ctx -> {

            String token = ctx.cookie("token");

            if(token == null){
                ctx.status(401);
                return;
            }

            var decoded = JwtUtil.validateToken(token);

            int userId = decoded.getClaim("id").asInt();

            Usuario usuario = usuarioRepository.buscarPorId(userId);

            ctx.json(usuario);

        });
    }
}