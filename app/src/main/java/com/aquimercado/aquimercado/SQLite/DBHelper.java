package com.aquimercado.aquimercado.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aquimercado.aquimercado.departamento.DepartamentoItem;
import com.aquimercado.aquimercado.produto.ProdutoItem;

import java.util.LinkedList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper  {

    private static final String TABLE_PRODUTO = "produto";

    public static final String PRODUTOS_TABLE_ID = "id";
    public static final String PRODUTOS_TABLE_NAME = "nome";
    public static final String PRODUTOS_TABLE_DESCRICAO = "descricao";
    public static final String PRODUTOS_TABLE_PRECO = "preco";
    public static final String PRODUTOS_TABLE_MARCA = "marca";
    public static final String PRODUTOS_TABLE_IMAGEM = "imagem";
    public static final String PRODUTOS_TABLE_DESCONTO = "desconto";
    public static final String PRODUTOS_TABLE_APDESCONTO = "apdesconto";
    public static final String PRODUTOS_TABLE_QTDE = "quantidade";

    private static final String[] COLUMNS = {PRODUTOS_TABLE_ID,PRODUTOS_TABLE_NAME,PRODUTOS_TABLE_DESCRICAO,PRODUTOS_TABLE_PRECO,PRODUTOS_TABLE_MARCA,PRODUTOS_TABLE_IMAGEM,PRODUTOS_TABLE_DESCONTO,PRODUTOS_TABLE_APDESCONTO,PRODUTOS_TABLE_QTDE};
    //Database Name
    public static final String DATABASE_NAME = "DBProdutos.db";

    // Database Version
    private static final int DATABASE_VERSION = 2;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // SQL statement to create produto table
            String CREATE_PRODUTO_TABLE = "CREATE TABLE produto("+
                    "id TEXT,"+
                    "nome TEXT,"+
                    "descricao TEXT,"+
                    "preco TEXT,"+
                    "marca TEXT,"+
                    "imagem TEXT,"+
                    "desconto TEXT,"+
                    "apdesconto TEXT,"+
                    "quantidade TEXT"+
                    ")";

            // create produto table
            db.execSQL(CREATE_PRODUTO_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older books table if existed
            db.execSQL("DROP TABLE IF EXISTS produto");

            // create fresh books table
            this.onCreate(db);
        }


    public void addProd(ProdutoItem pd){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PRODUTOS_TABLE_ID, pd.getId()); // get id
        values.put(PRODUTOS_TABLE_NAME, pd.getNome()); // get name
        values.put(PRODUTOS_TABLE_DESCRICAO, pd.getDescricao()); // get descricao
        values.put(PRODUTOS_TABLE_PRECO, pd.getPreco()); // get preco
        values.put(PRODUTOS_TABLE_MARCA, pd.getMarca()); // get marca
        values.put(PRODUTOS_TABLE_IMAGEM, pd.getThumbnail()); // get imagem
        values.put(PRODUTOS_TABLE_DESCONTO, pd.getpDesconto()); // get desconto
        values.put(PRODUTOS_TABLE_APDESCONTO, pd.getApDesconto()); // get aplicavel desconto
        values.put(PRODUTOS_TABLE_QTDE, "1"); // get quantidade produtos
        // 3. insert
        db.insert(TABLE_PRODUTO, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public List<ProdutoItem> getAllProd() {
        List<ProdutoItem> produtos = new LinkedList<ProdutoItem>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PRODUTO;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        ProdutoItem prod = null;
        if (cursor.moveToFirst()) {
            do {
                prod = new ProdutoItem();
                prod.setId(cursor.getString(0));
                prod.setNome(cursor.getString(1));
                prod.setDescricao(cursor.getString(2));
                prod.setPreco(cursor.getString(3));
                prod.setMarca(cursor.getString(4));
                prod.setThumbnail(cursor.getString(5));
                prod.setpDesconto(cursor.getString(6));
                prod.setApDesconto(cursor.getString(7));
                prod.setQuantidade(cursor.getString(8));
                // Add book to books
                produtos.add(prod);
            } while (cursor.moveToNext());
        }


        // return books
        return produtos;
    }
    public void deleteAll() {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_PRODUTO, null, null);
    }

    public void addProdDepto(DepartamentoItem pd){
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(PRODUTOS_TABLE_ID, pd.getidProduto()); // get id
        values.put(PRODUTOS_TABLE_NAME, pd.getNomeProduto()); // get name
        values.put(PRODUTOS_TABLE_DESCRICAO, pd.getDescricao()); // get descricao
        values.put(PRODUTOS_TABLE_PRECO, pd.getPrecoProduto()); // get preco
        values.put(PRODUTOS_TABLE_MARCA, pd.getMarca()); // get marca
        values.put(PRODUTOS_TABLE_IMAGEM, pd.getImagemProduto()); // get imagem
        values.put(PRODUTOS_TABLE_DESCONTO, pd.getDesconto()); // get desconto
        values.put(PRODUTOS_TABLE_APDESCONTO, pd.getApDesconto()); // get aplicavel desconto
        values.put(PRODUTOS_TABLE_QTDE, pd.getQuantidade()); // get quantidade produtos

        // 3. insert
        db.insert(TABLE_PRODUTO, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public List<DepartamentoItem> getAllProdDepto() {
        List<DepartamentoItem> produtos = new LinkedList<DepartamentoItem>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_PRODUTO;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        DepartamentoItem prod = null;
        if (cursor.moveToFirst()) {
            do {
                prod = new DepartamentoItem();
                prod.setIdProduto(cursor.getString(0));
                prod.setNomeProduto(cursor.getString(1));
                prod.setDescricao(cursor.getString(2));
                prod.setPrecoProduto(cursor.getString(3));
                prod.setMarca(cursor.getString(4));
                prod.setImagemProduto(cursor.getString(5));
                prod.setDesconto(cursor.getString(6));
                prod.setApDesconto(cursor.getString(7));
                prod.setQuantidade(cursor.getString(8));


                // Add book to books
                produtos.add(prod);
            } while (cursor.moveToNext());
        }


        // return books
        return produtos;
    }

    public void updateProdQtde(DepartamentoItem pd) {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRODUTOS_TABLE_QTDE, pd.getQuantidade());
        db.update(TABLE_PRODUTO, values,"id="+pd.getidProduto(),null);
    }

}
