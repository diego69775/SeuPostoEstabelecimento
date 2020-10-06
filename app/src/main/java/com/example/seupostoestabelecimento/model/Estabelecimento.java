package com.example.seupostoestabelecimento.model;

import com.example.seupostoestabelecimento.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import java.io.Serializable;
import java.util.List;

public class Estabelecimento implements Serializable {

    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;
    private String telefone;
    private String cep;
    private String rua;
    private String bairro;
    private String numero;
    private String cidade;
    private String estado;
    private String latitute;
    private String longitude;
    private Usuario usuario;
    private List<Produto> produto;
    private List<Avaliacao> avaliacao;

    public Estabelecimento() {
    }

    public Estabelecimento(String razaoSocial, String nomeFantasia, String cnpj, String telefone, String cep, String rua, String bairro, String numero, String cidade, String estado, String latitute, String longitude) {

        if(razaoSocial.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Razão Social vazio!");
        }

        if(nomeFantasia.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Nome Fantasia vazio!");
        }

        if(cnpj.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter CNPJ vazio!");
        }

        if(telefone.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter telefone vazio!");
        }

        if(cep.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter CEP vazio!");
        }

        if(rua.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Rua vazio!");
        }

        if(bairro.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Bairro vazio!");
        }

        if(numero.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Número vazio!");
        }

        if(cidade.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Cidade vazio!");
        }

        if(estado.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Estado vazio!");
        }

        if(latitute.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Latitude vazio!");
        }

        if(longitude.equals("")){
            throw new IllegalArgumentException("Estabelecimento não pode ter Longitude vazio!");
        }

        this.razaoSocial = razaoSocial;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.cidade = cidade;
        this.estado = estado;
        this.latitute = latitute;
        this.longitude = longitude;
    }

    public void criar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(getUsuario().getId());
        estabelecimentoRef.setValue(this);
    }

    public void atualizar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference estabelecimentoRef = firebaseRef.child("estabelecimentos").child(getUsuario().getId());
        estabelecimentoRef.setValue(this);
    }

    public List<Avaliacao> getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(List<Avaliacao> avaliacao) {
        this.avaliacao = avaliacao;
    }

    public List<Produto> getProduto() {
        return produto;
    }

    public void setProduto(List<Produto> produto) {
        this.produto = produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLatitute() {
        return latitute;
    }

    public void setLatitute(String latitute) {
        this.latitute = latitute;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
