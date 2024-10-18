package com.example.nihaohub.model;

public class Formacao {
    private int idEducador;
    private int idFormacao;
    private String nomeFormacao;

    public Formacao() {

    }

    public Formacao(int idFormacao, int idEducador, String nomeFormacao) {
        this.idFormacao = idFormacao;
        this.idEducador = idEducador;
        this.nomeFormacao = nomeFormacao;
    }

    public int getIdEducador() {
        return idEducador;
    }

    public void setIdEducador(int idEducador) {
        this.idEducador = idEducador;
    }

    public int getIdFormacao() {
        return idFormacao;
    }

    public void setIdFormacao(int idFormacao) {
        this.idFormacao = idFormacao;
    }

    public String getNomeFormacao() {
        return nomeFormacao;
    }

    public void setNomeFormacao(String nomeFormacao) {
        this.nomeFormacao = nomeFormacao;
    }
}
