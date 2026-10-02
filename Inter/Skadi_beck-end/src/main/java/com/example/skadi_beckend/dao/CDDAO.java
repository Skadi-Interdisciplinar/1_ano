package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.CD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável pelas operações de banco de dados (CRUD) da entidade CD.
 */
public class CDDAO {

    // CREATE (Inserir)
    public boolean inserir(CD cd) {
        String sql = "INSERT INTO CD (nome, cnpj, endereco) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cd.getNome());
            stmt.setString(2, cd.getCnpj());
            stmt.setString(3, cd.getEndereco());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao inserir CD: " + e.getMessage());
            return false;
        }
    }

    // READ (Listar todos)
    public List<CD> listarTodos() {
        List<CD> lista = new ArrayList<>();
        String sql = "SELECT * FROM CD ORDER BY id_cd ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CD cd = new CD();
                cd.setIdCd(rs.getInt("id_cd"));
                cd.setNome(rs.getString("nome"));
                cd.setCnpj(rs.getString("cnpj"));
                cd.setEndereco(rs.getString("endereco"));
                lista.add(cd);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar CDs: " + e.getMessage());
        }
        return lista;
    }

    // UPDATE (Atualizar)
    public boolean atualizar(CD cd) {
        String sql = "UPDATE CD SET nome = ?, cnpj = ?, endereco = ? WHERE id_cd = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cd.getNome());
            stmt.setString(2, cd.getCnpj());
            stmt.setString(3, cd.getEndereco());
            stmt.setInt(4, cd.getIdCd());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar CD: " + e.getMessage());
            return false;
        }
    }

    // DELETE (Deletar)
    public boolean deletar(int idCd) {
        String sql = "DELETE FROM CD WHERE id_cd = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCd);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar CD (pode estar vinculado a outras tabelas): " + e.getMessage());
            return false;
        }
    }
}