package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.model.Termometro;
import com.example.skadi_beckend.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TermometroDAO {

    public List<Termometro> listarTodos() throws SQLException {
        List<Termometro> lista = new ArrayList<>();
        String sql = "SELECT * FROM termometro";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Termometro(
                        rs.getInt("id_termometro"),
                        rs.getString("modelo"),
                        rs.getString("status"),
                        rs.getInt("id_refrigerador")
                ));
            }
        }
        return lista;
    }

    public Termometro buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM termometro WHERE id_termometro = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Termometro(
                            rs.getInt("id_termometro"),
                            rs.getString("modelo"),
                            rs.getString("status"),
                            rs.getInt("id_refrigerador")
                    );
                }
            }
        }
        return null;
    }

    public void inserir(Termometro t) throws SQLException {
        String sql = "INSERT INTO termometro (modelo, status, id_refrigerador) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getModelo());
            stmt.setString(2, t.getStatus());
            stmt.setInt(3, t.getId_refrigerador());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Termometro t) throws SQLException {
        String sql = "UPDATE termometro SET modelo = ?, status = ?, id_refrigerador = ? WHERE id_termometro = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, t.getModelo());
            stmt.setString(2, t.getStatus());
            stmt.setInt(3, t.getId_refrigerador());
            stmt.setInt(4, t.getId_termometro());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM termometro WHERE id_termometro = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}