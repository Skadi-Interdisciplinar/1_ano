package com.example.skadi_beckend.model;

import java.sql.Timestamp;

/**
 - Classe de modelo que representa a entidade Frigorifico.

 */
public class Frigorifico {

    private int idFrigorifico;
    private String nome;
    private String localizacao;
    private int idCd;
    private double temperaturaMax;
    private double temperaturaMin;
    private Timestamp dataAtualizacao;

    //    Construtor vazio
    public Frigorifico() {
    }

    //    Construtor sem ID frigorifico
    public Frigorifico(String nome, String localizacao, int idCd, double temperaturaMax, double temperaturaMin) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.idCd = idCd;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
    }

    //   Construtor
    public Frigorifico(int idFrigorifico, String nome, String localizacao, int idCd, double temperaturaMax, double temperaturaMin) {
        this.idFrigorifico = idFrigorifico;
        this.nome = nome;
        this.localizacao = localizacao;
        this.idCd = idCd;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
    }

    //    Construtor
    public Frigorifico(int idFrigorifico, String nome, String localizacao, int idCd, double temperaturaMax, double temperaturaMin, Timestamp dataAtualizacao) {
        this.idFrigorifico = idFrigorifico;
        this.nome = nome;
        this.localizacao = localizacao;
        this.idCd = idCd;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
        this.dataAtualizacao = dataAtualizacao;
    }

    // GETTERS E SETTERS

    public int getIdFrigorifico() {
        return idFrigorifico;
    }

    public void setIdFrigorifico(int idFrigorifico) {
        this.idFrigorifico = idFrigorifico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Double getTemperaturaMax() {return temperaturaMax; }

    public void setTemperaturaMax(Double temperaturaMax) { this.temperaturaMax = temperaturaMax; }

    public Double getTemperaturaMin() { return temperaturaMin; }

    public void setTemperaturaMin(Double temperaturaMin) { this.temperaturaMin = temperaturaMin; }

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

    @Override
    public String toString() {
        return "Frigorifico " + idFrigorifico + " - " + nome + " (CD ID: " + idCd + ")";
    }
}