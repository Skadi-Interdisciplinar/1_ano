package com.example.skadi_beckend.dao;

import com.example.skadi_beckend.connection.ConnectionFactory;
import com.example.skadi_beckend.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para manipular as operações da tabela Usuario
 */
public class UsuarioDAO {

    // Inserir
    public boolean inserir(Usuario usuario) {
        String sql = "insert into Usuario(\n" +
                "username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, usuario.getUsername());

            if (usuario.getCodGestor() == null) {
                comando.setNull(2, java.sql.Types.INTEGER);
            } else {
                comando.setInt(2, usuario.getCodGestor());
            }

            comando.setString(3, usuario.getNome());
            comando.setString(4, usuario.getCpf());
            comando.setString(5, usuario.getEmail());
            comando.setString(6, usuario.getSenha());
            comando.setString(7, usuario.getCargo());
            comando.setString(8, usuario.getNivelAcesso());
            comando.setInt(9, usuario.getIdCd());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao inserir usuario: " + erro.getMessage());
            return false;
        }
    }

    // Listar todos simples
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
                "from Usuario\n" +
                "order by id_usuario asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                Integer codGestor = (Integer) rs.getObject("cod_gestor");

                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        codGestor,
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cargo"),
                        rs.getString("nivel_acesso"),
                        rs.getInt("id_cd"),
                        rs.getTimestamp("data_atualizacao")
                );
                lista.add(usuario);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar usuarios: " + erro.getMessage());
        }
        return lista;
    }

    // Listar usuários trazendo o nome do cd
    public List<Usuario> listarUsuariosComNomeCD() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.cod_gestor, u.nome, u.cpf, u.email, u.senha, " +
                "u.cargo, u.nivel_acesso, u.id_cd, u.data_atualizacao, c.nome AS nome_cd " +
                "FROM Usuario u " +
                "INNER JOIN CD c ON u.id_cd = c.id_cd " +
                "ORDER BY u.id_usuario ASC";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
                ResultSet rs = comando.executeQuery();
        ) {
            while (rs.next()) {
                Integer codGestor = (Integer) rs.getObject("cod_gestor");

                Usuario usuario = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        codGestor,
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email"),
                        rs.getString("senha"),
                        rs.getString("cargo"),
                        rs.getString("nivel_acesso"),
                        rs.getInt("id_cd"),
                        rs.getTimestamp("data_atualizacao")
                );
                usuario.setNomeCd(rs.getString("nome_cd"));
                lista.add(usuario);
            }
        } catch (SQLException erro) {
            System.out.println("erro ao listar usuarios com nome do CD: " + erro.getMessage());
        }
        return lista;
    }

    // Buscar por ID
    public Usuario buscarPorId(int idUsuario) {
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
                "from Usuario\n" +
                "where id_usuario = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idUsuario);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    Integer codGestor = (Integer) rs.getObject("cod_gestor");

                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            codGestor,
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("cargo"),
                            rs.getString("nivel_acesso"),
                            rs.getInt("id_cd"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar usuario por id: " + erro.getMessage());
        }
        return null;
    }

    // Buscar por nome
    public List<Usuario> buscarPorNome(String trecho) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
                "from Usuario\n" +
                "where lower(nome) like lower(?)\n" +
                "order by nome asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, "%" + trecho + "%");

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Integer codGestor = (Integer) rs.getObject("cod_gestor");

                    Usuario usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            codGestor,
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("cargo"),
                            rs.getString("nivel_acesso"),
                            rs.getInt("id_cd"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(usuario);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar usuario por nome: " + erro.getMessage());
        }
        return lista;
    }

    // Atualizar
    public boolean atualizar(Usuario usuario) {
        String sql = "update Usuario\n" +
                "set username = ?, cod_gestor = ?, nome = ?, cpf = ?, email = ?, cargo = ?, nivel_acesso = ?, id_cd = ?\n" +
                "where id_usuario = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, usuario.getUsername());

            if (usuario.getCodGestor() == null) {
                comando.setNull(2, java.sql.Types.INTEGER);
            } else {
                comando.setInt(2, usuario.getCodGestor());
            }

            comando.setString(3, usuario.getNome());
            comando.setString(4, usuario.getCpf());
            comando.setString(5, usuario.getEmail());
            comando.setString(6, usuario.getCargo());
            comando.setString(7, usuario.getNivelAcesso());
            comando.setInt(8, usuario.getIdCd());
            comando.setInt(9, usuario.getIdUsuario());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar usuario: " + erro.getMessage());
            return false;
        }
    }
//   Atualizar senha
    public boolean atualizarSenha(String email, String novaSenha) {
        String sql = "UPDATE Usuario SET senha = ? WHERE email = ?";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, novaSenha); // A nova senha
            comando.setString(2, email);

            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 0;

        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar a senha: " + erro.getMessage());
            return false;
        }
    }

    // Deletar por ID
    public boolean deletar(int idUsuario) {
        String sql = "delete from Usuario\n" +
                "where id_usuario = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idUsuario);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar usuario: " + erro.getMessage());
            return false;
        }
    }

    // Deletar por CPF
    public boolean deletarPorCpf(String cpf) {
        String sql = "delete from Usuario\n" +
                "where cpf = ?\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, cpf);

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao deletar usuario por CPF: " + erro.getMessage());
            return false;
        }
    }

//   Autenticar
    public Usuario autenticar(String login, String senha) {
        String sql = "SELECT id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd, data_atualizacao " +
                "FROM Usuario " +
                "WHERE (email = ? OR username = ? OR cpf = ?) AND senha = ?";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, login); // Email
            comando.setString(2, login); // Username
            comando.setString(3, login); // CPF
            comando.setString(4, senha); // Verificar senha

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    Integer codGestor = (Integer) rs.getObject("cod_gestor");

                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("username"),
                            codGestor,
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email"),
                            rs.getString("senha"),
                            rs.getString("cargo"),
                            rs.getString("nivel_acesso"),
                            rs.getInt("id_cd"),
                            rs.getTimestamp("data_atualizacao")
                    );
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao autenticar usuário: " + erro.getMessage());
        }

        return null;
    }

    // Verificar se o e-mail existe
    public boolean verificarEmailExiste(String email) {
        String sql = "SELECT id_usuario FROM Usuario WHERE email = ?";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, email);

            try (ResultSet rs = comando.executeQuery()) {

                return rs.next();
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao verificar e-mail: " + erro.getMessage());
            return false;
        }
    }

}