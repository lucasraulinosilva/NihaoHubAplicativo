package com.example.nihaohub.model;

public class Avaliacao {
    private int idAvaliacao;
    private int idConteudo;
    private String avaliacao;
    private String nomeAutorComentario;

    public Avaliacao() {

    }

    public Avaliacao(int idAvaliacao, int idConteudo, String nomeAutorComentario, String avaliacao) {
        this.idAvaliacao = idAvaliacao;
        this.idConteudo = idConteudo;
        this.nomeAutorComentario = nomeAutorComentario;
        this.avaliacao = avaliacao;
    }

    public int getIdAvaliacao() {
        return idAvaliacao;
    }

    public void setIdAvaliacao(int idAvaliacao) {
        this.idAvaliacao = idAvaliacao;
    }

    public int getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(int idConteudo) {
        this.idConteudo = idConteudo;
    }

    public String getNomeAutorComentario() {
        return nomeAutorComentario;
    }

    public void setNomeAutorComentario(String nomeAutorComentario) {
        this.nomeAutorComentario = nomeAutorComentario;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }
}
