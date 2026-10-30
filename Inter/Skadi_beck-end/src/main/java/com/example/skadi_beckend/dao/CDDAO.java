package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.CD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para manipular as operações da tabela CD
 */
public class CDDAO {

//   Inserir
    public boolean inserir(CD cd) {
        String sql = "insert into cd(\n" +
                "nome, cnpj)\n" +
                "values (?, ?)\n";
        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, cd.getNome());
            comando.setString(2, cd.getCnpj());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao inserir CD: " + erro.getMessage());
            return false;
        }
    }

//      Lista todos os CDS por orderm de ID
    public List<CD> listarTodos() {
        List<CD> lista = new ArrayList<>();
        String sql = "select id_cd, nome, cnpj, data_atualizacao\n" +
                "from cd\n" +
                "order by id_cd asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                CD cd = new CD(
                        rs.getInt("id_cd"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(cd);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar CDs: " + erro.getMessage());
        }
        return lista;
    }

//      Buscar por ID
    public CD buscarPorId(int idCd) {
        String sql = "select id_cd, nome, cnpj, data_atualizacao\n" +
                "from cd\n" +
                "where id_cd = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idCd);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new CD(
                            rs.getInt("id_cd"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar CD por id: " + erro.getMessage());
        }
        return null;
    }

//     Buscar por nome
    public List<CD> buscarPorNome(String trecho) {
        List<CD> lista = new ArrayList<>();
        String sql = "select id_cd, nome, cnpj, data_atualizacao\n" +
                "from cd\n" +
                "where lower(nome) like lower(?)\n" +
                "order by nome asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, "%" + trecho + "%");

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    CD cd = new CD(
                            rs.getInt("id_cd"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(cd);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar CD por nome: " + erro.getMessage());
        }
        return lista;
    }

//      Atualizar por ID
    public boolean atualizar(CD cd) {
        String sql = "update cd\n" +
                "set nome = ?, cnpj = ?\n" +
                "where id_cd = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, cd.getNome());
            comando.setString(2, cd.getCnpj());
            comando.setInt(3, cd.getIdCd());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar CD: " + erro.getMessage());
            return false;
        }
    }

//      Atualizar nome por CNPJ
    public boolean atualizarNomePorCnpj(String cnpj, String novoNome) {
        String sql = "update cd\n" +
                "set nome = ?\n" +
                "where cnpj = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, novoNome);
            comando.setString(2, cnpj);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar nome do CD por CNPJ: " + erro.getMessage());
            return false;
        }
    }

//    Deletar por ID

    public boolean deletar(int idCd) {
        String sql = "delete from cd\n" +
                "where id_cd = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idCd);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar CD: " + erro.getMessage());
            return false;
        }
    }


//      Deletar por CNPJ
    public boolean deletarPorCnpj(String cnpj) {
        String sql = "delete from cd\n" +
                "where cnpj = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, cnpj);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar CD por CNPJ: " + erro.getMessage());
            return false;
        }
    }
}