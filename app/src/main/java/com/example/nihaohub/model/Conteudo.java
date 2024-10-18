package com.example.nihaohub.model;

public class Conteudo {
    private int idConteudo;
    private int idEducador;
    private String nomeConteudo;
    private String autor;
    private String descricao;
    private String tema;

    public Conteudo() {

    }

    public Conteudo(int idConteudo, int idEducador, String nomeConteudo, String autor, String descricao, String tema) {
        this.idConteudo = idConteudo;
        this.idEducador = idEducador;
        this.nomeConteudo = nomeConteudo;
        this.autor = autor;
        this.descricao = descricao;
        this.tema = tema;
    }

    public int getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(int idConteudo) {
        this.idConteudo = idConteudo;
    }

    public int getIdEducador() {
        return idEducador;
    }

    public void setIdEducador(int idEducador) {
        this.idEducador = idEducador;
    }

    public String getNomeConteudo() {
        return nomeConteudo;
    }

    public void setNomeConteudo(String nomeConteudo) {
        this.nomeConteudo = nomeConteudo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }
}
