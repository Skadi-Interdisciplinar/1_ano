package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Frigorifico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 - Classe DAO para manipular as operações da tabela Frigorifico

 */
public class FrigorificoDAO {

    // Inserir
    public boolean inserir(Frigorifico frigorifico) {
        String sql = "insert into Frigorifico (\n" +
                "    nome, localizacao, id_cd, temperatura_max, temperatura_min\n" +
                ") values (?, ?, ?, ?, ?)\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, frigorifico.getNome());
            comando.setString(2, frigorifico.getLocalizacao());
            comando.setInt(3, frigorifico.getIdCd());
            comando.setDouble(4, frigorifico.getTemperaturaMax());
            comando.setDouble(5, frigorifico.getTemperaturaMin());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao inserir frigorifico: " + erro.getMessage());
            return false;
        }
    }

//     Lista todos os frigoríficos cadastrados em ordem crescente por ID

    public List<Frigorifico> listarTodos() {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "select id_frigorifico, nome, localizacao, id_cd, temperatura_max, temperatura_min, data_atualizacao\n" +
                "from Frigorifico\n" +
                "order by id_frigorifico asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                Frigorifico frigorifico = new Frigorifico(
                        rs.getInt("id_frigorifico"),
                        rs.getString("nome"),
                        rs.getString("localizacao"),
                        rs.getInt("id_cd"),
                        rs.getDouble("temperatura_max"),
                        rs.getDouble("temperatura_min"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(frigorifico);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar frigorificos: " + erro.getMessage());
        }
        return lista;
    }


    //     Busca um frigorífico específico pelo ID
    public Frigorifico buscarPorId(int idFrigorifico) {
        String sql = "select id_frigorifico, nome, localizacao, id_cd, temperatura_max, temperatura_min, data_atualizacao\n" +
                "from Frigorifico\n" +
                "where id_frigorifico = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idFrigorifico);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new Frigorifico(
                            rs.getInt("id_frigorifico"),
                            rs.getString("nome"),
                            rs.getString("localizacao"),
                            rs.getInt("id_cd"),
                            rs.getDouble("temperatura_max"),
                            rs.getDouble("temperatura_min"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar frigorifico por id: " + erro.getMessage());
        }
        return null;
    }


//     Busca frigorificos por nome

    public List<Frigorifico> buscarPorNome(String trecho) {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "select id_frigorifico, nome, localizacao, id_cd, temperatura_max, temperatura_min, data_atualizacao\n" +
                "from Frigorifico\n" +
                "where lower(nome) like lower(?)\n" +
                "order by nome asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, "%" + trecho + "%");

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Frigorifico frigorifico = new Frigorifico(
                            rs.getInt("id_frigorifico"),
                            rs.getString("nome"),
                            rs.getString("localizacao"),
                            rs.getInt("id_cd"),
                            rs.getDouble("temperatura_max"),
                            rs.getDouble("temperatura_min"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(frigorifico);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar frigorificos por nome: " + erro.getMessage());
        }
        return lista;
    }

    //    Lista por CD
    public List<Frigorifico> listarPorCd(int idCd) {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "select id_frigorifico, nome, localizacao, id_cd, temperatura_max, temperatura_min, data_atualizacao\n" +
                "from Frigorifico\n" +
                "where id_cd = ?\n" +
                "order by id_frigorifico asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idCd);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Frigorifico frigorifico = new Frigorifico(
                            rs.getInt("id_frigorifico"),
                            rs.getString("nome"),
                            rs.getString("localizacao"),
                            rs.getInt("id_cd"),
                            rs.getDouble("temperatura_max"),
                            rs.getDouble("temperatura_min"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(frigorifico);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar frigorificos por CD: " + erro.getMessage());
        }
        return lista;
    }


    //     Atualiza tudo que der
    public boolean atualizar(Frigorifico frigorifico) {
        String sql = "update Frigorifico\n" +
                "set nome = ?, localizacao = ?, id_cd = ?, temperatura_max = ?, temperatura_min = ?\n" +
                "where id_frigorifico = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, frigorifico.getNome());
            comando.setString(2, frigorifico.getLocalizacao());
            comando.setInt(3, frigorifico.getIdCd());
            comando.setDouble(4, frigorifico.getTemperaturaMax());
            comando.setDouble(5, frigorifico.getTemperaturaMin());
            comando.setInt(6, frigorifico.getIdFrigorifico());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao atualizar frigorifico: " + erro.getMessage());
            return false;
        }
    }

    //      Atualiza a localização
    public boolean atualizarLocalizacao(int idFrigorifico, String novaLocalizacao) {
        String sql = "update Frigorifico\n" +
                "set localizacao = ?\n" +
                "where id_frigorifico = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, novaLocalizacao);
            comando.setInt(2, idFrigorifico);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao atualizar localizacao do frigorifico: " + erro.getMessage());
            return false;
        }
    }

    //    Deleta por um ID
    public boolean deletar(int idFrigorifico) {
        String sql = "delete from Frigorifico\n" +
                "where id_frigorifico = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idFrigorifico);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao deletar frigorifico por id: " + erro.getMessage());
            return false;
        }
    }

    //      Deleta por localização
    public boolean deletarPorLocalizacao(String localizacao) {
        String sql = "delete from Frigorifico\n" +
                "where localizacao = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, localizacao);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("erro ao deletar frigorifico por localizacao: " + erro.getMessage());
            return false;
        }
    }

    //      Lista os frigorificos com termometros ativos
    public List<Frigorifico> listarComTermometrosAtivos() {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "select distinct f.id_frigorifico, f.nome, f.localizacao, f.id_cd, f.temperatura_max, f.temperatura_min, f.data_atualizacao\n" +
                "from Frigorifico f\n" +
                "inner join Termometro t on f.id_frigorifico = t.id_frigorifico\n" +
                "where t.status = 'ativo'\n" +
                "order by f.id_frigorifico asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                Frigorifico frigorifico = new Frigorifico(
                        rs.getInt("id_frigorifico"),
                        rs.getString("nome"),
                        rs.getString("localizacao"),
                        rs.getInt("id_cd"),
                        rs.getDouble("temperatura_max"),
                        rs.getDouble("temperatura_min"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(frigorifico);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar frigorificos com termometros ativos: " + erro.getMessage());
        }
        return lista;
    }
}