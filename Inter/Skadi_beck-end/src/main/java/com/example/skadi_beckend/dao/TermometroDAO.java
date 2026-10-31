package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Termometro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
* Classe DAO para manipular as operações da tabela Termometro
*/
public class TermometroDAO {

//   Adicionar
    public boolean inserir(Termometro termometro) {
        String sql = "insert into Termometro (modelo, status, id_frigorifico) values (?, ?, ?)";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, termometro.getModelo());

            comando.setString(2, termometro.getStatus() != null ? termometro.getStatus() : "ativo");
            comando.setInt(3, termometro.getIdFrigorifico());

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao inserir termometro: " + erro.getMessage());
            return false;
        }
    }

//   Listar todos
    public List<Termometro> listarTodos() {
        List<Termometro> lista = new ArrayList<>();
        String sql = "select * from Termometro order by id_termometro asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                Termometro termometro = new Termometro(
                        rs.getInt("id_termometro"),
                        rs.getString("modelo"),
                        rs.getString("status"),
                        rs.getInt("id_frigorifico"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(termometro);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar termometros: " + erro.getMessage());
        }
        return lista;
    }

//   Listar por frigorifico
    public List<Termometro> listarPorFrigorifico(int idFrigorifico) {
        List<Termometro> lista = new ArrayList<>();
        String sql = "select * from Termometro where id_frigorifico = ? order by id_termometro asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idFrigorifico);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Termometro termometro = new Termometro(
                            rs.getInt("id_termometro"),
                            rs.getString("modelo"),
                            rs.getString("status"),
                            rs.getInt("id_frigorifico"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(termometro);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar termometros pelo frigorifico: " + erro.getMessage());
        }
        return lista;
    }

    // Buscar por ID
    public Termometro buscarPorId(int idTermometro) {
        String sql = "select * from Termometro where id_termometro = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idTermometro);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new Termometro(
                            rs.getInt("id_termometro"),
                            rs.getString("modelo"),
                            rs.getString("status"),
                            rs.getInt("id_frigorifico"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar termometro por id: " + erro.getMessage());
        }
        return null;
    }

//    Atualizar
    public boolean atualizar(Termometro termometro) {
        String sql = "update Termometro set modelo = ?, status = ?, id_frigorifico = ? where id_termometro = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, termometro.getModelo());
            comando.setString(2, termometro.getStatus());
            comando.setInt(3, termometro.getIdFrigorifico());
            comando.setInt(4, termometro.getIdTermometro());

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar termometro: " + erro.getMessage());
            return false;
        }
    }

//   Atualizar status por frigorifico
    public boolean atualizarStatusPorFrigorifico(int idFrigorifico, String novoStatus) {
        String sql = "update Termometro set status = ? where id_frigorifico = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, novoStatus);
            comando.setInt(2, idFrigorifico);

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar status dos termometros: " + erro.getMessage());
            return false;
        }
    }

//   Deletar por ID
    public boolean deletar(int idTermometro) {
        String sql = "delete from Termometro where id_termometro = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idTermometro);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar termometro: " + erro.getMessage());
            return false;
        }
    }

//   Deletar por frigorifico
    public boolean deletarPorFrigorifico(int idFrigorifico) {
        String sql = "delete from Termometro where id_frigorifico = ?";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idFrigorifico);

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar termometros pelo frigorifico: " + erro.getMessage());
            return false;
        }
    }

    // Admin lista termômetros somente dos frigoríficos da própria CD.
    public List<Termometro> listarPorCd(int idCd) {
        List<Termometro> lista = new ArrayList<>();
        String sql = "SELECT t.* FROM Termometro t JOIN Frigorifico f " +
                "ON f.id_frigorifico = t.id_frigorifico WHERE f.id_cd = ? " +
                "ORDER BY t.id_termometro ASC";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idCd);
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) lista.add(mapearTermometro(rs));
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao listar termômetros da CD: " + erro.getMessage());
        }
        return lista;
    }

    // Busca o termômetro só se o frigorífico associado pertencer à CD informada.
    public Termometro buscarPorIdECd(int idTermometro, int idCd) {
        String sql = "SELECT t.* FROM Termometro t JOIN Frigorifico f " +
                "ON f.id_frigorifico = t.id_frigorifico " +
                "WHERE t.id_termometro = ? AND f.id_cd = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idTermometro);
            comando.setInt(2, idCd);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next() ? mapearTermometro(rs) : null;
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao buscar termômetro da CD: " + erro.getMessage());
            return null;
        }
    }

    // A origem e o destino precisam pertencer à CD do admin antes de atualizar o termômetro.
    public boolean atualizarPorCd(Termometro termometro, int idCd) {
        String sql = "UPDATE Termometro t SET modelo = ?, status = ?, id_frigorifico = ? " +
                "WHERE t.id_termometro = ? " +
                "AND EXISTS (SELECT 1 FROM Frigorifico f WHERE f.id_frigorifico = t.id_frigorifico AND f.id_cd = ?) " +
                "AND EXISTS (SELECT 1 FROM Frigorifico destino WHERE destino.id_frigorifico = ? AND destino.id_cd = ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, termometro.getModelo());
            comando.setString(2, termometro.getStatus());
            comando.setInt(3, termometro.getIdFrigorifico());
            comando.setInt(4, termometro.getIdTermometro());
            comando.setInt(5, idCd);
            comando.setInt(6, termometro.getIdFrigorifico());
            comando.setInt(7, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar termômetro da CD: " + erro.getMessage());
            return false;
        }
    }

    // Exclui somente termômetro cujo frigorífico está na CD autorizada.
    public boolean deletarPorIdECd(int idTermometro, int idCd) {
        String sql = "DELETE FROM Termometro t WHERE t.id_termometro = ? " +
                "AND EXISTS (SELECT 1 FROM Frigorifico f WHERE f.id_frigorifico = t.id_frigorifico AND f.id_cd = ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idTermometro);
            comando.setInt(2, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao excluir termômetro da CD: " + erro.getMessage());
            return false;
        }
    }

    private Termometro mapearTermometro(ResultSet rs) throws SQLException {
        return new Termometro(rs.getInt("id_termometro"), rs.getString("modelo"),
                rs.getString("status"), rs.getInt("id_frigorifico"), rs.getTimestamp("data_atualizacao"));
    }
}
