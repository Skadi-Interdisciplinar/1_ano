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

//      Listar as CDs de um admin
//      Um admin pode ter várias CDs: elas ficam na tabela admin_cd.
//      A CD gravada em Usuario.id_cd também entra, para contas antigas que só têm esse vínculo.
//      Diferente dos outros métodos, um erro do banco aqui não devolve lista vazia:
//      lista vazia levaria o admin para o "cadastre sua primeira unidade". O servlet mostra "erro no servidor".
    public List<CD> listarPorAdmin(int idAdmin) {
        List<CD> lista = new ArrayList<>();
        String sql = "select id_cd, nome, cnpj, data_atualizacao\n" +
                "from cd\n" +
                "where id_cd in (select cod_cd from admin_cd where cod_admin = ?)\n" +
                "or id_cd = (select id_cd from Usuario where id_usuario = ?)\n" +
                "order by id_cd asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idAdmin);
            comando.setInt(2, idAdmin);

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
            System.out.println("erro ao listar CDs do admin: " + erro.getMessage());
            throw new IllegalStateException("Erro no banco ao listar as CDs do admin.", erro);
        }
        return lista;
    }

//      Confere se a CD é de um admin (mesma regra do listarPorAdmin)
//      Um erro do banco aqui também não devolve false: false viraria "sem permissão" (403)
//      para o próprio dono da CD. O servlet mostra "erro no servidor" (500).
    public boolean pertenceAoAdmin(int idAdmin, int idCd) {
        String sql = "select 1\n" +
                "from cd\n" +
                "where id_cd = ?\n" +
                "and (id_cd in (select cod_cd from admin_cd where cod_admin = ?)\n" +
                "or id_cd = (select id_cd from Usuario where id_usuario = ?))\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idCd);
            comando.setInt(2, idAdmin);
            comando.setInt(3, idAdmin);

            try (ResultSet rs = comando.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException erro) {
            System.out.println("erro ao conferir CD do admin: " + erro.getMessage());
            throw new IllegalStateException("Erro no banco ao conferir a CD do admin.", erro);
        }
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
//    Exclusão real: apaga tudo o que está ligado à CD e, por último, a própria CD.
//    Tudo roda numa única transação: se um passo falhar, nada é apagado.

    public boolean deletar(int idCd) {
        // Na ordem em que o banco permite apagar (cada comando recebe o id da CD uma vez)
        String frigorificosDaCd = "select id_frigorifico from Frigorifico where id_cd = ?";
        String termometrosDaCd = "select id_termometro from Termometro where id_frigorifico in (" + frigorificosDaCd + ")";
        String leiturasDaCd = "select id_leitura from LeituraTemperatura where id_termometro in (" + termometrosDaCd + ")";
        // só gestores e operadores são apagados junto com a CD; admin e super_admin continuam com a conta
        String funcionariosDaCd = "select id_usuario from Usuario where id_cd = ? and nivel_acesso in ('gestor', 'operador')";

        String[] comandos = {
                // alertas das leituras dos termômetros da CD (e as notificações deles)
                "delete from NotificacaoAlerta where id_alerta in (select id_alerta from Alerta where id_leitura in (" + leiturasDaCd + "))",
                "delete from Alerta where id_leitura in (" + leiturasDaCd + ")",
                "delete from LeituraTemperatura where id_termometro in (" + termometrosDaCd + ")",
                "delete from Termometro where id_frigorifico in (" + frigorificosDaCd + ")",
                // lotes que estão nos frigoríficos da CD (a CD do lote é a do último frigorífico dele)
                "delete from Lote where (select f.id_cd from lote_frigorifico lf" +
                        " join Frigorifico f on f.id_frigorifico = lf.id_frigorifico" +
                        " where lf.id_lote = Lote.id_lote" +
                        " order by lf.data_entrada desc, lf.id_lote_frigorifico desc limit 1) = ?",
                // passagens antigas, pelos frigoríficos da CD, de lotes que hoje estão em outra unidade
                "delete from lote_frigorifico where id_frigorifico in (" + frigorificosDaCd + ")",
                "delete from Frigorifico where id_cd = ?",
                // funcionários da CD (operadores primeiro: o banco não deixa operador sem gestor)
                "delete from NotificacaoAlerta where id_usuario in (" + funcionariosDaCd + ")",
                "delete from admin_cd where cod_admin in (" + funcionariosDaCd + ")",
                "delete from admin_cd where cod_cd = ?",
                // o admin que estava usando esta CD passa para outra CD dele (ou fica sem CD, se era a única)
                "update Usuario set id_cd = (select min(cod_cd) from admin_cd where cod_admin = Usuario.id_usuario)" +
                        " where id_cd = ? and nivel_acesso in ('admin', 'super_admin')",
                "delete from Usuario where id_cd = ? and nivel_acesso = 'operador'",
                "delete from Usuario where id_cd = ?",
                "delete from Endereco where id_cd = ?",
                "delete from cd where id_cd = ?"
        };

        try (Connection conexao = ConnectionFactory.getConnection()) {
            boolean autoCommitOriginal = conexao.getAutoCommit();
            conexao.setAutoCommit(false);
            try {
                int linhasAfetadas = 0;
                for (String sql : comandos) {
                    try (PreparedStatement comando = conexao.prepareStatement(sql)) {
                        comando.setInt(1, idCd);
                        linhasAfetadas = comando.executeUpdate();
                    }
                }

                // o último comando apaga a CD; se ela não existia, desfaz tudo
                if (linhasAfetadas == 0) {
                    conexao.rollback();
                    return false;
                }
                conexao.commit();
                return true;
            } catch (SQLException erro) {
                conexao.rollback();
                throw erro;
            } finally {
                conexao.setAutoCommit(autoCommitOriginal);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao deletar CD: " + erro.getMessage());
            return false;
        }
    }


//      Deletar por CNPJ (usa a mesma exclusão completa do ID)
    public boolean deletarPorCnpj(String cnpj) {
        String sql = "select id_cd from cd\n" +
                "where cnpj = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, cnpj);

            try (ResultSet rs = comando.executeQuery()) {
                return rs.next() && deletar(rs.getInt("id_cd"));
            }
        } catch (SQLException erro) {
            System.out.println("erro ao deletar CD por CNPJ: " + erro.getMessage());
            return false;
        }
    }
}