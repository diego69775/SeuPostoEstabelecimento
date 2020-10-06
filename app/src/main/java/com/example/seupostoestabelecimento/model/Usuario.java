package com.example.seupostoestabelecimento.model;

import com.google.firebase.database.Exclude;
import java.io.Serializable;

public class Usuario implements Serializable {
    private String id;
    private String email;
    private String senha;


    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
