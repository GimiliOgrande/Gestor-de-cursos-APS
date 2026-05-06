package br.ufpb.dcx.aps;

import br.ufpb.dcx.aps.config.DatabaseConnection;
import br.ufpb.dcx.aps.controller.AuthController;
import br.ufpb.dcx.aps.controller.CursoController;
import br.ufpb.dcx.aps.controller.MatriculaController;
import br.ufpb.dcx.aps.model.CategoriaUsuario;
import br.ufpb.dcx.aps.model.Curso;
import br.ufpb.dcx.aps.model.Usuario;
import br.ufpb.dcx.aps.notification.NotificacaoServiceListener;
import br.ufpb.dcx.aps.notification.Notificador;
import br.ufpb.dcx.aps.repository.CursoRepository;
import br.ufpb.dcx.aps.repository.MatriculaRepository;
import br.ufpb.dcx.aps.repository.NotificacaoRepository;
import br.ufpb.dcx.aps.repository.UsuarioRepository;
import br.ufpb.dcx.aps.security.AuthorizationMiddleware;
import br.ufpb.dcx.aps.security.JwtUtil; // 🔥 IMPORTANTE
import br.ufpb.dcx.aps.service.AuthService;
import br.ufpb.dcx.aps.service.CursoService;
import br.ufpb.dcx.aps.service.MatriculaService;
import br.ufpb.dcx.aps.service.UsuarioService;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.logging.Logger;

public class App {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {

        LOGGER.info("Iniciando Sistema de Gestão de Cursos...");

        // Inicializa banco
        DatabaseConnection.initDatabase();

        // Repositories
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        CursoRepository cursoRepository = new CursoRepository();
        MatriculaRepository matriculaRepository = new MatriculaRepository();

        // Services
        AuthService authService = new AuthService(usuarioRepository);
        UsuarioService usuarioService = new UsuarioService(usuarioRepository);
        CursoService cursoService = new CursoService(cursoRepository, usuarioRepository);
        Notificador notificador = new Notificador();
        NotificacaoRepository notificacaoRepository = new NotificacaoRepository();

        notificador.adicionarListener(
                new NotificacaoServiceListener(notificacaoRepository)
        );
        MatriculaService matriculaService =
                new MatriculaService(matriculaRepository, usuarioRepository, cursoRepository, notificador);

        // Dados iniciais para teste
        Usuario admin = new Usuario(0, "João", "joao@email.com", "123456", CategoriaUsuario.ADMIN);
        Usuario aluno = new Usuario(0, "Joana", "joana@email.com", "123456", CategoriaUsuario.ALUNO);
        Usuario professor = new Usuario(0, "Ramon", "ramon@email.com", "123456", CategoriaUsuario.PROFESSOR);

        usuarioRepository.salvar(admin);
        usuarioRepository.salvar(aluno);
        usuarioRepository.salvar(professor);

        // Curso de teste
        Curso cursoTeste = new Curso(0, "Java Básico", "Curso introdutório de Java", 3);
        cursoRepository.salvar(cursoTeste);

        Curso cursoTeste1 = new Curso(0, "Java Básico 2", "Curso de Java 2", 3);
        cursoRepository.salvar(cursoTeste1);

        LOGGER.info("Dados iniciais criados com sucesso.");

        // Javalin
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
                staticFiles.location = Location.CLASSPATH;
            });
            TemplateEngine templateEngine = new TemplateEngine();

            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("/templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML");
            resolver.setCharacterEncoding("UTF-8");

            templateEngine.setTemplateResolver(resolver);

            config.fileRenderer(new JavalinThymeleaf(templateEngine));
        });

        // Exception global
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500).result("Erro interno: " + e.getMessage());
        });

        // Controllers
        AuthController.register(app, authService, usuarioRepository);
        CursoController.register(app, cursoService, usuarioRepository);
        MatriculaController.register(app, matriculaService, notificacaoRepository);

        // Proteções globais
        app.before("/admin/*",
                AuthorizationMiddleware.requireAnyRole(CategoriaUsuario.ADMIN)
        );

        app.before("/professor/*",
                AuthorizationMiddleware.requireAnyRole(
                        CategoriaUsuario.ADMIN,
                        CategoriaUsuario.PROFESSOR
                )
        );

        app.before("/aluno/*",
                AuthorizationMiddleware.requireAnyRole(
                        CategoriaUsuario.ADMIN,
                        CategoriaUsuario.PROFESSOR,
                        CategoriaUsuario.ALUNO
                )
        );

        // Rotas básicas
        app.get("/", ctx -> ctx.redirect("/login.html"));
        app.get("/login", ctx -> ctx.redirect("/login.html"));

        // DASHBOARD DINÂMICO
        app.get("/dashboard", ctx -> {

            String token = ctx.cookie("token");

            System.out.println("TOKEN: " + token);

            if (token == null) {
                ctx.redirect("/login.html");
                return;
            }

            try {
                var decoded = JwtUtil.validateToken(token);
                String categoria = decoded.getClaim("categoria").asString();

                System.out.println("CATEGORIA: " + categoria); // 🔥

                switch (categoria) {
                    case "ADMIN":
                        ctx.render("admin-dashboard.html");
                        break;
                    case "PROFESSOR":
                        ctx.render("professor-dashboard.html");
                        break;
                    case "ALUNO":
                        ctx.render("aluno-dashboard.html");
                        break;
                    default:
                        ctx.status(403).result("Categoria inválida");
                }

            } catch (Exception e) {
                ctx.redirect("/login.html");
            }
        });

        // Registro
        app.post("/register", ctx -> {

            String nome = ctx.formParam("nome");
            String email = ctx.formParam("email");
            String senha = ctx.formParam("senha");
            String categoriaStr = ctx.formParam("categoria");

            CategoriaUsuario categoria = CategoriaUsuario.valueOf(categoriaStr);

            try {
                usuarioService.cadastrar(nome, email, senha, categoria);
                ctx.result("Usuário cadastrado com sucesso!");
            } catch (RuntimeException e) {
                ctx.status(400).result(e.getMessage());
            }
        });

        app.start(7000);

        LOGGER.info("Servidor inicializado na porta 7000");
    }
}