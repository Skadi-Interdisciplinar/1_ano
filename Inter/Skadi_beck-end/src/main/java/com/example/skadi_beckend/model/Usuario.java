package com.example.skadi_beckend.model;

public class Usuario {
//    Atributos
    private int id_usuario;
    private String nome;
    private String cpf;
    private String email;
    private String cargo;
    private String nivel_acesso;
    private int id_cd;

//    Construtor de vazio para Gson
    public Usuario() {}

//    Construtor
    public Usuario(int id_usuario, String nome, String cpf, String email, String cargo, String nivel_acesso, int id_cd) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.nivel_acesso = nivel_acesso;
        this.id_cd = id_cd;
    }

    public Usuario(String nome, String cpf, String email, String cargo, String nivel_acesso, int id_cd) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.cargo = cargo;
        this.nivel_acesso = nivel_acesso;
        this.id_cd = id_cd;
    }

//   Getters e Setters
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getNivel_acesso() { return nivel_acesso; }
    public void setNivel_acesso(String nivel_acesso) { this.nivel_acesso = nivel_acesso; }

    public int getId_cd() { return id_cd; }
    public void setId_cd(int id_cd) { this.id_cd = id_cd; }
}