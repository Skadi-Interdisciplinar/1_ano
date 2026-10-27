package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Classe DAO para manipular as operações da tabela Categoria
 */

public class CategoriaDAO {

//    Inserir
    public boolean inserir(Categoria categoria) {
        if (categoria.getTemperaturaMax() <= categoria.getTemperaturaMin()) {
            System.out.println("Validação falhou: Temperatura máxima deve ser maior que a mínima.");
            return false;
        }

        String sql = "insert into Categoria (nome, vida_util_horas, temperatura_max, temperatura_min) values (?, ?::interval, ?, ?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, categoria.getNome());


            comando.setString(2, categoria.getVidaUtilHoras());

            comando.setDouble(3, categoria.getTemperaturaMax());
            comando.setDouble(4, categoria.getTemperaturaMin());

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao inserir categoria: " + erro.getMessage());
            return false;
        }
    }

//    Listar todos
    public List<Categoria> listarTodos() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "select * from Categoria order by nome asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nome"),
                        rs.getString("vida_util_horas"), // Lê o intervalo retornado pelo banco como String
                        rs.getDouble("temperatura_max"),
                        rs.getDouble("temperatura_min"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(categoria);
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao listar categorias: " + erro.getMessage());
        }
        return lista;
    }

//   Buscar por ID
    public Categoria buscarPorId(int idCategoria) {
        String sql = "select * from Categoria where id_categoria = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCategoria);
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nome"),
                            rs.getString("vida_util_horas"),
                            rs.getDouble("temperatura_max"),
                            rs.getDouble("temperatura_min"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao buscar categoria por id: " + erro.getMessage());
        }
        return null;
    }

//    Buscar por nome
    public List<Categoria> buscarPorNome(String trechoPesquisa) {
        List<Categoria> lista = new ArrayList<>();
        String sql = "select * from Categoria where lower(nome) like lower(?) order by nome asc";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, "%" + trechoPesquisa + "%");

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Categoria categoria = new Categoria(
                            rs.getInt("id_categoria"),
                            rs.getString("nome"),
                            rs.getString("vida_util_horas"),
                            rs.getDouble("temperatura_max"),
                            rs.getDouble("temperatura_min"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(categoria);
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao buscar categoria por nome: " + erro.getMessage());
        }
        return lista;
    }

//    Atualizar
    public boolean atualizar(Categoria categoria) {
        String sql = "update Categoria set nome = ?, vida_util_horas = ?::interval, temperatura_max = ?, temperatura_min = ? where id_categoria = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, categoria.getNome());
            comando.setString(2, categoria.getVidaUtilHoras());
            comando.setDouble(3, categoria.getTemperaturaMax());
            comando.setDouble(4, categoria.getTemperaturaMin());
            comando.setInt(5, categoria.getIdCategoria());

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao atualizar categoria: " + erro.getMessage());
            return false;
        }
    }

//    Atualizar vida util por nome
    public boolean atualizarVidaUtilPorNome(String nome, String novaVidaUtil) {
        String sql = "update Categoria set vida_util_horas = ?::interval where lower(nome) = lower(?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, novaVidaUtil);
            comando.setString(2, nome);

            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao atualizar vida útil por nome: " + erro.getMessage());
            return false;
        }
    }

//    Deletar por nome
    public boolean deletarPorNome(String nome) {
        String sql = "delete from Categoria where lower(nome) = lower(?)";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, nome);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao deletar categoria por nome: " + erro.getMessage());
            return false;
        }
    }

//   Deletar por ID
    public boolean deletar(int idCategoria) {
        String sql = "delete from Categoria where id_categoria = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCategoria);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao deletar categoria: " + erro.getMessage());
            return false;
        }
    }
}