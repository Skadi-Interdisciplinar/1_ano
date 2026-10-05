package com.example.skadi_beckend.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe de teste para validar a conectividade entre a aplicação Java e o banco de dados PostgreSQL.
 * Executa uma consulta simples (SELECT 1) para confirmar a conexão.
 */
public class TesteConexao {

    public static void main(String[] args) {
        System.out.println("Iniciando teste de conexão com o PostgreSQL...");

        // Utiliza try-with-resources para garantir o fechamento automático da conexão e do statement
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement stmt = conexao.prepareStatement("SELECT 1");
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                System.out.println(" Conexão estabelecida com sucesso!");
                System.out.println("Resultado do teste do banco: " + rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados!");
            e.printStackTrace();
        }
    }
}