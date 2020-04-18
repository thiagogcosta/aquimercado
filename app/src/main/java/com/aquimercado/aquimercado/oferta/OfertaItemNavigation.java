package com.aquimercado.aquimercado.oferta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.combo.ComboItemNavigation;
import com.aquimercado.aquimercado.departamento.DepartamentoItemNavigation;
import com.aquimercado.aquimercado.mercado.MercadoItemNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OfertaItemNavigation extends AppCompatActivity
        implements  Response.Listener<JSONObject>, Response.ErrorListener {

    private Toolbar toolbar;
    RecyclerView mRecyclerViewOfertas;
    RecyclerView.Adapter mAdapter;
    public View v;
    public static final String PREFS_NAME = "produtos";

    public static Intent intent = null;
    public static Bundle b = null;
    public static String auxUrlimagem = null;
    int status;
    public  Menu menu2;
public CountDownTimer counterDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta_item_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);




        //***************************************Pegando o ID **********************************
        intent = getIntent();
        b = intent.getExtras();


        // ***********************  verifica permissoes de Vendas
        SharedPreferences permissoes = getApplication().getSharedPreferences("Permite", Context.MODE_PRIVATE);
        final String permitedep = permissoes.getString("permitedep", "");
        String permiteoferta = permissoes.getString("permiteoferta", "");
        final String permitecombo = permissoes.getString("permitecombo", "");

        Log.d("permitedep",permitedep);
        Log.d("permitecombo",permitecombo);
        Log.d("permiteoferta",permiteoferta);

        if (permiteoferta.equals("0")) { // nao tem oferta
            if (permitecombo.equals("1")){ // tem combo
                Toast.makeText(getApplicationContext(), "Compra por Combos", Toast.LENGTH_SHORT).show();
                Intent intentDepto = new Intent(getApplicationContext(), ComboItemNavigation.class);
                intentDepto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentDepto.putExtra("id", "" + b.get("id"));
                intentDepto.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                intentDepto.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                intentDepto.putExtra("tipo", "ambos");
                intentDepto.putExtra("permitedepto", "1");
                startActivity(intentDepto);
                finish();
            }
            else{ // tem departamento
                Toast.makeText(getApplicationContext(), "Compra por Departamentos", Toast.LENGTH_SHORT).show();
                Intent intentDepto = new Intent(getApplicationContext(), DepartamentoItemNavigation.class);
                intentDepto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentDepto.putExtra("id", "" + b.get("id"));
                intentDepto.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                intentDepto.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                intentDepto.putExtra("tipo", "ambos");
                intentDepto.putExtra("permitedepto", "1");
                startActivity(intentDepto);
                finish();
            }

        }

        //Calling the RecyclerView
        mRecyclerViewOfertas = (RecyclerView) findViewById(R.id.recycler_view_ofertas);
        mRecyclerViewOfertas.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerViewOfertas.setLayoutManager(gridLayoutManager);


        //***************************************************************************************
        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        //URL IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        //recuperando latitude e logitude
        SharedPreferences sPlatitude = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        String latitude = sPlatitude.getString("latitude", "");
        String longitude = sPlatitude.getString("longitude", "");


        //NOME DO MERCADO
        SharedPreferences mercadoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edMercado = mercadoPref.edit();
        edMercado.putString("nome_mercado", "" + b.get("nome_mercado"));
        edMercado.apply();

        //LOGO DO MERCADO
        SharedPreferences mercadoPref2 = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edMercado2 = mercadoPref2.edit();
        edMercado2.putString("logo_mercado", "" + b.get("logo_mercado"));
        edMercado2.apply();

        //ID DO MERCADO
        SharedPreferences idPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edPref = idPref.edit();
        edPref.putString("id_mercado", "" + b.get("id"));
        edPref.apply();

        //limpando o shared dos produtos
        SharedPreferences settings = getApplicationContext().getSharedPreferences("prodInfo", Context.MODE_PRIVATE);
        settings.edit().clear().apply();


        String url = auxUrl + "makeJsonOfertas.php?id=" + b.get("id");

        Log.d("urlofertas=",url);

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
        VolleyProvider.getInstance(getBaseContext()).addRequest(jsObjRequest);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        new MenuPrincipal(this.getApplicationContext(), this);

        /*
        //***********************Reescrevendo Shared Preferences******************************
       */
        SharedPreferences produtosPref = getApplication().getSharedPreferences("Mercado", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        editor.putString("id", "" + b.get("id"));
        editor.apply();

        //************************************************************************/
        // verifica se o cliente esta na área de atendimento do mercado
        // Testa se pedido veio de uma cidade autorizada

        if (permitecombo.equals("1") || permitedep.equals("1")) {
            String urlLL = auxUrl + "validaRetirada.php?lat=" + latitude + "&lng=" + longitude + "&id_mercado=" + b.get("id");
            Log.d("regiao", urlLL);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLL,
                    new Response.Listener<String>() {
                        //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(String response) {
                            final String[] data = {""};
                            try {
                                if (response.toString().length() > 0 && response.toString() != " ") {
                                    data[0] = response.toString();
                                    JSONObject consultaArray = new JSONObject(data[0]);

                                    JSONObject infodistancia;
                                    JSONArray arrayPedidos = consultaArray.getJSONArray("retirada");
                                    infodistancia = (JSONObject) arrayPedidos.get(0);


                                    if (infodistancia.getString("status").equals("0")) {
                                        Toast.makeText(getApplicationContext(), "Você está fora da área de atendimento do estabelecimento " + b.get("nome_mercado"), Toast.LENGTH_SHORT).show();
                                        status = 0;
                                    }
                                    if (infodistancia.getString("status").equals("1")) {
                                        //   Toast.makeText(getApplicationContext(), "Você está dentro da área de atuacao do mercado", Toast.LENGTH_LONG).show();
                                        status = 1;
                                    }

                                    // -------------------   gravando status-------------------------
                                    // status da regiao de atendimento
                                    SharedPreferences regiao = getApplicationContext().getSharedPreferences("regiao", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor distancia = regiao.edit();
                                    distancia.putString("status", "" + status);
                                    distancia.apply();

                                    //NOME DO MERCADO
                                    SharedPreferences mercadoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor edMercado = mercadoPref.edit();
                                    edMercado.putString("nome_mercado", "" + b.get("nome_mercado"));
                                    edMercado.apply();

                                }
                            } catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyProvider.getInstance(this).addRequest(stringRequest);
        }
        // ############# Botao Combo
        ImageButton bcombo = (ImageButton) findViewById(R.id.bcombo);

        bcombo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (permitecombo.equals("1")){
                    Toast.makeText(getApplicationContext(), "Compra por Combos", Toast.LENGTH_SHORT).show();
                    Intent intentDepto = new Intent(getApplicationContext(), ComboItemNavigation.class);
                    intentDepto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentDepto.putExtra("id", "" + b.get("id"));
                    intentDepto.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intentDepto.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intentDepto.putExtra("tipo", "ambos");
                    intentDepto.putExtra("permitedepto", "1");
                    startActivity(intentDepto);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Este estabelecimento ainda não vende combos pelo aplicativo aquiMercado.com!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        // ############# Botao Departamento
        ImageButton bdepto = (ImageButton) findViewById(R.id.bdepto);

        bdepto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (permitedep.equals("1")) {

                    Toast.makeText(getApplicationContext(), "Compra por Departamentos", Toast.LENGTH_SHORT).show();
                    Intent intentDepto = new Intent(getApplicationContext(), DepartamentoItemNavigation.class);
                    intentDepto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentDepto.putExtra("id", "" + b.get("id"));
                    intentDepto.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intentDepto.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intentDepto.putExtra("tipo", "ambos");
                    intentDepto.putExtra("permitedepto", "1");
                    startActivity(intentDepto);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Este estabelecimento ainda não vende produtos por departamento no aplicativo aquiMercado.com!", Toast.LENGTH_SHORT).show();

                }
            }
        });






    }


    @Override
    public void onBackPressed() {
        super.onResume();

        SharedPreferences latlgn = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        String latitude = latlgn.getString("latitude", "");
        String longitude = latlgn.getString("longitude", "");
        Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("latitude", Double.parseDouble(latitude));
        intent.putExtra("longitude", Double.parseDouble(longitude));
        Log.d("lat-lgn", latitude+" - "+longitude);
        startActivity(intent);
        finish();
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        List<OfertaItem> Ofertalist = new ArrayList<OfertaItem>();
        final String[] data = {""};


        try {
            if (response.toString().length() > 0 && response.toString() != " ") {

                data[0] = response.toString();
                JSONObject consultaArray = new JSONObject(data[0]);

                JSONObject infoOferta;
                JSONArray arrayOferta = consultaArray.getJSONArray("produtos");

                for (int i = 0; i < arrayOferta.length(); i++) {
                    infoOferta = (JSONObject) arrayOferta.get(i);

                    String id_produto = infoOferta.getString("id_produtos");
                    String nome_produto = infoOferta.getString("nome_produto");
                    String departamento = infoOferta.getString("departamento");
                    String preco_produto = infoOferta.getString("preco_produto");
                    String imagem = infoOferta.getString("imagem_produto");
                    String imagemOferta = auxUrlimagem + imagem;
                    String marca = infoOferta.getString("marca_produto");
                    String descricao = infoOferta.getString("descricaofull");


                    OfertaItem ofertaItem = new OfertaItem(id_produto, b.get("id").toString(), nome_produto,
                            departamento, preco_produto,
                            imagemOferta, marca,descricao);

                    Ofertalist.add(ofertaItem);

                }

                mRecyclerViewOfertas.setVisibility(View.VISIBLE);
                mAdapter = new GridAdapterOferta(Ofertalist);
                mRecyclerViewOfertas.setAdapter(mAdapter);

            }
        } catch (Exception e) {
        }

    }



    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences produtosPref = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        editor.clear();
        editor.apply();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

        return true;
    }*/
    long timer = 12000;

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.oferta_item_navigation, menu);

        final MenuItem counter = menu.findItem(R.id.counter);

        counterDown = new CountDownTimer(timer, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String  hms =  (TimeUnit.MILLISECONDS.toHours(millis))+":"+
                        (TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)))+":"+ (TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

                counter.setTitle(hms);
                timer = millis;

            }

            public void onFinish() {
                counter.setTitle("");
                counter.setIcon(R.drawable.checked);
                String id_mercado = b.get("id")+"";
                SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                String id_cliente = clienteid.getString("id_cliente", "");
                addPontos( id_mercado, id_cliente,"1","0"); // adicona um ponto por entrar na oferta
            }
        }.start();

        return  true;

    }


    // Verifica se esta proximo a algum mercado
    public void addPontos(String id_mercado,String id_cliente, String pontos, String tipo){


        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl2 = auxUrl + "insertPontos.php?id_cliente="+id_cliente+"&id_mercado="+id_mercado+"&pontos="+pontos+"&tipo="+tipo;
        Log.d("urlpontos=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        /* --------------- Retarda o timeout VOLLEY... evita o reenvio da requisicao*/
        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }



}