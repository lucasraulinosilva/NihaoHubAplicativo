package com.example.nihaohub.model;

public class Educador {
    private int idEducador;
    private String loginEducador;
    private String nomeEducador;

    public Educador() {

    }

    public Educador(int idEducador, String nomeEducador, String loginEducador) {
        this.idEducador = idEducador;
        this.nomeEducador = nomeEducador;
        this.loginEducador = loginEducador;
    }

    public int getIdEducador() {
        return idEducador;
    }

    public void setIdEducador(int idEducador) {
        this.idEducador = idEducador;
    }

    public String getLoginEducador() {
        return loginEducador;
    }

    public void setLoginEducador(String loginEducador) {
        this.loginEducador = loginEducador;
    }

    public String getNomeEducador() {
        return nomeEducador;
    }

    public void setNomeEducador(String nomeEducador) {
        this.nomeEducador = nomeEducador;
    }
}
