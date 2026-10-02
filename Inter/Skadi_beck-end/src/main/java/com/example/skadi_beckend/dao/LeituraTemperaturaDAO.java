package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.model.LeituraTemperatura;
import com.example.skadi_beckend.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeituraTemperaturaDAO {

    public List<LeituraTemperatura> listarTodos() throws SQLException {
        List<LeituraTemperatura> lista = new ArrayList<>();
        String sql = "SELECT * FROM leituratemperatura";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new LeituraTemperatura(
                        rs.getInt("id_leitura"),
                        rs.getDouble("temperatura"),
                        rs.getDate("data"),
                        rs.getInt("id_termometro")
                ));
            }
        }
        return lista;
    }

    public LeituraTemperatura buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM leituratemperatura WHERE id_leitura = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new LeituraTemperatura(
                            rs.getInt("id_leitura"),
                            rs.getDouble("temperatura"),
                            rs.getDate("data"),
                            rs.getInt("id_termometro")
                    );
                }
            }
        }
        return null;
    }

    public void inserir(LeituraTemperatura l) throws SQLException {
        String sql = "INSERT INTO leituratemperatura (temperatura, data, id_termometro) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, l.getTemperatura());
            stmt.setDate(2, l.getData());
            stmt.setInt(3, l.getId_termometro());
            stmt.executeUpdate();
        }
    }

    public void atualizar(LeituraTemperatura l) throws SQLException {
        String sql = "UPDATE leituratemperatura SET temperatura = ?, data = ?, id_termometro = ? WHERE id_leitura = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, l.getTemperatura());
            stmt.setDate(2, l.getData());
            stmt.setInt(3, l.getId_termometro());
            stmt.setInt(4, l.getId_leitura());
            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM leituratemperatura WHERE id_leitura = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}