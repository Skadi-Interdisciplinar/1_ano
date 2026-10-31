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

    // Busca por nome dentro da CD do admin
    public List<Frigorifico> buscarPorNomeECd(String trecho, int idCd) {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "SELECT * FROM Frigorifico WHERE id_cd = ? AND lower(nome) LIKE lower(?) " +
                "ORDER BY nome ASC";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idCd);
            comando.setString(2, "%" + trecho + "%");
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) lista.add(mapearFrigorifico(rs));
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao pesquisar frigoríficos da CD: " + erro.getMessage());
        }
        return lista;
    }

    // Busca frigorífico apenas quando ele pertence à CD autorizada
    public Frigorifico buscarPorIdECd(int idFrigorifico, int idCd) {
        String sql = "SELECT * FROM Frigorifico WHERE id_frigorifico = ? AND id_cd = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idFrigorifico);
            comando.setInt(2, idCd);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next() ? mapearFrigorifico(rs) : null;
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao buscar frigorífico da CD: " + erro.getMessage());
            return null;
        }
    }

    // Lista frigoríficos com termômetros ativos
    public List<Frigorifico> listarComTermometrosAtivosPorCd(int idCd) {
        List<Frigorifico> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT f.* FROM Frigorifico f " +
                "JOIN Termometro t ON t.id_frigorifico = f.id_frigorifico " +
                "WHERE f.id_cd = ? AND t.status = 'ativo' ORDER BY f.id_frigorifico ASC";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idCd);
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) lista.add(mapearFrigorifico(rs));
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao listar frigoríficos ativos da CD: " + erro.getMessage());
        }
        return lista;
    }

    // Atualização usa id_cd no filtro para bloquear alterações de outra unidade
    public boolean atualizarPorCd(Frigorifico frigorifico, int idCd) {
        String sql = "UPDATE Frigorifico SET nome = ?, localizacao = ?, temperatura_max = ?, temperatura_min = ? " +
                "WHERE id_frigorifico = ? AND id_cd = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, frigorifico.getNome());
            comando.setString(2, frigorifico.getLocalizacao());
            comando.setDouble(3, frigorifico.getTemperaturaMax());
            comando.setDouble(4, frigorifico.getTemperaturaMin());
            comando.setInt(5, frigorifico.getIdFrigorifico());
            comando.setInt(6, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar frigorífico da CD: " + erro.getMessage());
            return false;
        }
    }

    public boolean deletarPorIdECd(int idFrigorifico, int idCd) {
        String sql = "DELETE FROM Frigorifico WHERE id_frigorifico = ? AND id_cd = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idFrigorifico);
            comando.setInt(2, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao excluir frigorífico da CD: " + erro.getMessage());
            return false;
        }
    }

    public boolean deletarPorLocalizacaoECd(String localizacao, int idCd) {
        String sql = "DELETE FROM Frigorifico WHERE localizacao = ? AND id_cd = ?";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, localizacao);
            comando.setInt(2, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao excluir frigorífico por localização da CD: " + erro.getMessage());
            return false;
        }
    }

    private Frigorifico mapearFrigorifico(ResultSet rs) throws SQLException {
        return new Frigorifico(rs.getInt("id_frigorifico"), rs.getString("nome"),
                rs.getString("localizacao"), rs.getInt("id_cd"), rs.getDouble("temperatura_max"),
                rs.getDouble("temperatura_min"), rs.getTimestamp("data_atualizacao"));
    }
}
