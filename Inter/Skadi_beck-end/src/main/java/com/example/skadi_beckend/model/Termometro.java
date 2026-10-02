package com.example.skadi_beckend.model;

public class Termometro {
//      Atributos
    private int id_termometro;
    private String modelo;
    private String status;
    private int id_refrigerador;
// Construtor vazio
    public Termometro() {}
//      Construtor
    public Termometro(int id_termometro, String modelo, String status, int id_refrigerador) {
        this.id_termometro = id_termometro;
        this.modelo = modelo;
        this.status = status;
        this.id_refrigerador = id_refrigerador;
    }

    public Termometro(String modelo, String status, int id_refrigerador) {
        this.modelo = modelo;
        this.status = status;
        this.id_refrigerador = id_refrigerador;
    }
// Getters e Setters
    public int getId_termometro() { return id_termometro; }
    public void setId_termometro(int id_termometro) { this.id_termometro = id_termometro; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getId_refrigerador() { return id_refrigerador; }
    public void setId_refrigerador(int id_refrigerador) { this.id_refrigerador = id_refrigerador; }
}