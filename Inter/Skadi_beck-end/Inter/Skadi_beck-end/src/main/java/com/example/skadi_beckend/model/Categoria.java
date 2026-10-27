package com.example.skadi_beckend.model;

import java.sql.Timestamp;
/*
* Classe modelo que representa a entidade Categoria
*/

public class Categoria {


    private int idCategoria;
    private String nome;
    private String vidaUtilHoras;
    private double temperaturaMax;
    private double temperaturaMin;
    private Timestamp dataAtualizacao;

//    Construtor vazio
    public Categoria() {}

//    Construtor sem ID e sem Timestamp
    public Categoria(String nome, String vidaUtilHoras, double temperaturaMax, double temperaturaMin) {
        this.nome = nome;
        this.vidaUtilHoras = vidaUtilHoras;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
    }

//   Construtor
    public Categoria(int idCategoria, String nome, String vidaUtilHoras, double temperaturaMax, double temperaturaMin, Timestamp dataAtualizacao) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.vidaUtilHoras = vidaUtilHoras;
        this.temperaturaMax = temperaturaMax;
        this.temperaturaMin = temperaturaMin;
        this.dataAtualizacao = dataAtualizacao;
    }

    // GETTERS E SETTERS

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getVidaUtilHoras() {
        return vidaUtilHoras;
    }

    public void setVidaUtilHoras(String vidaUtilHoras) {
        this.vidaUtilHoras = vidaUtilHoras;
    }

    public double getTemperaturaMax() {
        return temperaturaMax;
    }

    public void setTemperaturaMax(double temperaturaMax) {
        this.temperaturaMax = temperaturaMax;
    }

    public double getTemperaturaMin() {
        return temperaturaMin;
    }

    public void setTemperaturaMin(double temperaturaMin) {
        this.temperaturaMin = temperaturaMin;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Categoria " + idCategoria + " - " + nome + " (Min: " + temperaturaMin + " / Max: " + temperaturaMax + " / Vida Útil: " + vidaUtilHoras + ")";
    }
}