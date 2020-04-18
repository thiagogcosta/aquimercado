package com.aquimercado.aquimercado.pedidos;

/**
 * Created by thiago on 13/05/2016.
 */
public class Pedido {

    private String dataPedido;
    private String icone;
    private String valor;
    private String codigo;
    private String status;
    private String id;

    public String getTipo_pedido() {
        return tipo_pedido;
    }

    public void setTipo_pedido(String tipo_pedido) {
        this.tipo_pedido = tipo_pedido;
    }

    private  String tipo_pedido;


    //Construtor
    public Pedido(String data, String icone, String valor, String cod, String status, String id, String tipo_pedido){
        setDataPedido(data);
        setIcone(icone);
        setValor(valor);
        setCodigo(cod);
        setStatus(status);
        setId(id);
        setTipo_pedido(tipo_pedido);
    }

    public Pedido(){

    }
    //Get e Set
    public String getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(String dataPedido) {
        this.dataPedido = dataPedido;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
