package com.example.seupostoestabelecimento;

import com.example.seupostoestabelecimento.model.Produto;

import org.junit.Test;

public class ProdutoTest {

    @Test(expected = IllegalArgumentException.class)
    public void produtoNaoPodeTerDescricaoVazio(){
        Produto prod = new Produto("",3.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void produtoNaoPodeTerValorMenorQueZero(){
        Produto prod = new Produto("Gasolina",-2);
    }

}
