package com.aquimercado.aquimercado.produto;

import android.os.Parcel;

import java.io.Serializable;

/**
 * Created by thiago on 09/12/2015.
 */
public class ProdutoItem implements Serializable {

    //Atributos
    private String pNome;
    private String pDescricao;
    private String pPreco;
    private String pNomeCombo;
    private String pMarca;
    private String pThumbnail;
    private String pId;
    private String pDesconto;
    private String apDesconto;
    private String idCombo;
    private String idMercado;
    private String psobre;
    private String pQuantidade;


    //Construtor

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto, String apDesconto, String idCombo, String idMercado, String sobre, String quantidade) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id, pDesconto,apDesconto, idCombo, idMercado,sobre);
        setQuantidade(quantidade);
    }
    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto, String apDesconto, String idCombo, String idMercado, String sobre) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id, pDesconto,apDesconto, idCombo, idMercado);
        setPsobre(sobre);
    }


    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto, String apDesconto, String idCombo, String idMercado) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id, pDesconto,apDesconto, idCombo);
        setIdMercado(idMercado);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto, String apDesconto, String idCombo) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id, pDesconto,apDesconto);
        setIdCombo(idCombo);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto, String apDesconto) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id, pDesconto);
        setApDesconto(apDesconto);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id, String pDesconto) {
        this(Nome, Descricao, Preco, Marca, Thumbnail, id);
        setpDesconto(pDesconto);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail, String id) {
        this(Nome, Descricao, Preco, Marca, Thumbnail);
        setId(id);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca, String Thumbnail) {
        this(Nome, Descricao, Preco, Marca);
        setThumbnail(Thumbnail);
    }

    public ProdutoItem(String Nome, String Descricao, String Preco, String Marca) {
        this(Nome, Descricao, Preco);
        setMarca(Marca);
    }


    public ProdutoItem(String Nome, String Descricao, String Preco) {
        this(Nome, Descricao);
        setPreco(Preco);
    }

    public ProdutoItem(String Nome, String Descricao) {
        this(Nome);
        setDescricao(Descricao);
    }

    public ProdutoItem(String Nome) {
        setNome(Nome);
    }

    public ProdutoItem() {
    }

    protected ProdutoItem(Parcel in) {
        pNome = in.readString();
        pDescricao = in.readString();
        pPreco = in.readString();
        pNomeCombo = in.readString();
        pMarca = in.readString();
        pThumbnail = in.readString();
        pId = in.readString();
    }


    public String getNome() {
        return pNome;
    }

    public void setNome(String name) {
        this.pNome = name;
    }

    /* *************************Descricao ******************************* */
    public String getDescricao() {
        return pDescricao;
    }

    public void setDescricao(String descricao) {
        this.pDescricao = descricao;
    }

    /* *************************************Preco***************************** */
    public String getPreco() {
        return pPreco;
    }

    public void setPreco(String preco) {
        this.pPreco = preco;
    }

    /* **************************** Marca *******************/

    public String getMarca() {
        return pMarca;
    }

    public void setMarca(String marca) {
        this.pMarca = marca;
    }
    /* **************************** Imagem *******************/

    public String getThumbnail() {
        return pThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.pThumbnail = thumbnail;
    }


    public String getId() {
        return pId;
    }

    public void setId(String pId) {
        this.pId = pId;
    }

    //*********************************Método de clonagem***************************************
    public ProdutoItem clone(ProdutoItem p){
        ProdutoItem novo = new ProdutoItem();
        novo.setId(p.getId());
        novo.setNome(p.getNome());
        novo.setThumbnail(p.getThumbnail());
        novo.setDescricao(p.getDescricao());
        novo.setMarca(p.getMarca());
        novo.setPreco(p.getPreco());
        return novo;
    }

    public String getpDesconto() {
        return pDesconto;
    }

    public void setpDesconto(String pDesconto) {
        this.pDesconto = pDesconto;
    }

    public String getApDesconto() {
        return apDesconto;
    }

    public void setApDesconto(String apDesconto) {
        this.apDesconto = apDesconto;
    }

    public String getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(String idCombo) {
        this.idCombo = idCombo;
    }

    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getPsobre() {
        return psobre;
    }

    public void setPsobre(String psobre) {
        this.psobre = psobre;
    }

    public String getQuantidade() {
        return pQuantidade;
    }

    public void setQuantidade(String quantidade) {
        this.pQuantidade = quantidade;
    }

}
