package com.example.seupostoestabelecimento.model;

import java.io.Serializable;

public class Produto implements Serializable {

    private int id;
    private String descricao;
    private double valor;
    private Promocao promocao;

    public Produto() {
    }

    public Produto(String descricao, double valor) {

        if(descricao.equals("")){
            throw new IllegalArgumentException("Produto não pode ter Descrição vazio!");
        }

        if(valor < 0){
            throw new IllegalArgumentException("Produto não pode ter Valor menor que zero!");
        }

        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Promocao getPromocao() {
        return promocao;
    }

    public void setPromocao(Promocao promocao) {
        this.promocao = promocao;
    }
}
