package com.aquimercado.aquimercado.premiacao;

/**
 * Created by thiago on 06/01/2016.
 */
public class PremioItem {


    private String mNome_produto;
    private String mNome_mercado;
    private String mImagemProduto;
    private String mImagemMercado;
    private String mDescricaopremio;
    private String mVencimento;
  private  String mTipo;

    public PremioItem(String mNome_produto, String mNome_mercado, String mImagemProduto, String mImagemMercado, String mDescricaopremio, String mVencimento, String mTipo) {
        this.mNome_produto = mNome_produto;
        this.mNome_mercado = mNome_mercado;
        this.mImagemProduto = mImagemProduto;
        this.mImagemMercado = mImagemMercado;
        this.mDescricaopremio = mDescricaopremio;
        this.mVencimento = mVencimento;
        this.mTipo = mTipo;
    }

    public String getmTipo() {
        return mTipo;
    }

    public void setmTipo(String mTipo) {
        this.mTipo = mTipo;
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

    public String getmDescricaopremio() {
        return mDescricaopremio;
    }

    public void setmDescricaopremio(String mDescricaopremio) {
        this.mDescricaopremio = mDescricaopremio;
    }

    public String getmVencimento() {
        return mVencimento;
    }

    public void setmVencimento(String mVencimento) {
        this.mVencimento = mVencimento;
    }









}
