package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para manipular as operações da tabela Endereco
 */
public class EnderecoDAO {

//         Inserir
    public boolean inserir(Endereco endereco) {
        String sql = "insert into Endereco (\n" +
                "    id_cd, rua, numero, complemento, bairro, cidade, estado, cep\n" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, endereco.getIdCd());
            comando.setString(2, endereco.getRua());

            // Tratamento para número nulo
            if (endereco.getNumero() == null) {
                comando.setNull(3, java.sql.Types.INTEGER);
            } else {
                comando.setInt(3, endereco.getNumero());
            }

            comando.setString(4, endereco.getComplemento());
            comando.setString(5, endereco.getBairro());
            comando.setString(6, endereco.getCidade());
            comando.setString(7, endereco.getEstado());
            comando.setString(8, endereco.getCep());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao inserir endereco: " + erro.getMessage());
            return false;
        }
    }

    // Lista os endereços trazendo o nome do CD
    public List<Endereco> listarEnderecosComNomeCD() {
        List<Endereco> lista = new ArrayList<>();
        String sql = "select e.id_endereco, e.id_cd, e.rua, e.numero, e.complemento, e.bairro, " +
                "e.cidade, e.estado, e.cep, e.data_atualizacao, c.nome as nome_cd\n" +
                "from Endereco e\n" +
                "inner join CD c on e.id_cd = c.id_cd\n" +
                "order by e.id_endereco asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                Integer numero = (Integer) rs.getObject("numero");

                Endereco endereco = new Endereco(
                        rs.getInt("id_endereco"),
                        rs.getInt("id_cd"),
                        rs.getString("rua"),
                        numero,
                        rs.getString("complemento"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("cep"),
                        rs.getTimestamp("data_atualizacao")
                );
                endereco.setNomeCd(rs.getString("nome_cd"));
                lista.add(endereco);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar enderecos com nome do CD: " + erro.getMessage());
        }
        return lista;
    }

    // Busca endereço por trecho do nome da cidade
    public List<Endereco> buscarPorCidade(String trecho) {
        List<Endereco> lista = new ArrayList<>();
        String sql = "select * from Endereco\n" +
                "where lower(cidade) like lower(?)\n" +
                "order by cidade asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, "%" + trecho + "%");

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Integer numero = (Integer) rs.getObject("numero");

                    Endereco endereco = new Endereco(
                            rs.getInt("id_endereco"),
                            rs.getInt("id_cd"),
                            rs.getString("rua"),
                            numero,
                            rs.getString("complemento"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("cep"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(endereco);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar endereco por cidade: " + erro.getMessage());
        }
        return lista;
    }
    // Buscar por ID
    public Endereco buscarPorId(int idEndereco) {
        String sql = "select * from Endereco where id_endereco = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idEndereco);
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    Integer numero = (Integer) rs.getObject("numero");
                    return new Endereco(
                            rs.getInt("id_endereco"),
                            rs.getInt("id_cd"),
                            rs.getString("rua"),
                            numero,
                            rs.getString("complemento"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("cep"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar endereco por id: " + erro.getMessage());
        }
        return null;
    }

    // Atualiza todos os dados do endereço
    public boolean atualizar(Endereco endereco) {
        String sql = "update Endereco\n" +
                "set id_cd = ?, rua = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ?\n" +
                "where id_endereco = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, endereco.getIdCd());
            comando.setString(2, endereco.getRua());

            if (endereco.getNumero() == null) {
                comando.setNull(3, java.sql.Types.INTEGER);
            } else {
                comando.setInt(3, endereco.getNumero());
            }

            comando.setString(4, endereco.getComplemento());
            comando.setString(5, endereco.getBairro());
            comando.setString(6, endereco.getCidade());
            comando.setString(7, endereco.getEstado());
            comando.setString(8, endereco.getCep());
            comando.setInt(9, endereco.getIdEndereco());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao atualizar endereco: " + erro.getMessage());
            return false;
        }
    }

    // Deleta por ID
    public boolean deletar(int idEndereco) {
        String sql = "delete from Endereco\n" +
                "where id_endereco = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idEndereco);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao deletar endereco: " + erro.getMessage());
            return false;
        }
    }
}