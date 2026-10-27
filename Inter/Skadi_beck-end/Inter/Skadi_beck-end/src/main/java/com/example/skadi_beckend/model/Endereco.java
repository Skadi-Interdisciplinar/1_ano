package com.example.skadi_beckend.model;

import java.sql.Timestamp;

/**
 * Classe de modelo que representa a entidade Endereco.
 */
public class Endereco {

    private int idEndereco;
    private int idCd;
    private String rua;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private Timestamp dataAtualizacao;

    // Atributo para receber o nome de cd
    private String nomeCd;

    // Construtor vazio
    public Endereco() {
    }

//   Construtor sem ID e timestamp
    public Endereco(int idCd, String rua, Integer numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.idCd = idCd;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

//    Construtor sem timestamp
    public Endereco(int idEndereco, int idCd, String rua, Integer numero, String complemento, String bairro, String cidade, String estado, String cep) {
        this.idEndereco = idEndereco;
        this.idCd = idCd;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }


//     Construtor
    public Endereco(int idEndereco, int idCd, String rua, Integer numero, String complemento, String bairro, String cidade, String estado, String cep, Timestamp dataAtualizacao) {
        this.idEndereco = idEndereco;
        this.idCd = idCd;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.dataAtualizacao = dataAtualizacao;
    }

    // --- GETTERS E SETTERS ---

    public int getIdEndereco() { return idEndereco; }
    public void setIdEndereco(int idEndereco) { this.idEndereco = idEndereco; }

    public int getIdCd() { return idCd; }
    public void setIdCd(int idCd) { this.idCd = idCd; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public Timestamp getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(Timestamp dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    public String getNomeCd() { return nomeCd; }
    public void setNomeCd(String nomeCd) { this.nomeCd = nomeCd; }

    @Override
    public String toString() {
        return "Endereco " + idEndereco + " - " + rua + ", " + numero + " (CD ID: " + idCd + ")";
    }
}