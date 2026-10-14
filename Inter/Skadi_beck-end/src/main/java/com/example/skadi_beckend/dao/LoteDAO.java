package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Lote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Classe DAO para manipular as operações da tabela Lote
 */

public class LoteDAO {

//    Inserir
    public boolean inserir(Lote lote) {

        if (lote.getDataValidade().before(lote.getDataFabricacao())) {
            System.out.println("Validação falhou: A data de validade não pode ser anterior à data de fabricação.");
            return false;
        }

        String sql = "insert into Lote (codigo_lote, id_categoria, data_fabricacao, data_validade, status) values (?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, lote.getCodigoLote());
            comando.setInt(2, lote.getIdCategoria());
            comando.setDate(3, lote.getDataFabricacao());
            comando.setDate(4, lote.getDataValidade());

            comando.setString(5, lote.getStatus() != null ? lote.getStatus() : "ativo");

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao inserir lote: " + erro.getMessage());
            return false;
        }
    }

//    Lista todos
    public List<Lote> listarTodos() {
        List<Lote> lista = new ArrayList<>();
        String sql = "select * from Lote order by data_validade asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                Lote lote = new Lote(
                        rs.getInt("id_lote"),
                        rs.getString("codigo_lote"),
                        rs.getInt("id_categoria"),
                        rs.getDate("data_fabricacao"),
                        rs.getDate("data_validade"),
                        rs.getString("status"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(lote);
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao listar lotes: " + erro.getMessage());
        }
        return lista;
    }

//    Lista lote por categoria
    public List<Lote> listarPorCategoria(int idCategoria) {
        List<Lote> lista = new ArrayList<>();
        String sql = "select * from Lote where id_categoria = ? order by data_validade asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCategoria);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Lote lote = new Lote(
                            rs.getInt("id_lote"),
                            rs.getString("codigo_lote"),
                            rs.getInt("id_categoria"),
                            rs.getDate("data_fabricacao"),
                            rs.getDate("data_validade"),
                            rs.getString("status"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(lote);
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao filtrar lotes por categoria: " + erro.getMessage());
        }
        return lista;
    }

    // Buscar por ID
    public Lote buscarPorId(int idLote) {
        String sql = "select * from Lote where id_lote = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idLote);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new Lote(
                            rs.getInt("id_lote"),
                            rs.getString("codigo_lote"),
                            rs.getInt("id_categoria"),
                            rs.getDate("data_fabricacao"),
                            rs.getDate("data_validade"),
                            rs.getString("status"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao buscar lote por id: " + erro.getMessage());
        }
        return null;
    }

//    Atualizar
    public boolean atualizar(Lote lote) {
        String sql = "update Lote set codigo_lote = ?, id_categoria = ?, data_fabricacao = ?, data_validade = ?, status = ? where id_lote = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, lote.getCodigoLote());
            comando.setInt(2, lote.getIdCategoria());
            comando.setDate(3, lote.getDataFabricacao());
            comando.setDate(4, lote.getDataValidade());
            comando.setString(5, lote.getStatus());
            comando.setInt(6, lote.getIdLote());

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao atualizar lote: " + erro.getMessage());
            return false;
        }
    }

//    Atualiza status por categoria
    public boolean atualizarStatusPorCategoria(int idCategoria, String novoStatus) {
        String sql = "update Lote set status = ? where id_categoria = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, novoStatus);
            comando.setInt(2, idCategoria);

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao atualizar status dos lotes em massa: " + erro.getMessage());
            return false;
        }
    }

//    Deletar por ID
    public boolean deletar(int idLote) {
        String sql = "delete from Lote where id_lote = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idLote);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao deletar lote: " + erro.getMessage());
            return false;
        }
    }

//    Deletar por Categoria
    public boolean deletarPorCategoria(int idCategoria) {
        String sql = "delete from Lote where id_categoria = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCategoria);

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao deletar lotes por categoria: " + erro.getMessage());
            return false;
        }
    }
}