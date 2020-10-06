package com.example.seupostoestabelecimento.model;

import java.io.Serializable;

public class Promocao implements Serializable {

    private int id;
    private String nomeProduto;
    private String dataInicial;
    private String dataFinal;
    private double valorNormal;
    private double valorPromocao;

    public Promocao() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public double getValorNormal() {
        return valorNormal;
    }

    public void setValorNormal(double valorNormal) {
        this.valorNormal = valorNormal;
    }

    public double getValorPromocao() {
        return valorPromocao;
    }

    public void setValorPromocao(double valorPromocao) {
        this.valorPromocao = valorPromocao;
    }
}
