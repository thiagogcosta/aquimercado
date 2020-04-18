package com.aquimercado.aquimercado.notificacao.oferta;

/**
 * Created by thiago on 06/01/2016.
 */
public class OfertaNotificacaoItem {

    private String mNome_produto;
    private String mNome_mercado;
    private String mValidade;
    private String mPreco;
    private String mImagemProduto;
    private String mImagemMercado;

    public OfertaNotificacaoItem(String nome_produto, String nome_mercado, String validade, String preco,
                                 String imagemproduto, String imagemmercado){
        this(nome_produto,nome_mercado,validade,preco,imagemproduto);
        setmImagemMercado(imagemmercado);
    }

    public OfertaNotificacaoItem(String nome_produto, String nome_mercado, String validade, String preco,
                                 String imagemproduto){
        this(nome_produto,nome_mercado,validade,preco);
        setmImagemProduto(imagemproduto);
    }
    public OfertaNotificacaoItem(String nome_produto, String nome_mercado, String validade, String preco){
        this(nome_produto,nome_mercado,validade);
        setmPreco(preco);
    }
    public OfertaNotificacaoItem(String nome_produto, String nome_mercado, String validade){
        this(nome_produto,nome_mercado);
        setmValidade(validade);
    }
    public OfertaNotificacaoItem(String nome_produto, String nome_mercado){
        this(nome_produto);
        setmNome_mercado(nome_mercado);
    }
    public OfertaNotificacaoItem(String nome_produto){
        setmNome_produto(nome_produto);
    }

    public OfertaNotificacaoItem(){
    }



    public String getmNome_produto() {
        return mNome_produto;
    }

    public void setmNome_produto(String mNome_produto) {
        this.mNome_produto = mNome_produto;
    }

    public String getmNome_mercado() {
        return mNome_mercado;
    }

    public void setmNome_mercado(String mNome_mercado) {
        this.mNome_mercado = mNome_mercado;
    }

    public String getmValidade() {
        return mValidade;
    }

    public void setmValidade(String mValidade) {
        this.mValidade = mValidade;
    }

    public String getmPreco() {
        return mPreco;
    }

    public void setmPreco(String mPreco) {
        this.mPreco = mPreco;
    }

    public String getmImagemProduto() {
        return mImagemProduto;
    }

    public void setmImagemProduto(String mImagemProduto) {
        this.mImagemProduto = mImagemProduto;
    }

    public String getmImagemMercado() {
        return mImagemMercado;
    }

    public void setmImagemMercado(String mImagemMercado) {
        this.mImagemMercado = mImagemMercado;
    }





}
