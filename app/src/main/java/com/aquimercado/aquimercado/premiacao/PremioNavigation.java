package com.aquimercado.aquimercado.premiacao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.mercado.MercadoItemNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class PremioNavigation extends AppCompatActivity {


    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;
    public static final String PREFS_NAME = "Produtos";
    GridAdapterPremio mAdapter;
    public static String auxUrlimagem = null;
    public Context ctx;
    List<PremioItem> premios;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premio_item_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_premio);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutFrozen(true);
        gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        premios = new ArrayList<PremioItem>();
        mAdapter = new GridAdapterPremio(premios);
        mRecyclerView.setAdapter(mAdapter);
        // captura latitude e longitude do smartphone (vem do splashscreenFirt)
        Intent intent = getIntent();
         b = intent.getExtras();


        //URL da IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        //URL da IMAGEM
        SharedPreferences sCidade = getApplication().getSharedPreferences("Cidade", Context.MODE_PRIVATE);
        String cidade  = sCidade.getString("cidade", "");

        try {
            cidade = URLEncoder.encode(cidade, "UTF-8");

        }catch (Exception e){}
        verificaPremios(cidade);
    }


    public void verificaPremios(String cidade){

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "verificaPremios.php?cidade="+cidade;

        Log.d("urlpremios=",strUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {

                                JSONObject consultaArray = new JSONObject(response);
                                JSONArray jsonMercado = consultaArray.getJSONArray("premios");
                                PremioItem premioItem;
                                for (int i = 0; i < jsonMercado.length(); i++) {
                                    JSONObject jsonMercadoItem =
                                            jsonMercado.getJSONObject(i);
                                    String nome_produto =
                                            jsonMercadoItem.getString("nome_produto");
                                    String nome_mercado =
                                            jsonMercadoItem.getString("nome_mercado");
                                    String descricaopremio =
                                            jsonMercadoItem.getString("descricaopremio");
                                    String vencimento =
                                            jsonMercadoItem.getString("vencimento");
                                    vencimento = "Venc.: "+vencimento;

                                    String tipo =
                                            jsonMercadoItem.getString("tipo");

                                    /*
                                     0 - Ranking geral
                                     1 - Ranking por Supermercado
                                     2 - Sorteio no mercacado
                                    */

                                    String imagem =
                                            jsonMercadoItem.getString("imagem_mercado");
                                    String imagemOK = auxUrlimagem + imagem;

                                    String imagem_produto =
                                            jsonMercadoItem.getString("imagem_produto");
                                    String imagemOK2 = auxUrlimagem + imagem_produto;

                                    Log.d("img=",nome_mercado);
                                    Log.d("imgok=",imagemOK);

                                     premioItem = new PremioItem(
                                            nome_produto,
                                            nome_mercado,
                                             imagemOK2,
                                            imagemOK,
                                             descricaopremio,
                                             vencimento,
                                             tipo);
                                    premios.add(premioItem);

                                }
                                mAdapter = new GridAdapterPremio(premios);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                        } catch (Exception e) {

                        }
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



    @Override
    public void onBackPressed() {
        super.onResume();
        Double latitude = (Double) b.get("latitude");
        Double longitude = (Double) b.get("longitude");
        Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("latitude", latitude );
        intent.putExtra("longitude", longitude);
        startActivity(intent);
        finish();
    }


}
