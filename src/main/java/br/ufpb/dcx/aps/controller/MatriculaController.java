package br.ufpb.dcx.aps.controller;

import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.repository.NotificacaoRepository;
import br.ufpb.dcx.aps.security.AuthorizationMiddleware;
import br.ufpb.dcx.aps.security.JwtUtil;
import br.ufpb.dcx.aps.service.MatriculaService;
import io.javalin.Javalin;

public class MatriculaController {

    public static void register(Javalin app,
                                MatriculaService matriculaService,
                                NotificacaoRepository notificacaoRepository) {

        //Apenas ALUNO pode se matricular
        app.before("/aluno/matricular",
                AuthorizationMiddleware.requireAnyRole(
                        CategoriaUsuario.ALUNO
                )
        );

        app.post("/aluno/matricular", ctx -> {

            String token = ctx.cookie("token");
            var decoded = JwtUtil.validateToken(token);

            int alunoId = decoded.getClaim("id").asInt();
            int cursoId = Integer.parseInt(ctx.formParam("cursoId"));

            matriculaService.matricularAluno(alunoId, cursoId);

            ctx.result("Matrícula realizada com sucesso");
        });


        //Cancelar matrícula
        app.post("/aluno/cancelar-matricula", ctx -> {

            int alunoId = Integer.parseInt(ctx.formParam("alunoId"));
            int cursoId = Integer.parseInt(ctx.formParam("cursoId"));

            matriculaService.cancelarMatricula(alunoId, cursoId);

            ctx.result("Matrícula cancelada com sucesso");
        });


        //Professor ver alunos matriculados em um curso
        app.get("/professor/cursos/{cursoId}/alunos", ctx -> {

            int cursoId = Integer.parseInt(ctx.pathParam("cursoId"));

            var alunos = matriculaService.buscarAlunosPorCurso(cursoId);

            ctx.json(alunos);
        });


        //NOTIFICAÇÕES DO PROFESSOR
        app.get("/professor/notificacoes", ctx -> {

            String token = ctx.cookie("token");
            var decoded = JwtUtil.validateToken(token);

            int professorId = decoded.getClaim("id").asInt();

            var notificacoes = notificacaoRepository.buscarPorProfessor(professorId);

            ctx.json(notificacoes);
        });

        app.post("/professor/notificacoes/marcar-lidas", ctx -> {

            String token = ctx.cookie("token");
            var decoded = JwtUtil.validateToken(token);

            int professorId = decoded.getClaim("id").asInt();

            notificacaoRepository.marcarComoLidas(professorId);

            ctx.status(200);
        });

        app.get("/aluno/meus-cursos", ctx -> {

            String token = ctx.cookie("token");

            var decoded = JwtUtil.validateToken(token);

            int alunoId = decoded.getClaim("id").asInt();

            var cursos = matriculaService.buscarCursosPorAluno(alunoId);

            ctx.json(cursos);

        });
    }
}