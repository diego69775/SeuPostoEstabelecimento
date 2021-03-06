package com.example.seupostoestabelecimento.model;

import java.io.Serializable;

public class Avaliacao implements Serializable {

    private int id;
    private String descricao;
    private double nota;
    private String idAvaliador;
    private String emailAvaliador;

    public Avaliacao() {
    }

    public Avaliacao(String descricao, double nota) {

        if(descricao.equals("")){
            throw new IllegalArgumentException("Avaliação não pode ter Descrição vazio!");
        }

        if(nota <= 0){
            throw new IllegalArgumentException("Avaliação não pode ter Nota menor ou igual a zero!");
        }

        this.descricao = descricao;
        this.nota = nota;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getIdAvaliador() {
        return idAvaliador;
    }

    public void setIdAvaliador(String idAvaliador) {
        this.idAvaliador = idAvaliador;
    }

    public String getEmailAvaliador() {
        return emailAvaliador;
    }

    public void setEmailAvaliador(String emailAvaliador) {
        this.emailAvaliador = emailAvaliador;
    }
}
