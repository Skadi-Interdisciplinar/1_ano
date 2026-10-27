package com.example.skadi_beckend.model;

import java.sql.Timestamp;

public class CD {
    private int idCd;
    private String nome;
    private String cnpj;
    private Timestamp dataAtualizacao;
/*
* Classe modelo que representa CD (Centro de Distribuição)
*/
    // Construtor
    public CD(int idCd, String nome, String cnpj, Timestamp dataAtualizacao) {
        this.idCd = idCd;
        this.nome = nome;
        this.cnpj = cnpj;
        this.dataAtualizacao = dataAtualizacao;
    }
//    Construtor para atualização de dados
    public CD(int idCd, String nome, String cnpj) {
        this.idCd = idCd;
        this.nome = nome;
        this.cnpj = cnpj;
    }

    // Construtor sem ID e sem Data
    public CD(String nome, String cnpj) {
        this.nome = nome;
        this.cnpj = cnpj;
    }

    public int getIdCd() {
        return idCd;
    }

    public void setIdCd(int idCd) {
        this.idCd = idCd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "CD " + idCd + " " + nome + " " + cnpj;
    }
}