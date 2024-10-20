package com.example.nihaohub.model;

public class Comentario {
    private int idComentario;
    private int idConteudo;
    private String comentario;
    private String nomeAutorComentario;

    public Comentario() {

    }

    public Comentario(int idComentario, int idConteudo, String comentario, String nomeAutorComentario) {
        this.idComentario = idComentario;
        this.idConteudo = idConteudo;
        this.comentario = comentario;
        this.nomeAutorComentario = nomeAutorComentario;
    }

    public int getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(int idComentario) {
        this.idComentario = idComentario;
    }

    public int getIdConteudo() {
        return idConteudo;
    }

    public void setIdConteudo(int idConteudo) {
        this.idConteudo = idConteudo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNomeAutorComentario() {
        return nomeAutorComentario;
    }

    public void setNomeAutorComentario(String nomeAutorComentario) {
        this.nomeAutorComentario = nomeAutorComentario;
    }
}
