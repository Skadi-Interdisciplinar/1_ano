package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FrigorificoDAO {

    public List<Frigorifico> listarTodos() throws SQLException {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "SELECT * FROM frigorifico";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Frigorifico(
                        rs.getInt("id_frigorifico"),
                        rs.getString("nome"),
                        rs.getString("localizacao"),
                        rs.getDouble("temperatura_min"),
                        rs.getDouble("temperatura_max"),
                        rs.getInt("id_cd")
                ));
            }
        }
        return lista;
    }

    public Frigorifico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM frigorifico WHERE id_frigorifico = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Frigorifico(
                            rs.getInt("id_frigorifico"),
                            rs.getString("nome"),
                            rs.getString("localizacao"),
                            rs.getDouble("temperatura_min"),
                            rs.getDouble("temperatura_max"),
                            rs.getInt("id_cd")
                    );
                }
            }
        }
        return null;
    }

    public void inserir(Frigorifico f) throws SQLException {
        String sql = "INSERT INTO frigorifico (nome, localizacao, temperatura_min, temperatura_max, id_cd) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getLocalizacao());
            stmt.setDouble(3, f.getTemperatura_min());
            stmt.setDouble(4, f.getTemperatura_max());
            stmt.setInt(5, f.getId_cd());
            stmt.executeUpdate();
        }
    }

    public void atualizar(Frigorifico f) throws SQLException {
        String sql = "UPDATE frigorifico SET nome = ?, localizacao = ?, temperatura_min = ?, temperatura_max = ?, id_cd = ? WHERE id_frigorifico = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getLocalizacao());
            stmt.setDouble(3, f.getTemperatura_min());
            stmt.setDouble(4, f.getTemperatura_max());
            stmt.setInt(5, f.getId_cd());
            stmt.setInt(6, f.getId_frigorifico());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM frigorifico WHERE id_frigorifico = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}