package com.example.skadi_beckend.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Classe modelo que representa a entidade Lote
 */
public class Lote {
    private int idLote;
    private String codigoLote;
    private int idCategoria;
    private Date dataFabricacao;
    private Date dataValidade;
    private String status;
    private Timestamp dataAtualizacao;
    // null em lotes antigos até serem associados; lotes novos do admin recebem sua CD.
    private Integer idCd;

//    Construtor vazio
    public Lote() {}

//    Construtor sem ID e sem Timestamp
    public Lote(String codigoLote, int idCategoria, Date dataFabricacao, Date dataValidade, String status) {
        this.codigoLote = codigoLote;
        this.idCategoria = idCategoria;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.status = status;
    }

//    Construtor
    public Lote(int idLote, String codigoLote, int idCategoria, Date dataFabricacao, Date dataValidade, String status, Timestamp dataAtualizacao) {
        this.idLote = idLote;
        this.codigoLote = codigoLote;
        this.idCategoria = idCategoria;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.status = status;
        this.dataAtualizacao = dataAtualizacao;
    }

    // GETTERS E SETTERS

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(String codigoLote) {
        this.codigoLote = codigoLote;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Date getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(Date dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getIdCd() {
        return idCd;
    }

    public void setIdCd(Integer idCd) {
        this.idCd = idCd;
    }

    @Override
    public String toString() {
        return "Lote " + idLote + " - " + codigoLote + " (Status: " + status + " / Categoria ID: " + idCategoria + ")";
    }
}
