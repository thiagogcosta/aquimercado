package com.aquimercado.aquimercado.combo;

public class ComboItem {
    private String cName;
    private String cValidade;
    private String mThumbnail;
    private String cIdCombo;
    private String cNameC;
    private String idMercado;
    private String entregaretirada;
    private String cod_horaprevista;
    private String temFrete;
    private String valor_isento;
    private String config;
    private String sobre;



    //Construtor

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada, String cod_horaprevista, String temFrete, String valor_isento, String config, String sobre){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto,idMercado,entregaretirada, cod_horaprevista, temFrete,valor_isento, config);
        setSobre(sobre);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada, String cod_horaprevista, String temFrete, String valor_isento, String config){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto,idMercado,entregaretirada, cod_horaprevista, temFrete,valor_isento);
        setConfig(config);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada, String cod_horaprevista, String temFrete, String valor_isento){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto,idMercado,entregaretirada, cod_horaprevista, temFrete);
        setValor_isento(valor_isento);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada, String cod_horaprevista, String temFrete){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto,idMercado,entregaretirada, cod_horaprevista);
        setTemFrete(temFrete);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada, String cod_horaprevista){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto,idMercado,entregaretirada);
        setCod_horaprevista(cod_horaprevista);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado, String entregaretirada){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto, idMercado);
        setEntregaretirada(entregaretirada);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto, String idMercado){
        this(Name,Imagem, Validade, IdCombo, NomeCompleto);
        setNomeCompleto(idMercado);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo, String NomeCompleto){
        this(Name,Imagem, Validade, IdCombo);
        setNomeCompleto(NomeCompleto);
    }

    public ComboItem(String Name, String Imagem, String Validade, String IdCombo){
        this(Name,Imagem, Validade);
        setIdCombo(IdCombo);
    }

    public ComboItem(String Name, String Imagem, String Validade){
        this(Name,Imagem);
        setValidade(Validade);
    }
    public ComboItem(String Name, String Imagem){
        this(Name);
        setThumbnail(Imagem);
    }
    public ComboItem(String Name){
        setName(Name);
    }

    public ComboItem(){
    }

                                //Atributos
    //Nome do Combo
    public String getName() {
        return cName;
    }

    public void setName(String name) {
        this.cName = name;
    }

    //Validade do Combo
    public String getValidade() {
        return cValidade;
    }

    public void setValidade(String validade) {
        this.cValidade = validade;
    }

    //Imagem do Combo
    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    //Id do Combo
    public String getIdCombo() {
        return cIdCombo;
    }

    public void setIdCombo(String IdCombo) {
        this.cIdCombo = IdCombo;
    }

    //Nome combo completo
    public String getNomeCompleto() {
        return cNameC;
    }

    public void setNomeCompleto(String cNameC) {
        this.cNameC = cNameC;
    }

    public String getIdMercado() {
        return idMercado;
    }

    public void setIdMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    public String getEntregaretirada() {
        return entregaretirada;
    }

    public void setEntregaretirada(String entregaretirada) {
        this.entregaretirada = entregaretirada;
    }

    public String getCod_horaprevista() {
        return cod_horaprevista;
    }

    public void setCod_horaprevista(String cod_horaprevista) {
        this.cod_horaprevista = cod_horaprevista;
    }

    public String getTemFrete() {
        return temFrete;
    }

    public void setTemFrete(String temFrete) {
        this.temFrete = temFrete;
    }

    public String getValor_isento() {
        return valor_isento;
    }

    public void setValor_isento(String valor_isento) {
        this.valor_isento = valor_isento;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getSobre() {
        return sobre;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }
}
