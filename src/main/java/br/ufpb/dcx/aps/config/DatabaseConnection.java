package br.ufpb.dcx.aps.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:h2:mem:apsdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initDatabase() {

        String sqlUsuario = """
            CREATE TABLE IF NOT EXISTS usuario (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nome VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                senha VARCHAR(255) NOT NULL,
                categoria VARCHAR(20) NOT NULL
            );
        """;

        String sqlCurso = """
            CREATE TABLE IF NOT EXISTS curso (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nome VARCHAR(100) NOT NULL,
                descricao VARCHAR(500),
                professor_id INT,
                CONSTRAINT fk_professor
                    FOREIGN KEY (professor_id)
                    REFERENCES usuario(id)
            );
        """;

        String sqlMatricula = """
            CREATE TABLE IF NOT EXISTS matricula (
                id INT PRIMARY KEY AUTO_INCREMENT,
                aluno_id INT NOT NULL,
                curso_id INT NOT NULL,
                data_matricula TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        
                CONSTRAINT fk_aluno
                    FOREIGN KEY (aluno_id)
                    REFERENCES usuario(id)
                    ON DELETE CASCADE,
        
                CONSTRAINT fk_curso
                    FOREIGN KEY (curso_id)
                    REFERENCES curso(id)
                    ON DELETE CASCADE
            );
        """;

        String sqlNotificacao = """
            CREATE TABLE IF NOT EXISTS notificacao (
                id INT PRIMARY KEY AUTO_INCREMENT,
                professor_id INT NOT NULL,
                mensagem VARCHAR(500) NOT NULL,
                lida BOOLEAN DEFAULT FALSE,
                data_notificacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                CONSTRAINT fk_notificacao_professor
                    FOREIGN KEY (professor_id)
                    REFERENCES usuario(id)
                    ON DELETE CASCADE
            );
        """;

        try (Connection conn = getConnection();
             var stmt = conn.createStatement()) {

            stmt.execute(sqlUsuario);
            System.out.println("Tabela usuario criada com sucesso.");

            stmt.execute(sqlCurso);
            System.out.println("Tabela curso criada com sucesso.");

            stmt.execute(sqlMatricula);
            System.out.println("Tabela matricula criada com sucesso.");

            stmt.execute(sqlNotificacao);
            System.out.println("Tabela notificacao criada com sucesso.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}