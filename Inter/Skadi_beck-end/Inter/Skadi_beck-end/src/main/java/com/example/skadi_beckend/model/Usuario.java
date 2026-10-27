package com.example.skadi_beckend.model;

import java.sql.Timestamp;

/**
 * Classe de modelo que representa a entidade Usuario.
 */
public class Usuario {

    private int idUsuario;
    private String username;
    private Integer codGestor;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String cargo;
    private String nivelAcesso;
    private int idCd;
    private Timestamp dataAtualizacao;
    // Atributo adicionado para receber o nome da CD
    private String nomeCd;

    // Construtor
    public Usuario() {
    }

    // Construtor sem ID
    public Usuario(String username, Integer codGestor, String nome, String cpf,
                   String email, String senha, String cargo, String nivelAcesso, int idCd) {
        this.username = username;
        this.codGestor = codGestor;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.nivelAcesso = nivelAcesso;
        this.idCd = idCd;
    }

    // Construtor para atualizar dados
    public Usuario(int idUsuario, String username, Integer codGestor, String nome,
                   String cpf, String email, String senha, String cargo, String nivelAcesso, int idCd) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.codGestor = codGestor;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.nivelAcesso = nivelAcesso;
        this.idCd = idCd;
    }

    // Construtor
    public Usuario(int idUsuario, String username, Integer codGestor, String nome, String cpf,
                   String email, String senha, String cargo, String nivelAcesso, int idCd, Timestamp dataAtualizacao) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.codGestor = codGestor;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cargo = cargo;
        this.nivelAcesso = nivelAcesso;
        this.idCd = idCd;
        this.dataAtualizacao = dataAtualizacao;
    }

    // GETTERS E SETTERS

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCodGestor() {
        return codGestor;
    }

    public void setCodGestor(Integer codGestor) {
        this.codGestor = codGestor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(String nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public int getIdCd() {
        return idCd;
    }

    public void setIdCd(int idCd) {
        this.idCd = idCd;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getNomeCd() {
        return nomeCd;
    }

    public void setNomeCd(String nomeCd) {
        this.nomeCd = nomeCd;
    }

    @Override
    public String toString() {
        return "Usuario " + idUsuario + " - " + nome + " (" + username + " - " + nivelAcesso + ")";
    }
}