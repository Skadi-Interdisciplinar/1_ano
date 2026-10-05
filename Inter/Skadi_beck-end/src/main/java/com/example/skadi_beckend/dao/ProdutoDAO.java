package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.model.Produto;
import com.example.skadi_beckend.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(montarObjeto(rs));
            }
        }
        return lista;
    }

    public Produto buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id_produto = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return montarObjeto(rs);
                }
            }
        }
        return null;
    }

    // Filtro pesquisar por categoria
    public List<Produto> buscarPorCategoria(String categoria) throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE LOWER(categoria) LIKE LOWER(?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + categoria + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(montarObjeto(rs));
                }
            }
        }
        return lista;
    }

    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produto (nome, categoria, temperatura_ideal, validade, tempo_sobrevivencia, id_refrigerador) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, p);
            stmt.executeUpdate();
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, categoria = ?, temperatura_ideal = ?, validade = ?, tempo_sobrevivencia = ?, id_refrigerador = ? WHERE id_produto = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            preencherStatement(stmt, p);
            stmt.setInt(7, p.getId_produto());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id_produto = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Métodos para evitar repetição de código
    private Produto montarObjeto(ResultSet rs) throws SQLException {
        Integer tempoSobrevivencia = rs.getObject("tempo_sobrevivencia") != null ? rs.getInt("tempo_sobrevivencia") : null;

        return new Produto(
                rs.getInt("id_produto"),
                rs.getString("nome"),
                rs.getString("categoria"),
                rs.getDouble("temperatura_ideal"),
                rs.getDate("validade"),
                tempoSobrevivencia,
                rs.getInt("id_refrigerador")
        );
    }

    private void preencherStatement(PreparedStatement stmt, Produto p) throws SQLException {
        stmt.setString(1, p.getNome());
        stmt.setString(2, p.getCategoria());
        stmt.setDouble(3, p.getTemperatura_ideal());
        stmt.setDate(4, p.getValidade());

        if (p.getTempo_sobrevivencia() != null) {
            stmt.setInt(5, p.getTempo_sobrevivencia());
        } else {
            stmt.setNull(5, Types.INTEGER);
        }
        stmt.setInt(6, p.getId_refrigerador());
    }
}