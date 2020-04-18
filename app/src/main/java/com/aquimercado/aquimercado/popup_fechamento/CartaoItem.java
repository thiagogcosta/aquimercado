package com.aquimercado.aquimercado.popup_fechamento;

/**
 * Created by thiago on 09/09/2016.
 */
public class CartaoItem {
    //Atributos
    private String id;
    private String nome;
    private String imagem;

    public CartaoItem(String id, String nome, String imagem) {
        setId(id);
        setNome(nome);
        setImagem(imagem);
    }

    public CartaoItem() {
    }


    //GET and SET
    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
