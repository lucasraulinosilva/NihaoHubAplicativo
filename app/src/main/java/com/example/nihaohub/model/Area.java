package com.example.nihaohub.model;

public class Area {
    private int idArea;
    private int idEducador;
    private String nomeArea;

    public Area() {

    }

    public Area(int idArea, int idEducador, String nomeArea) {
        this.idArea = idArea;
        this.idEducador = idEducador;
        this.nomeArea = nomeArea;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public int getIdEducador() {
        return idEducador;
    }

    public void setIdEstudante(int idEducador) {
        this.idEducador = idEducador;
    }

    public String getNomeArea() {
        return nomeArea;
    }

    public void setNomeArea(String nomeArea) {
        this.nomeArea = nomeArea;
    }
}
