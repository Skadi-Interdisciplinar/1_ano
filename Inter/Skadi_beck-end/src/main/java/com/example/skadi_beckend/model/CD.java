package com.example.skadi_beckend.model;

/**
 * Classe que representa a entidade CD (Centro de Distribuição)
 */
public class CD {
    private int idCd;
    private String nome;
    private String cnpj;
    private String endereco;

    // Construtor vazio para Gson
    public CD() {}

    // Construtor 
    public CD(int idCd, String nome, String cnpj, String endereco) {
        this.idCd = idCd;
        this.nome = nome;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    // Getters e Setters
    public int getIdCd() { return idCd; }
    public void setIdCd(int idCd) { this.idCd = idCd; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
}