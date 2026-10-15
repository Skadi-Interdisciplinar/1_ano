package com.example.skadi_beckend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Classe utilitária responsável por gerenciar e fornecer conexões com o banco de dados PostgreSQL.
 */
public class ConnectionFactory {

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados PostgreSQL.
     *
     * @return Connection objeto de conexão ativo com o banco de dados.
     * @throws SQLException caso ocorra falha ao carregar o driver ou ao conectar com o banco.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Registra explicitamente o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");

            //  Busca das VM Options do IntelliJ
            String url = System.getProperty("POSTGRES_URL");
            String user = System.getProperty("POSTGRES_USER");
            String pass = System.getProperty("POSTGRES_PASSWORD");

            //  Busca das variáveis de ambiente do sistema
            if (url == null) url = System.getenv("POSTGRES_URL");
            if (user == null) user = System.getenv("POSTGRES_USER");
            if (pass == null) pass = System.getenv("POSTGRES_PASSWORD");

            //  Se ainda nulo, tenta carregar pelo Dotenv
            if (url == null || user == null || pass == null) {
                try {
                    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                    if (url == null) url = dotenv.get("POSTGRES_URL");
                    if (user == null) user = dotenv.get("POSTGRES_USER");
                    if (pass == null) pass = dotenv.get("POSTGRES_PASSWORD");
                } catch (Exception e) {
                }
            }

            // Valida se as credenciais foram carregadas corretamente
            if (url == null || user == null || pass == null) {
                throw new SQLException("Erro de configuração: As credenciais do banco (POSTGRES_URL, POSTGRES_USER, POSTGRES_PASSWORD) não foram encontradas.");
            }

            // Abre e retorna a conexão com o PostgreSQL
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            throw new SQLException("Erro crítico: Driver do banco de dados PostgreSQL não foi encontrado.", e);
        }
    }
}