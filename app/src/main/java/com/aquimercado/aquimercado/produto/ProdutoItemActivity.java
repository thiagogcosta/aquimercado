package com.aquimercado.aquimercado.produto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SQLite.DBHelper;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.fechamento.AFechamento;
import com.aquimercado.aquimercado.login.AlterarNavigation;
import com.aquimercado.aquimercado.login.LoginNavigation;
import com.aquimercado.aquimercado.noWifi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ProdutoItemActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Toolbar toolbar;
    RecyclerView lista0, lista1, lista2;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    public static TextView txtTotal,txtDesc,txtDescTotal = null;
    int i, jobTam, contador_prod=0, contador_prod_json=0, contador_notlistavazia=0, contador_prodlinha=0, status = 0, status1 = 0, status2 = 0, status3 = 0;
    public static String auxUrlimagem = null;
    public static String auxUrl = null;
    public String config = "";
    private String auxIdMercado;
    String aux;
    static List<ProdutoItem> ListaPD = new ArrayList<ProdutoItem>();
    DBHelper myDb;
    public static final String PREFS_NAME = "produtos";
    static DBHelper db ;
    public static float soma,acu,eq=0;
    public static Intent intent = null;
    public static Bundle b = null;
    private List<ProdutoItem> produto1;
    private List<ProdutoItem> produto2;
    private List<ProdutoItem> produto3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);

        intent = getIntent();

        b = intent.getExtras();

        config = (String) b.get("config");


        switch(config){
            case "1":
                Toast.makeText(getApplicationContext(), "Por favor, selecione todos os produtos!", Toast.LENGTH_LONG).show();
                break;
            case "2":
                Toast.makeText(getApplicationContext(), "Por favor, selecione um produto por linha pelo menos!", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getApplicationContext(), "Erro de configuração de combo!", Toast.LENGTH_LONG).show();
                break;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              prodBotao();

            }
        });


        db = new DBHelper(getApplicationContext());
        db.deleteAll();
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtDescTotal = (TextView) findViewById(R.id.txtDescTot);

        //Combo ou departamento
        SharedPreferences combo_deptoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor combo_depto  = combo_deptoPref.edit();
        combo_depto.putString("combo_depto", "0");
        combo_depto.apply();

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        auxUrl = urlPref.getString("url_base", "");

        //URL da IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        String url = auxUrl + "makejsonProdutos.php?id="+b.get("id");


        SharedPreferences mercadoPref2 = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edMercado2 = mercadoPref2.edit();
        edMercado2.putString("id_combo", ""+b.get("id"));
        edMercado2.apply();

        SharedPreferences produtosClicados = getApplication().getSharedPreferences("INFOS_MERCADO", MODE_PRIVATE);
        auxIdMercado = produtosClicados.getString("id_mercado", "");



        TextView nome_combo = (TextView)findViewById(R.id.nome_combo);
        String nomec = (String)b.get("nome");
        nome_combo.setText(nomec);
        //*************************LISTAS*****************************************

        //Lista 1
        LinearLayoutManager llm0 = new LinearLayoutManager(getApplicationContext());
        llm0.setOrientation(LinearLayoutManager.HORIZONTAL);
        lista0 = (RecyclerView) findViewById(R.id.recycler0);
        lista0.setHasFixedSize(true);
        lista0.setLayoutManager(llm0);


        //Lista 2
        LinearLayoutManager llm1 = new LinearLayoutManager(getApplicationContext());
        llm1.setOrientation(LinearLayoutManager.HORIZONTAL);
        lista1 = (RecyclerView) findViewById(R.id.recycler1);
        lista1.setHasFixedSize(true);
        lista1.setLayoutManager(llm1);


        //Lista 3
        LinearLayoutManager llm2 = new LinearLayoutManager(getApplicationContext());
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        lista2 = (RecyclerView) findViewById(R.id.recycler2);
        lista2.setHasFixedSize(true);
        lista2.setLayoutManager(llm2);


        JsonObjectRequest jsObjRequest =
                new JsonObjectRequest(
                        Request.Method.GET, // Requisição via HTTP_GET
                        url,   // url da requisição
                        null,  // JSONObject a ser enviado via POST
                        this,  // Response.Listener
                        this); // Response.ErrorListener

        int timeout = 20000; // 20 segundos
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(this).addRequest(jsObjRequest);


    }

    public static void addValores(String clicado){

        String produtosClicados[] =clicado.split(Pattern.quote("/"));

        soma = 0;
        acu = 0;
        eq = 0;

        ListaPD.clear();

        for (ProdutoItem pd : db.getAllProd()) {
            for (String c : produtosClicados) {
                if (pd.getId().equalsIgnoreCase(c)) {

                    ListaPD.add(pd);
                    break;
                }
            }
        }
        db.close();

        for (ProdutoItem pd : ListaPD) {
            soma += Float.parseFloat(pd.getPreco());
            if(pd.getApDesconto().equalsIgnoreCase("1")){
                acu += Float.parseFloat(pd.getPreco()) - Float.parseFloat(pd.getpDesconto());
            }
        }

        txtDesc.setText("" + acu);

        if(!txtDesc.getText().toString().equalsIgnoreCase("0.0")){
            txtTotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtTotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        eq = soma - acu;

        NumberFormat moeda;

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtTotal.setText(moeda.format(Double.parseDouble(""+soma)));
        txtDescTotal.setText(moeda.format(Double.parseDouble(""+eq)));

    }

    public static void addsubValores(String clicado){


        String produtosClicados[] =clicado.split(Pattern.quote("/"));

        soma = 0;
        acu = 0;
        eq = 0;

        ListaPD.clear();

        for (ProdutoItem pd : db.getAllProd()) {
            for (String c : produtosClicados) {
                if (pd.getId().equalsIgnoreCase(c)) {


                    ListaPD.add(pd);
                    break;
                }
            }
        }
        db.close();


        for (ProdutoItem pd : ListaPD) {
            soma += Float.parseFloat(pd.getPreco());
            if(pd.getApDesconto().equalsIgnoreCase("1")){
                acu += Float.parseFloat(pd.getPreco()) - Float.parseFloat(pd.getpDesconto());
            }
        }

        txtDesc.setText("" + acu);

        if(!txtDesc.getText().toString().equalsIgnoreCase("0.0")){
            txtTotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtTotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        eq = soma - acu;

        NumberFormat moeda;

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txtTotal.setText(moeda.format(Double.parseDouble(""+soma)));
        txtDescTotal.setText(moeda.format(Double.parseDouble(""+eq)));
    }

    @Override
    public void onResponse(JSONObject response) {
        produto1 = new ArrayList<ProdutoItem>();
        produto2 = new ArrayList<ProdutoItem>();
        produto3 = new ArrayList<ProdutoItem>();

        try {

            JSONArray jsonComboP =
                    response.getJSONArray("combos");

            for(i = 0; i < jsonComboP.length(); i++){

                    JSONObject jsonMercadoItem =
                            jsonComboP.getJSONObject(i);

                    String num_lista =
                            jsonMercadoItem.getString("num_lista");
                    String id =
                            jsonMercadoItem.getString("id_produto");
                    //String nome = jsonMercadoItem.getString("nome_produto");

                    String nome = new String(jsonMercadoItem.getString("nome_produto").getBytes("ISO-8859-1"),"UTF-8");

                    //String descricao = jsonMercadoItem.getString("descricao_produto");

                    String descricao = new String(jsonMercadoItem.getString("descricao_produto").getBytes("ISO-8859-1"),"UTF-8");

                    String preco =
                            jsonMercadoItem.getString("preco_produto");

                    String sobre =
                        jsonMercadoItem.getString("sobreapp");

                    if(sobre.equalsIgnoreCase("null")){
                        sobre = "64359b7192746a14740ad4bb7afe4e097327d0790190fd16";
                    }

                    //String marca = jsonMercadoItem.getString("marca_produto");

                    String marca = new String(jsonMercadoItem.getString("marca_produto").getBytes("ISO-8859-1"),"UTF-8");

                    String desconto =
                            jsonMercadoItem.getString("vdesconto");
                    String apdesconto =
                            jsonMercadoItem.getString("add_desconto");
                    String imagem =
                            jsonMercadoItem.getString("imagem_produto");

                    String imagemCOMP = auxUrlimagem + imagem;

                String quantidade = "1";

                    ProdutoItem prodItem = new ProdutoItem(nome, descricao, preco, marca, imagemCOMP, id, desconto, apdesconto, ""+b.get("id"),auxIdMercado,sobre,quantidade);
                    //produto.add(prodItem);
                    // Toast.makeText(this, id + "->:"+ nome    , Toast.LENGTH_SHORT).show();

                //Verificação de qual linha pertence
                    switch(num_lista){
                        case "1":
                            produto1.add(prodItem);
                            break;
                        case "2":
                            produto2.add(prodItem);
                            break;
                        case "3":
                            produto3.add(prodItem);
                            break;
                    }

                    db.addProd(prodItem);
                    contador_prod++;
                }

                mAdapter = new GridAdapter2(produto1, "1" );

                lista0.setAdapter(mAdapter);

                mAdapter = new GridAdapter2(produto2, "2");
                lista1.setAdapter(mAdapter);

                mAdapter = new GridAdapter2(produto3, "3");
                lista2.setAdapter(mAdapter);


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Erro de conexão!",
                Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, noWifi.class));
    }

    public void prodBotao(){



        //Verifico se o produto foi clicado
        SharedPreferences produtosClicados = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String auxProdClicado = produtosClicados.getString("prodClicado", "");

        //VERIFICAÇÃO DE LISTA VAZIA
        contador_notlistavazia = 0;

        String prodclicado[] = auxProdClicado.split(Pattern.quote("/"));

        contador_prod_json = 0;
        contador_prodlinha = 0;
        status = 0;
        status1 = 0;
        status2 = 0;
        status3 = 0;

        for(String a: prodclicado){
            if(!a.equalsIgnoreCase("") && !a.equalsIgnoreCase(" ")){
                if(config.equalsIgnoreCase("2")){
                    if(!produto1.isEmpty()){
                        for(ProdutoItem p: produto1){
                            if(p.getId().equalsIgnoreCase(a)){
                                if(status1 == 0){
                                    status1++;
                                }
                                break;
                            }
                        }
                    }else{
                        status++;
                    }
                    if(!produto2.isEmpty()){
                        for(ProdutoItem p: produto2){
                            if(p.getId().equalsIgnoreCase(a)){
                                if(status2 == 0){
                                    status2++;
                                }
                                break;
                            }
                        }
                    }else{
                        status++;
                    }
                    if(!produto3.isEmpty()){
                        for(ProdutoItem p: produto3){
                            if(p.getId().equalsIgnoreCase(a)){
                                if(status3 == 0){
                                    status3++;
                                }
                                break;
                            }
                        }
                    }else{
                        status++;
                    }
                }
                contador_prod_json++;
            }
        }

        status += status + status1 + status2 + status3;



        SharedPreferences produtosPref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxiliando = produtosPref.getString("logado", "");
        SharedPreferences produtosPref2 = getApplication().getSharedPreferences("SessaoUserFULL", Context.MODE_PRIVATE);
        String auxiliando2 = produtosPref2.getString("preenchido", "");
            if(auxiliando.length() > 0) {
                if(!auxiliando2.equalsIgnoreCase("0")){
                    if(config.equalsIgnoreCase("2") && status == 3){
                        Intent intent = new Intent(getApplicationContext(), AFechamento.class);
                        finish();
                        startActivity(intent);
                    }else {
                        if (config.equalsIgnoreCase("1") && contador_prod_json == contador_prod) {
                            Intent intent = new Intent(getApplicationContext(), AFechamento.class);
                            finish();
                            startActivity(intent);
                        } else {
                            switch(config){
                                case "1":
                                    MyToast("Por favor, selecione todos os produtos! Este combo é vendido apenas na modalidade completo");
                                    //Toast.makeText(getApplicationContext(), "Por favor, selecione todos os produtos!", Toast.LENGTH_LONG).show();
                                    break;
                                case "2":
                                    MyToast("Por favor, selecione um produto por linha pelo menos! Este combo é vendido na modalidade fracionado");
                                    // Toast.makeText(getApplicationContext(), "Por favor, selecione um produto por linha pelo menos!", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Erro de configuração de combo!", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Por favor, faça o cadastro completo!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), AlterarNavigation.class);
                    finish();
                    startActivity(intent);
                }
                //vertificar se todos os campos tão preenchidos
            }else{
                //Se não tem nada eu vou pra login navigation -> cadastrar etc.
                Toast.makeText(getApplicationContext(),"Por favor, conecte-se ao aplicativo!",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LoginNavigation.class);
                finish();
                startActivity(intent);
            }

    }

    public void MyToast(String msg){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.alert);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.produto_item_activity, menu);


        menu.findItem(R.id.action_share);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:

                String log_url = auxUrl+"makeJsonComboPublicidade.php?id_combo="+b.get("id");

                StringRequest stringRequest = new StringRequest(Request.Method.GET, log_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try{

                                    JSONObject jObject  = new JSONObject(response); // json
                                    JSONArray jsonImg = jObject.getJSONArray("combopublic");

                                    for (int i = 0; i < jsonImg.length(); i++) {
                                        JSONObject jsonMercadoItem =
                                                jsonImg.getJSONObject(i);

                                        String img_public = jsonMercadoItem.getString("combo_public");


                                        if(img_public.equalsIgnoreCase("nulo")){
                                            Toast.makeText(getApplicationContext(), "Não disponível para compartilhamento!", Toast.LENGTH_LONG).show();
                                        }else{
                                            shareItem(auxUrlimagem+img_public);
                                        }
                                    }

                                }catch(Exception e){
                                }


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {

                };


                int timeout= 20000; // 20 segundos
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleyProvider.getInstance(this).addRequest(stringRequest);


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    //*****************************************COMPARTILHAR PUBLICIDADE***********************************
    public void shareItem(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    //*****************************************TRANSFORMAR IMAGEM EM BITMAP******************************
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}
