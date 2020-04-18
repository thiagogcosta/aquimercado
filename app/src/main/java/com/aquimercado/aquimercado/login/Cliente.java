package com.aquimercado.aquimercado.login;

/**
 * Created by thiago on 19/01/2016.
 */
public class Cliente {

    //Atributos
    private String Nome;
    private String Senha;
    private String Email;
    private String CPF;
    private String Nascimento;
    private String Telefone;
    private String Id;
    private String cep;
    private String estado;
    private String cidade;
    private String rua;
    private String num;
    private String verificacao;


                                //Construtores
    //Cadastro completo
    public Cliente (String nome, String email, String senha, String id, String cpf, String nascimento, String cep, String estado, String cidade, String rua, String numero, String telefone){
        this();
        setId(id);
        setCPF(cpf);
        setNascimento(nascimento);
        setCep(cep);
        setEstado(estado);
        setCidade(cidade);
        setRua(rua);
        setNum(numero);
        setTelefone(telefone);
    }

    //Cadastro Simples
    public Cliente(String nome, String email, String senha){
        this();
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    //Logar
    public Cliente(String email, String senha){
        setEmail(email);
        setSenha(senha);
    }
    //sessão
    public Cliente(String id){
        setId(id);
    }

    public Cliente(){}

    //Métodos GET and SET
    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getNascimento() {
        return Nascimento;
    }

    public void setNascimento(String nascimento) {
        Nascimento = nascimento;
    }



    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getVerificacao() {
        return verificacao;
    }

    public void setVerificacao(String verificacao) {
        this.verificacao = verificacao;
    }
}
