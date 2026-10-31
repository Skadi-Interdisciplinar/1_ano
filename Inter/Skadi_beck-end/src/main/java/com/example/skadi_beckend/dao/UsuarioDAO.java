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
 - Classe DAO para manipular as operações da tabela Usuario
 */
public class UsuarioDAO {

//  Inserir
    // Admin pode existir sem CD; funcionários também podem ser inseridos sem credenciais
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
            if (usuario.getIdCd() == null) comando.setNull(9, java.sql.Types.INTEGER);
            else comando.setInt(9, usuario.getIdCd());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao inserir usuario: " + erro.getMessage());
            return false;
        }
    }

    // Listar todos
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
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
                        (Integer) rs.getObject("id_cd"),
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
        String sql = "SELECT u.id_usuario, u.username, u.cod_gestor, u.nome, u.cpf, u.email, NULL AS senha, " +
                "u.cargo, u.nivel_acesso, u.id_cd, u.data_atualizacao, c.nome AS nome_cd " +
                "FROM Usuario u " +
                "LEFT JOIN CD c ON u.id_cd = c.id_cd " +
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
                        (Integer) rs.getObject("id_cd"),
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
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
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
                            (Integer) rs.getObject("id_cd"),
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
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
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
                            (Integer) rs.getObject("id_cd"),
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

//   Buscar por ID do CD
    public List<Usuario> buscarPorIdCd(int idCd) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "select id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, cargo, nivel_acesso, id_cd, data_atualizacao\n" +
                "from Usuario\n" +
                "where id_cd = ?\n" +
                "order by nome asc\n";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setInt(1, idCd);

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
                            (Integer) rs.getObject("id_cd"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    lista.add(usuario);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar usuario por ID do CD: " + erro.getMessage());
        }
        return lista;
    }

//   Buscar usuários pelo nome do CD
    public List<Usuario> buscarPorNomeCd(String nomeCd) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.id_usuario, u.username, u.cod_gestor, u.nome, u.cpf, u.email, NULL AS senha, " +
                "u.cargo, u.nivel_acesso, u.id_cd, u.data_atualizacao, c.nome AS nome_cd " +
                "FROM Usuario u " +
                "LEFT JOIN CD c ON u.id_cd = c.id_cd " +
                "WHERE lower(c.nome) LIKE lower(?) " +
                "ORDER BY u.nome ASC";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql);
        ) {
            comando.setString(1, "%" + nomeCd + "%");

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
                            (Integer) rs.getObject("id_cd"),
                            rs.getTimestamp("data_atualizacao")
                    );
                    usuario.setNomeCd(rs.getString("nome_cd"));
                    lista.add(usuario);
                }
            }
        } catch (SQLException erro) {
            System.out.println("erro ao buscar usuario pelo nome do CD: " + erro.getMessage());
        }
        return lista;
    }

