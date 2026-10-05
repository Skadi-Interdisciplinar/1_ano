package com.example.skadi_beckend.model;

public class Frigorifico {
    //    Atributos
    private int id_frigorifico;
    private String nome;
    private String localizacao;
    private double temperatura_min;
    private double temperatura_max;
    private int id_cd;

    //    Construtor vazio
    public Frigorifico() {}

    //    Construtor com ID usar nas buscas do banco
    public Frigorifico(int id_frigorifico, String nome, String localizacao, double temperatura_min, double temperatura_max, int id_cd) {
        this.id_frigorifico = id_frigorifico;
        this.nome = nome;
        this.localizacao = localizacao;
        this.temperatura_min = temperatura_min;
        this.temperatura_max = temperatura_max;
        this.id_cd = id_cd;
    }

    //    Construtor sem ID - usar na hora de inserir dados novos
    public Frigorifico(String nome, String localizacao, double temperatura_min, double temperatura_max, int id_cd) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.temperatura_min = temperatura_min;
        this.temperatura_max = temperatura_max;
        this.id_cd = id_cd;
    }


    //    Getters e Setters
    public int getId_frigorifico() { return id_frigorifico; }
    public void setId_frigorifico(int id_frigorifico) { this.id_frigorifico = id_frigorifico; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public double getTemperatura_min() { return temperatura_min; }
    public void setTemperatura_min(double temperatura_min) { this.temperatura_min = temperatura_min; }

    public double getTemperatura_max() { return temperatura_max; }
    public void setTemperatura_max(double temperatura_max) { this.temperatura_max = temperatura_max; }

    public int getId_cd() { return id_cd; }
    public void setId_cd(int id_cd) { this.id_cd = id_cd; }
}