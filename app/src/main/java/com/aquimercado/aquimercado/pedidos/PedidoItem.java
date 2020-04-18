package com.aquimercado.aquimercado.pedidos;

/**
 * Created by thiago on 20/05/2016.
 */
public class PedidoItem {

    //Atributos
    private String nome;
    private String imagem;
    private String valor;
    private Integer numCelula;
    private  String pQuantidade;




    public PedidoItem(String n, String i, String v, Integer num, String quantidade ){
        setNome(n);
        setImagem(i);
        setValor(v);
        setNumCelula(num);
        setQuantidade(quantidade);

    }

    public PedidoItem(){

    }

    //  Métodos GET AND SET


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getNumCelula() {
        return numCelula;
    }

    public void setNumCelula(Integer numCelula) {
        this.numCelula = numCelula;
    }

    public String getQuantidade() {
        return pQuantidade;
    }

    public void setQuantidade(String quantidade) {
        this.pQuantidade = quantidade;
    }

}