//  Atualizar
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
            if (usuario.getIdCd() == null) comando.setNull(8, java.sql.Types.INTEGER);
            else comando.setInt(8, usuario.getIdCd());
            comando.setInt(9, usuario.getIdUsuario());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException erro) {
            System.out.println("erro ao atualizar usuario: " + erro.getMessage());
            return false;
        }
    }

    // Atualizar senha
    public boolean atualizarSenha(String email, String novaSenha) {
        // Recuperação só altera contas que podem acessar o sistema
        String sql = "UPDATE Usuario SET senha = ? WHERE email = ? " +
                "AND lower(nivel_acesso) IN ('admin', 'super_admin')";

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
                "where id_usuario = ? and lower(nivel_acesso) <> 'super_admin'\n";

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
                "where cpf = ? and lower(nivel_acesso) <> 'super_admin'\n";

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

    // Lista somente gestores e operadores da CD do administrador
    public List<Usuario> listarFuncionariosPorCd(int idCd, String trechoNome) {
        List<Usuario> lista = new ArrayList<>();
        boolean filtrarNome = trechoNome != null && !trechoNome.trim().isEmpty();
        String sql = "SELECT u.id_usuario, u.username, u.cod_gestor, u.nome, u.cpf, u.email, NULL AS senha, " +
                "u.cargo, u.nivel_acesso, u.id_cd, u.data_atualizacao, c.nome AS nome_cd " +
                "FROM Usuario u JOIN CD c ON u.id_cd = c.id_cd " +
                "WHERE u.id_cd = ? AND lower(u.nivel_acesso) IN ('gestor', 'operador') " +
                (filtrarNome ? "AND lower(u.nome) LIKE lower(?) " : "") + "ORDER BY u.nome ASC";

        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idCd);
            if (filtrarNome) comando.setString(2, "%" + trechoNome.trim() + "%");
            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = mapearUsuario(rs);
                    usuario.setNomeCd(rs.getString("nome_cd"));
                    lista.add(usuario);
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao listar funcionários da CD: " + erro.getMessage());
        }
        return lista;
    }

    // O formulário só precisa dos nomes dos gestores
    public List<Usuario> listarGestores() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, " +
                "cargo, nivel_acesso, id_cd, data_atualizacao FROM Usuario " +
                "WHERE lower(nivel_acesso) = 'gestor' ORDER BY nome ASC";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {
            while (rs.next()) lista.add(mapearUsuario(rs));
        } catch (SQLException erro) {
            System.out.println("Erro ao listar gestores: " + erro.getMessage());
        }
        return lista;
    }

    // Busca funcionário somente se ele pertencer à CD indicada
    public Usuario buscarFuncionarioPorIdECd(int idUsuario, int idCd) {
        String sql = "SELECT id_usuario, username, cod_gestor, nome, cpf, email, NULL AS senha, " +
                "cargo, nivel_acesso, id_cd, data_atualizacao FROM Usuario " +
                "WHERE id_usuario = ? AND id_cd = ? " +
                "AND lower(nivel_acesso) IN ('gestor', 'operador')";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            comando.setInt(2, idCd);
            try (ResultSet rs = comando.executeQuery()) {
                return rs.next() ? mapearUsuario(rs) : null;
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao buscar funcionário da CD: " + erro.getMessage());
            return null;
        }
    }

    // Atualização feita por admin mantém username e senha nulos e não permite trocar de CD
    public boolean atualizarFuncionario(Usuario usuario) {
        String sql = "UPDATE Usuario SET username = NULL, senha = NULL, cod_gestor = ?, nome = ?, cpf = ?, email = ?, " +
                "cargo = ?, nivel_acesso = ? WHERE id_usuario = ? AND id_cd = ? " +
                "AND lower(nivel_acesso) IN ('gestor', 'operador')";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            if (usuario.getCodGestor() == null) comando.setNull(1, java.sql.Types.INTEGER);
            else comando.setInt(1, usuario.getCodGestor());
            comando.setString(2, usuario.getNome());
            comando.setString(3, usuario.getCpf());
            comando.setString(4, usuario.getEmail());
            comando.setString(5, usuario.getCargo());
            comando.setString(6, usuario.getNivelAcesso());
            comando.setInt(7, usuario.getIdUsuario());
            comando.setInt(8, usuario.getIdCd());
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar funcionário da CD: " + erro.getMessage());
            return false;
        }
    }

    // super_admin pode mover funcionário entre CDs, mantendo o cadastro sem login
    public boolean atualizarFuncionarioPorSuperAdmin(Usuario usuario) {
        String sql = "UPDATE Usuario SET username = NULL, senha = NULL, cod_gestor = ?, nome = ?, cpf = ?, email = ?, " +
                "cargo = ?, nivel_acesso = ?, id_cd = ? WHERE id_usuario = ? " +
                "AND lower(nivel_acesso) IN ('gestor', 'operador')";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            if (usuario.getCodGestor() == null) comando.setNull(1, java.sql.Types.INTEGER);
            else comando.setInt(1, usuario.getCodGestor());
            comando.setString(2, usuario.getNome());
            comando.setString(3, usuario.getCpf());
            comando.setString(4, usuario.getEmail());
            comando.setString(5, usuario.getCargo());
            comando.setString(6, usuario.getNivelAcesso());
            if (usuario.getIdCd() == null) comando.setNull(7, java.sql.Types.INTEGER);
            else comando.setInt(7, usuario.getIdCd());
            comando.setInt(8, usuario.getIdUsuario());
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar funcionário pelo super_admin: " + erro.getMessage());
            return false;
        }
    }

    // Restringe exclusão de funcionários à CD associada ao administrador
    public boolean deletarFuncionario(int idUsuario, int idCd) {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ? AND id_cd = ? " +
                "AND lower(nivel_acesso) IN ('gestor', 'operador')";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setInt(1, idUsuario);
            comando.setInt(2, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao excluir funcionário da CD: " + erro.getMessage());
            return false;
        }
    }

    public boolean deletarFuncionarioPorCpf(String cpf, int idCd) {
        String sql = "DELETE FROM Usuario WHERE cpf = ? AND id_cd = ? " +
                "AND lower(nivel_acesso) IN ('gestor', 'operador')";
        try (Connection conexao = ConnectionFactory.getConnection();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, cpf);
            comando.setInt(2, idCd);
            return comando.executeUpdate() > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao excluir funcionário por CPF: " + erro.getMessage());
            return false;
        }
    }

    // A CD e sua associação ao admin são gravadas na mesma transação
    public Integer criarCdParaAdministrador(int idUsuario, String nomeCd, String cnpj) {
        String sqlCd = "INSERT INTO CD (nome, cnpj) VALUES (?, ?) RETURNING id_cd";
        String sqlUsuario = "UPDATE Usuario SET id_cd = ? WHERE id_usuario = ? " +
                "AND lower(nivel_acesso) = 'admin' AND id_cd IS NULL";

        try (Connection conexao = ConnectionFactory.getConnection()) {
            boolean autoCommitOriginal = conexao.getAutoCommit();
            conexao.setAutoCommit(false);
            try {
                int idCd;
                try (PreparedStatement inserirCd = conexao.prepareStatement(sqlCd)) {
                    inserirCd.setString(1, nomeCd);
                    inserirCd.setString(2, cnpj);
                    try (ResultSet rs = inserirCd.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Não foi possível criar a CD.");
                        idCd = rs.getInt("id_cd");
                    }
                }

                try (PreparedStatement associar = conexao.prepareStatement(sqlUsuario)) {
                    associar.setInt(1, idCd);
                    associar.setInt(2, idUsuario);
                    if (associar.executeUpdate() == 0) {
                        conexao.rollback();
                        return null;
                    }
                }

                conexao.commit();
                return idCd;
            } catch (SQLException erro) {
                conexao.rollback();
                throw erro;
            } finally {
                conexao.setAutoCommit(autoCommitOriginal);
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao criar CD para administrador: " + erro.getMessage());
            return null;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getInt("id_usuario"), rs.getString("username"), (Integer) rs.getObject("cod_gestor"),
                rs.getString("nome"), rs.getString("cpf"), rs.getString("email"), rs.getString("senha"),
                rs.getString("cargo"), rs.getString("nivel_acesso"), (Integer) rs.getObject("id_cd"),
                rs.getTimestamp("data_atualizacao"));
    }

    // Autenticar
    public Usuario autenticar(String login, String senhaDigitada) {

        // Gestores e operadores não entram no sistema, mesmo que tenham username cadastrado
        String sql = "SELECT id_usuario, username, cod_gestor, nome, cpf, email, senha, cargo, nivel_acesso, id_cd, data_atualizacao " +
                "FROM Usuario " +
                "WHERE (email = ? OR username = ? OR cpf = ?) " +
                "AND lower(nivel_acesso) IN ('admin', 'super_admin') AND senha IS NOT NULL";

        try (
                Connection conexao = ConnectionFactory.getConnection();
                PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setString(1, login); // Email
            comando.setString(2, login); // Username
            comando.setString(3, login); // CPF

            try (ResultSet rs = comando.executeQuery()) {
//              Se encontrou um usuário com esse login
                if (rs.next()) {
                    String hashDoBanco = rs.getString("senha");

//                  Compara a senha digitada com a do banco
                    if (hashDoBanco != null && senhaDigitada != null
                            && org.mindrot.jbcrypt.BCrypt.checkpw(senhaDigitada, hashDoBanco)) {
                        Integer codGestor = (Integer) rs.getObject("cod_gestor");

                        return new Usuario(
                                rs.getInt("id_usuario"),
                                rs.getString("username"),
                                codGestor,
                                rs.getString("nome"),
                                rs.getString("cpf"),
                                rs.getString("email"),
                                hashDoBanco,
                                rs.getString("cargo"),
                                rs.getString("nivel_acesso"),
                                (Integer) rs.getObject("id_cd"),
                                rs.getTimestamp("data_atualizacao")
                        );
                    }
                }
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao autenticar usuário: " + erro.getMessage());
        }

        return null;
    }

    // Verificar se o e-mail existe
    public boolean verificarEmailExiste(String email) {
        // Recuperação de senha é oferecida somente para contas administrativas
        String sql = "SELECT id_usuario FROM Usuario WHERE email = ? " +
                "AND lower(nivel_acesso) IN ('admin', 'super_admin')";

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
