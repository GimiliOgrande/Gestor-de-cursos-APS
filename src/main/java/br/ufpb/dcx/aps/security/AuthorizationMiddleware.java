package br.ufpb.dcx.aps.security;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.util.Arrays;

public class AuthorizationMiddleware {

    public static Handler requireAnyRole(CategoriaUsuario... rolesPermitidos) {

        return ctx -> {

            String token = ctx.cookie("token");

            if (token == null) {
                ctx.status(401).result("Token não encontrado");
                return;
            }

            try {
                var decoded = JwtUtil.validateToken(token);

                String categoria = decoded.getClaim("categoria").asString();

                boolean autorizado = Arrays.stream(rolesPermitidos)
                        .anyMatch(role -> role.name().equals(categoria));

                if (!autorizado) {
                    ctx.status(403).result("Acesso negado");
                    return;
                }

                // opcional: salvar id do usuário no contexto
                int usuarioId = decoded.getClaim("id").asInt();
                ctx.attribute("usuarioId", usuarioId);

            } catch (Exception e) {
                ctx.status(401).result("Token inválido");
            }
        };
    }
}