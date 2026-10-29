package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Exclusão "de verdade": apaga o registro mesmo que ele esteja vinculado a outras tabelas.
 *
 * As chaves estrangeiras do banco usam RESTRICT em vários pontos (CD -> Usuario/Frigorifico,
 * Frigorifico -> Lote_Frigorifico, Termometro -> LeituraTemperatura, Categoria -> Lote...).
 * Por isso os registros dependentes são apagados primeiro, tudo dentro de uma única transação:
 * se qualquer passo falhar, nada é apagado (rollback).
 */
public final class ExclusaoEmCascata {

    private ExclusaoEmCascata() {
    }

    /** Um passo de exclusão que usa a conexão da transação e devolve as linhas apagadas do registro principal. */
    private interface Passo {
        int executar(Connection conexao) throws SQLException;
    }

    public static boolean termometro(int idTermometro) {
        return transacao("termometro", conexao -> termometro(conexao, idTermometro));
    }

    public static boolean frigorifico(int idFrigorifico) {
        return transacao("frigorifico", conexao -> frigorifico(conexao, idFrigorifico));
    }

    public static boolean lote(int idLote) {
        // Lote_Frigorifico tem ON DELETE CASCADE para o lote
        return transacao("lote", conexao -> apagar(conexao, "delete from Lote where id_lote = ?", idLote));
    }

    public static boolean categoria(int idCategoria) {
        return transacao("categoria", conexao -> {
            apagar(conexao, "delete from Lote where id_categoria = ?", idCategoria);
            return apagar(conexao, "delete from Categoria where id_categoria = ?", idCategoria);
        });
    }

    public static boolean usuario(int idUsuario) {
        return transacao("usuario", conexao -> usuario(conexao, idUsuario));
    }

    public static boolean endereco(int idEndereco) {
        return transacao("endereco", conexao -> apagar(conexao, "delete from Endereco where id_endereco = ?", idEndereco));
    }

    public static boolean cd(int idCd) {
        return transacao("CD", conexao -> {
            for (int idFrigorifico : ids(conexao, "select id_frigorifico from Frigorifico where id_cd = ?", idCd)) {
                frigorifico(conexao, idFrigorifico);
            }
            for (int idUsuario : ids(conexao, "select id_usuario from Usuario where id_cd = ?", idCd)) {
                usuario(conexao, idUsuario);
            }
            apagar(conexao, "delete from admin_cd where cod_cd = ?", idCd);
            apagar(conexao, "delete from Endereco where id_cd = ?", idCd);
            return apagar(conexao, "delete from CD where id_cd = ?", idCd);
        });
    }

    // ----- passos reutilizados -----

    private static int termometro(Connection conexao, int idTermometro) throws SQLException {
        // Alerta e NotificacaoAlerta caem junto (ON DELETE CASCADE a partir da leitura)
        apagar(conexao, "delete from LeituraTemperatura where id_termometro = ?", idTermometro);
        return apagar(conexao, "delete from Termometro where id_termometro = ?", idTermometro);
    }

    private static int frigorifico(Connection conexao, int idFrigorifico) throws SQLException {
        apagar(conexao,
                "delete from LeituraTemperatura where id_termometro in " +
                "(select id_termometro from Termometro where id_frigorifico = ?)", idFrigorifico);
        apagar(conexao, "delete from Termometro where id_frigorifico = ?", idFrigorifico);
        apagar(conexao, "delete from lote_frigorifico where id_frigorifico = ?", idFrigorifico);
        return apagar(conexao, "delete from Frigorifico where id_frigorifico = ?", idFrigorifico);
    }

    private static int usuario(Connection conexao, int idUsuario) throws SQLException {
        // Usuario.cod_gestor e Alerta.id_usuario usam SET NULL; os outros dois precisam ser apagados antes
        apagar(conexao, "delete from admin_cd where cod_admin = ?", idUsuario);
        apagar(conexao, "delete from NotificacaoAlerta where id_usuario = ?", idUsuario);
        return apagar(conexao, "delete from Usuario where id_usuario = ?", idUsuario);
    }

    // ----- utilitários -----

    private static boolean transacao(String nome, Passo passo) {
        try (Connection conexao = ConnectionFactory.getConnection()) {
            conexao.setAutoCommit(false);
            try {
                int apagados = passo.executar(conexao);
                if (apagados > 0) {
                    conexao.commit();
                    return true;
                }
                conexao.rollback();
                return false;
            } catch (SQLException erro) {
                conexao.rollback();
                System.out.println("erro ao deletar " + nome + " em cascata: " + erro.getMessage());
                return false;
            }
        } catch (SQLException erro) {
            System.out.println("erro de conexão ao deletar " + nome + ": " + erro.getMessage());
            return false;
        }
    }

    private static int apagar(Connection conexao, String sql, int id) throws SQLException {
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            return comando.executeUpdate();
        }
    }

    private static List<Integer> ids(Connection conexao, String sql, int id) throws SQLException {
        List<Integer> lista = new ArrayList<>();
        try (PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, id);
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt(1));
                }
            }
        }
        return lista;
    }
}
