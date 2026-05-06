package br.ufpb.dcx.aps.controller;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.security.AuthorizationMiddleware;
import br.ufpb.dcx.aps.security.JwtUtil;
import br.ufpb.dcx.aps.service.CursoService;
import io.javalin.Javalin;

public class CursoController {

    public static void register(Javalin app, CursoService cursoService, UsuarioRepository usuarioRepository
    ) {

        //LISTAR TODOS (qualquer logado)
        app.before("/cursos",
                AuthorizationMiddleware.requireAnyRole(
                        CategoriaUsuario.ADMIN,
                        CategoriaUsuario.PROFESSOR,
                        CategoriaUsuario.ALUNO
                )
        );

        app.get("/cursos", ctx -> {
            ctx.json(cursoService.listarCursos());
        });


        //BUSCAR POR ID (qualquer logado)
        app.before("/cursos/{id}",
                AuthorizationMiddleware.requireAnyRole(
                        CategoriaUsuario.ADMIN,
                        CategoriaUsuario.PROFESSOR,
                        CategoriaUsuario.ALUNO
                )
        );

        app.get("/cursos/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Curso curso = cursoService.buscarCurso(id);

            if (curso == null) {
                ctx.status(404).result("Curso não encontrado");
            } else {
                ctx.json(curso);
            }
        });


        //ROTAS SOMENTE ADMIN

        app.before("/cursos/admin/*",
                AuthorizationMiddleware.requireAnyRole(CategoriaUsuario.ADMIN)
        );

        //CRIAR
        app.post("/admin/cursos", ctx -> {

            String nome = ctx.formParam("nome");
            String descricao = ctx.formParam("descricao");
            int professorId = Integer.parseInt(ctx.formParam("professorId"));

            cursoService.cadastrarCurso(nome, descricao, professorId);

            ctx.redirect("/dashboard");
        });

        //ATUALIZAR
        app.put("/cursos/admin/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            String nome = ctx.formParam("nome");
            String descricao = ctx.formParam("descricao");
            int professorId = Integer.parseInt(ctx.formParam("professorId"));

            Curso curso = new Curso(id, nome, descricao, professorId);

            cursoService.atualizarCurso(curso);

            ctx.result("Curso atualizado com sucesso");
        });

        //DELETAR
        app.delete("/cursos/admin/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));

            cursoService.removerCurso(id);

            ctx.result("Curso removido com sucesso");
        });

        app.get("/admin/cursos", ctx -> {
            ctx.json(cursoService.listarCursos());
        });

        //ROTAS SOMENTE PROFESSOR

        app.get("/professor/cursos", ctx -> {

            String token = ctx.cookie("token");

            var decoded = JwtUtil.validateToken(token);
            int professorId = decoded.getClaim("id").asInt();

            var cursos = cursoService.buscarPorProfessor(professorId);

            ctx.json(cursos);
        });

        app.get("/admin/professores", ctx -> {

            var professores = usuarioRepository.buscarPorCategoria(CategoriaUsuario.PROFESSOR);

            ctx.json(professores);

        });

        app.get("/curso/{id}", ctx -> {

            String cursoId = ctx.pathParam("id");

            ctx.render("curso.html");

        });

    }

}
