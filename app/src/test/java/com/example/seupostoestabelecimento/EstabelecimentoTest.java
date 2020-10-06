package com.example.seupostoestabelecimento;

import com.example.seupostoestabelecimento.model.Estabelecimento;

import org.junit.Test;

public class EstabelecimentoTest {

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerRazaoSocialVazio(){
        Estabelecimento est = new Estabelecimento("","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerNomeFantasiaVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerCnpjVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerTelefoneVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerCepVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerRuaVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","","Centro","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerBairroVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","","16","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerNumeroVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","","Acreúna","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerCidadeVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","","Goiás","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerEstadoVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","","-17.394188","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerLatitudeVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","","-50.381200");
    }

    @Test(expected = IllegalArgumentException.class)
    public void estabelecimentoNaoPodeTerLongitudeVazio(){
        Estabelecimento est = new Estabelecimento("Posto Decio LTDA","Posto Decio","123456789","6436451919","75960000","Avenida Araguaia","Centro","16","Acreúna","Goiás","-17.394188","");
    }

}
