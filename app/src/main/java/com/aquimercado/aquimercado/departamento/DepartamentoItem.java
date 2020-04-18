package com.aquimercado.aquimercado.departamento;

public class DepartamentoItem {
    private String idProduto;
    private String idMercado;
    private String cDepartamento;
    private String cNomeProduto;
    private String cPrecoProduto;
    private String cImagemProduto;
    private String cDesconto;
    private String cApDesconto;
    private String cMarca;
    private String cDescricao;
    private String cQuantidade;




    //Construtor

    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto, String desconto, String apdesconto,
                            String marca, String descricao, String quantidade){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto,desconto,apdesconto,
                marca,descricao);
        setQuantidade(quantidade);
    }

    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto, String desconto, String apdesconto,
                            String marca, String descricao){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto,desconto,apdesconto,
                marca);
        setDescricao(descricao);
    }
    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto, String desconto, String apdesconto,
                            String marca){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto,desconto,apdesconto);
        setMarca(marca);
    }

    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto, String desconto, String apdesconto ){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto,desconto);
        setApDesconto(apdesconto);
    }

    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto, String desconto ){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto,imagem_produto);
        setDesconto(desconto);
    }
    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento,
                            String preco_produto, String imagem_produto){
        this(idProduto, idMercado, NomeProduto,Departamento, preco_produto);
        setImagemProduto(imagem_produto);
    }

    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento, String preco_produto){
        this(idProduto,idMercado, NomeProduto,Departamento);
        setPrecoProduto(preco_produto);
    }
    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto, String Departamento){
        this(idProduto,idMercado,NomeProduto);
        setDepartamento(Departamento);
    }
    public DepartamentoItem(String idProduto, String idMercado, String NomeProduto){
        this(idProduto,idMercado);
        setNomeProduto(NomeProduto);
    }

    public DepartamentoItem(String idProduto, String idMercado){
        this(idProduto);
        setidMercado(idMercado);
    }

    public DepartamentoItem(String idProduto){
        setIdProduto(idProduto);
    }

    public DepartamentoItem(){
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

    //Desconto
    public String getDesconto() {
        return cDesconto;
    }

    public void setDesconto(String desconto) {
        this.cDesconto = desconto;
    }

    //Aplica Desconto
    public String getApDesconto() {
        return cApDesconto;
    }

    public void setApDesconto(String apdesconto) {
        this.cApDesconto = apdesconto;
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

    //id do produto
    public String getQuantidade() {
        return cQuantidade;
    }

    public void setQuantidade(String quantidade) {
        this.cQuantidade = quantidade;
    }





}
