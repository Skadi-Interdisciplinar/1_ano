package com.example.skadi_beckend.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Classe utilitária responsável por gerenciar e fornecer conexões com o banco de dados PostgreSQL.
 * Utiliza o padrão Factory para centralizar a criação de conexões e a biblioteca Dotenv
 * para carregar credenciais de forma segura a partir do arquivo de ambiente (.env).
 */
public class ConnectionFactory {

    /**
     * Estabelece e retorna uma nova conexão com o banco de dados PostgreSQL.
     *
     * @return Connection objeto de conexão ativo com o banco de dados.
     * @throws SQLException caso ocorra falha ao carregar o driver ou ao conectar com o banco.
     */
    public static Connection getConnection() throws SQLException {
        // Carrega as variáveis de ambiente do arquivo .env
        Dotenv dotenv = Dotenv.load();

        String url = dotenv.get("POSTGRES_URL");
        String user = dotenv.get("POSTGRES_USER");
        String pass = dotenv.get("POSTGRES_PASSWORD");

        try {
            // Registra explicitamente o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Abre e retorna a conexão usando as credenciais do .env
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            throw new SQLException("Erro crítico: Driver do banco de dados PostgreSQL não foi encontrado.", e);
        }
    }
}