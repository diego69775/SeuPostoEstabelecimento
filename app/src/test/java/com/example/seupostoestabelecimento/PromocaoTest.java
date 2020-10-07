package com.example.seupostoestabelecimento;

import com.example.seupostoestabelecimento.model.Promocao;

import org.junit.Assert;
import org.junit.Test;

public class PromocaoTest {

    @Test(expected = IllegalArgumentException.class)
    public void promocaoNaoPodeTerNomeProdutoVazio(){
        Promocao promo = new Promocao("","07/10/2020","08/10/2020",3.6,3.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void promocaoNaoPodeTerDataInicialVazio(){
        Promocao promo = new Promocao("Etanol","","08/10/2020",3.6,3.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void promocaoNaoPodeTerDataFinalVazio(){
        Promocao promo = new Promocao("Etanol","07/10/2020","",3.6,3.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void promocaoNaoPodeTerValorNormalMenorQueZero(){
        Promocao promo = new Promocao("Etanol","07/10/2020","08/10/2020",-2,3.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void promocaoNaoPodeTerValorPromocaooMenorQueZero(){
        Promocao promo = new Promocao("Etanol","07/10/2020","08/10/2020",3.6,-2);
    }

    @Test
    public void promocaoNaoPodeTerValorPromocaoMaiorOuIgualValorNormal(){
        Promocao promo = new Promocao("Etanol","07/10/2020","08/10/2020",3.6,4.5);
        boolean resultado = promo.verificaValorPromocao();
        Assert.assertFalse(resultado);
    }

    @Test
    public void promocaoTemQueTerValorPromocaoMenorQueValorNormal(){
        Promocao promo = new Promocao("Etanol","07/10/2020","08/10/2020",3.6,3.2);
        boolean resultado = promo.verificaValorPromocao();
        Assert.assertTrue(resultado);
    }


}
