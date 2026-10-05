package com.example.skadi_beckend.model;

import java.sql.Date;

public class Produto {
//    Atributos
    private int id_produto;
    private String nome;
    private String categoria;
    private double temperatura_ideal;
    private Date validade;
    private Integer tempo_sobrevivencia;
    private int id_refrigerator;

//    Construtor vazio
    public Produto() {}

//    Construtor
    public Produto(int id_produto, String nome, String categoria, double temperatura_ideal, Date validade, Integer tempo_sobrevivencia, int id_refrigerador) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.categoria = categoria;
        this.temperatura_ideal = temperatura_ideal;
        this.validade = validade;
        this.tempo_sobrevivencia = tempo_sobrevivencia;
        this.id_refrigerator = id_refrigerador;
    }

    // Getters e Setters
    public int getId_produto() { return id_produto; }
    public void setId_produto(int id_produto) { this.id_produto = id_produto; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public double getTemperatura_ideal() { return temperatura_ideal; }
    public void setTemperatura_ideal(double temperatura_ideal) { this.temperatura_ideal = temperatura_ideal; }

    public Date getValidade() { return validade; }
    public void setValidade(Date validade) { this.validade = validade; }

    public Integer getTempo_sobrevivencia() { return tempo_sobrevivencia; }
    public void setTempo_sobrevivencia(Integer tempo_sobrevivencia) { this.tempo_sobrevivencia = tempo_sobrevivencia; }

    public int getId_refrigerador() { return id_refrigerator; }
    public void setId_refrigerador(int id_refrigerador) { this.id_refrigerator = id_refrigerador; }

}