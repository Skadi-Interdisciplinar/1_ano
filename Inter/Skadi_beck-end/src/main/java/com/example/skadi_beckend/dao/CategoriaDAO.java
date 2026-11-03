package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Classe DAO para manipular as operações da tabela Categoria
 */

public class CategoriaDAO {

    // Lotes de outro cliente: têm unidade (id_cd) e ela não é uma das CDs do admin.
    // As CDs do admin são as da admin_cd mais o Usuario.id_cd (contas antigas).
    // Lotes sem unidade (id_cd vazio) não contam: nenhum admin enxerga esses lotes.
    private static final String LOTES_DE_OUTRO_CLIENTE = "from Lote l where l.id_cd is not null " +
            "and l.id_cd not in (select cod_cd from admin_cd where cod_admin = ? " +
            "union select id_cd from Usuario where id_usuario = ? and id_cd is not null)";

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

//   Confere se outra categoria já usa o nome (o nome é único no banco)
    public boolean nomeEmUso(String nome, Integer idIgnorado) {
        String sql = "select 1 from Categoria where lower(nome) = lower(?)" +
                (idIgnorado != null ? " and id_categoria <> ?" : "") + " limit 1";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, nome);
            if (idIgnorado != null) comando.setInt(2, idIgnorado);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao conferir nome de categoria: " + erro.getMessage());
            return false;
        }
    }

//   Confere se a categoria é usada por lotes de outro cliente (o admin não altera nem exclui)
    public boolean usadaPorOutroCliente(int idCategoria, int idAdmin) {
        String sql = "select 1 " + LOTES_DE_OUTRO_CLIENTE + " and l.id_categoria = ? limit 1";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idAdmin);
            comando.setInt(2, idAdmin);
            comando.setInt(3, idCategoria);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            // Erro de banco vira "Erro no servidor" (500), e não um aviso de categoria bloqueada
            System.out.println("Erro SQL ao conferir lotes de outro cliente: " + erro.getMessage());
            throw new IllegalStateException("Erro no banco ao conferir os lotes da categoria.", erro);
        }
    }

//   Ids das categorias usadas por lotes de outro cliente (a tela avisa antes de editar ou deletar)
    public Set<Integer> listarUsadasPorOutroCliente(int idAdmin) {
        Set<Integer> ids = new HashSet<>();
        String sql = "select distinct l.id_categoria " + LOTES_DE_OUTRO_CLIENTE;
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idAdmin);
            comando.setInt(2, idAdmin);
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt("id_categoria"));
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao listar categorias de outro cliente: " + erro.getMessage());
        }
        return ids;
    }

//   Confere se algum lote (de qualquer unidade) usa a categoria
    public boolean temLotes(int idCategoria) {
        String sql = "select 1 from Lote where id_categoria = ? limit 1";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCategoria);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            System.out.println("Erro SQL ao conferir lotes da categoria: " + erro.getMessage());
            return false;
        }
    }
}
