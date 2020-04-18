package com.aquimercado.aquimercado.sobre;

/**
 * Created by thiago on 29/01/2016.
 */
public class Contato {

    private String nome;
    private String email;
    private String mensagem;

    public Contato(String nome, String email, String mensagem){
        setNome(nome);
        setEmail(email);
        setMensagem(mensagem);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
