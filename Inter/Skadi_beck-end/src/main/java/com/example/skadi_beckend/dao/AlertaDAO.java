package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.model.Alerta;
import com.example.skadi_beckend.connection.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    public List<Alerta> listarTodos() throws SQLException {
        List<Alerta> lista = new ArrayList<>();
        String sql = "SELECT * FROM alerta";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Alerta(
                        rs.getInt("id_alerta"),
                        rs.getString("status"),
                        (Integer) rs.getObject("tempo_sobrevivencia"),
                        rs.getTimestamp("data_hora"),
                        rs.getString("tipo"),
                        rs.getString("nivel_gravidade"),
                        rs.getString("canal"),
                        rs.getString("notificacao"),
                        rs.getDate("data_envio"),
                        rs.getInt("id_leitura"),
                        (Integer) rs.getObject("id_usuario")
                ));
            }
        }
        return lista;
    }

    public Alerta buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM alerta WHERE id_alerta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Alerta(
                            rs.getInt("id_alerta"),
                            rs.getString("status"),
                            (Integer) rs.getObject("tempo_sobrevivencia"),
                            rs.getTimestamp("data_hora"),
                            rs.getString("tipo"),
                            rs.getString("nivel_gravidade"),
                            rs.getString("canal"),
                            rs.getString("notificacao"),
                            rs.getDate("data_envio"),
                            rs.getInt("id_leitura"),
                            (Integer) rs.getObject("id_usuario")
                    );
                }
            }
        }
        return null;
    }

    public void inserir(Alerta a) throws SQLException {
        String sql = "INSERT INTO alerta (status, tempo_sobrevivencia, tipo, nivel_gravidade, canal, notificacao, data_envio, id_leitura, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getStatus());
            stmt.setObject(2, a.getTempo_sobrevivencia(), Types.INTEGER);
            stmt.setString(3, a.getTipo());
            stmt.setString(4, a.getNivel_gravidade());
            stmt.setString(5, a.getCanal());
            stmt.setString(6, a.getNotificacao());
            stmt.setDate(7, a.getData_envio());
            stmt.setInt(8, a.getId_leitura());
            stmt.setObject(9, a.getId_usuario(), Types.INTEGER);

            stmt.executeUpdate();
        }
    }

    public void atualizar(Alerta a) throws SQLException {
        String sql = "UPDATE alerta SET status = ?, tempo_sobrevivencia = ?, tipo = ?, nivel_gravidade = ?, canal = ?, notificacao = ?, data_envio = ?, id_leitura = ?, id_usuario = ? WHERE id_alerta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getStatus());
            stmt.setObject(2, a.getTempo_sobrevivencia(), Types.INTEGER);
            stmt.setString(3, a.getTipo());
            stmt.setString(4, a.getNivel_gravidade());
            stmt.setString(5, a.getCanal());
            stmt.setString(6, a.getNotificacao());
            stmt.setDate(7, a.getData_envio());
            stmt.setInt(8, a.getId_leitura());
            stmt.setObject(9, a.getId_usuario(), Types.INTEGER);
            stmt.setInt(10, a.getId_alerta());

            stmt.executeUpdate();
        }
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM alerta WHERE id_alerta = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}