package com.example.seupostoestabelecimento;

import com.example.seupostoestabelecimento.model.Avaliacao;

import org.junit.Test;

public class AvaliacaoTest {

    @Test(expected = IllegalArgumentException.class)
    public void avaliacaoNaoPodeTerDescricaoVazio(){
        Avaliacao ava = new Avaliacao("",5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void avaliacaoNaoPodeTerNotaMenorOuIgualAZero(){
        Avaliacao ava = new Avaliacao("Otimo estabelecimento",0);
    }

}
