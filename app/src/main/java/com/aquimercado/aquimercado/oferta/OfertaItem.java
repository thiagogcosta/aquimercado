package com.aquimercado.aquimercado.oferta;

public class OfertaItem {
    private String idProduto;
    private String idMercado;
    private String cDepartamento;
    private String cNomeProduto;
    private String cPrecoProduto;
    private String cImagemProduto;
    private String cMarca;
    private String cDescricao;


    //Construtor

    public OfertaItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                      String preco_produto, String imagem_produto,
                      String marca, String descricao){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto,
                marca);
        setDescricao(descricao);
    }
    public OfertaItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                      String preco_produto, String imagem_produto,
                      String marca){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto);
        setMarca(marca);
    }

    public OfertaItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                      String preco_produto, String imagem_produto){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto);
        setImagemProduto(imagem_produto);
    }

    public OfertaItem(String idProduto, String idMercado, String NomeProduto, String Departamento, String preco_produto){
        this(idProduto,idMercado, NomeProduto,Departamento);
        setPrecoProduto(preco_produto);
    }
    public OfertaItem(String idProduto, String idMercado, String NomeProduto, String Departamento){
        this(idProduto,idMercado,NomeProduto);
        setDepartamento(Departamento);
    }
    public OfertaItem(String idProduto, String idMercado, String NomeProduto){
        this(idProduto,idMercado);
        setNomeProduto(NomeProduto);
    }

    public OfertaItem(String idProduto, String idMercado){
        this(idProduto);
        setidMercado(idMercado);
    }

    public OfertaItem(String idProduto){
        setIdProduto(idProduto);
    }

    public OfertaItem(){
    }


                                //Atributos


    //id do produto
    public String getidProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    //id do mercado
    public String getidMercado() {
        return idMercado;
    }

    public void setidMercado(String idMercado) {
        this.idMercado = idMercado;
    }

    //Nome do Produto
    public String getNomeProduto() {
        return cNomeProduto;
    }

    public void setNomeProduto(String name) {
        this.cNomeProduto = name;
    }

    //Departamento
    public String getDepartamento() {
        return cDepartamento;
    }

    public void setDepartamento(String departamento) {
        this.cDepartamento = departamento;
    }

    //Preco produto
    public String getPrecoProduto() {
        return cPrecoProduto;
    }

    public void setPrecoProduto(String preco_produto) {
        this.cPrecoProduto = preco_produto;
    }

    //Imagem do Produto
    public String getImagemProduto() {
        return cImagemProduto;
    }

    public void setImagemProduto(String imagem_produto) {
        this.cImagemProduto = imagem_produto;
    }

    //Marca
    public String getMarca() {
        return cMarca;
    }

    public void setMarca(String marca) {
        this.cMarca = marca;
    }

    //Descricao
    public String getDescricao() {
        return cDescricao;
    }

    public void setDescricao(String descricao) {
        this.cDescricao = descricao;
    }




}
