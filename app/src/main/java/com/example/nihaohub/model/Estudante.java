package com.example.nihaohub.model;

public class Estudante {
    private int idEstudante;
    private String loginEstudante;
    private String nomeEstudante;

    public Estudante() {

    }

    public Estudante(int idEstudante, String nomeEstudante, String loginEstudante) {
        this.idEstudante = idEstudante;
        this.nomeEstudante = nomeEstudante;
        this.loginEstudante = loginEstudante;
    }

    public String getNomeEstudante() {
        return nomeEstudante;
    }

    public void setNomeEstudante(String nomeEstudante) {
        this.nomeEstudante = nomeEstudante;
    }

    public int getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(int idEstudante) {
        this.idEstudante = idEstudante;
    }

    public String getLoginEstudante() {
        return loginEstudante;
    }

    public void setLoginEstudante(String loginEstudante) {
        this.loginEstudante = loginEstudante;
    }
}
