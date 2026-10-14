package com.example.skadi_beckend.model;

import java.sql.Timestamp;

/**
 *  Classe de modelo que representa a entidade Termometro
 */
public class Termometro {
    private int idTermometro;
    private String modelo;
    private String status;
    private int idFrigorifico;
    private Timestamp dataAtualizacao;

    // Construtor vazio
    public Termometro() {}

    // Construtor sem ID e sem Timestamp
    public Termometro(String modelo, String status, int idFrigorifico) {
        this.modelo = modelo;
        this.status = status;
        this.idFrigorifico = idFrigorifico;
    }

    // Construtor
    public Termometro(int idTermometro, String modelo, String status, int idFrigorifico, Timestamp dataAtualizacao) {
        this.idTermometro = idTermometro;
        this.modelo = modelo;
        this.status = status;
        this.idFrigorifico = idFrigorifico;
        this.dataAtualizacao = dataAtualizacao;
    }

    // GETTERS E SETTERS

    public int getIdTermometro() {
        return idTermometro;
    }

    public void setIdTermometro(int idTermometro) {
        this.idTermometro = idTermometro;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdFrigorifico() {
        return idFrigorifico;
    }

    public void setIdFrigorifico(int idFrigorifico) {
        this.idFrigorifico = idFrigorifico;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }


    @Override
    public String toString() {
        return "Termometro " + idTermometro + " - " + modelo + " (" + status + " - Frigorifico ID: " + idFrigorifico + ")";
    }
}