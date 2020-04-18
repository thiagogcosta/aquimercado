package com.aquimercado.aquimercado.ranking;

/**
 * Created by thiago on 16/08/2017.
 */

public class RankingItemNavigation {

    private String nome;
    private String pontuacao;
    private String posicao;
    private String id;


    //**************************************Métodos*************************************************
    public RankingItemNavigation(String nome, String pontuacao, String posicao, String id){
        setNome(nome);
        setPontuacao(pontuacao);
        setPosicao(posicao);
        setId(id);
    }

    public RankingItemNavigation(){

    }


    //*************************************Atributos************************************************
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(String pontuacao) {
        this.pontuacao = pontuacao;
    }

    public String getPosicao() {
        return posicao;
    }

    public void setPosicao(String posicao) {
        this.posicao = posicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
