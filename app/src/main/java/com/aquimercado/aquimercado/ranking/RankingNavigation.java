package com.aquimercado.aquimercado.ranking;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class RankingNavigation extends AppCompatActivity {

    String auxUrl, auxId, auxCidade, nomeTotal, id_clienteTotal, pontuacaoTotal, posicaoTotal, nomeSemanal, id_clienteSemanal, pontuacaoSemanal, posicaoSemanal;

    List<RankingItemNavigation> ListaRankingTotal = new ArrayList<RankingItemNavigation>();
    List<RankingItemNavigation> ListaRankingSemanal = new ArrayList<RankingItemNavigation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new MenuPrincipal(this.getApplicationContext(), this);


        //*************************************SHARED***********************************************
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        auxUrl = urlPref.getString("url_base", "");

        SharedPreferences urlPref2 = getApplication().getSharedPreferences("enderecoEntrega", Context.MODE_PRIVATE);
        auxCidade = urlPref2.getString("cidade", "");

        SharedPreferences urlPref3 = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        auxId = urlPref3.getString("id_cliente", "");



        try {
            auxCidade = URLEncoder.encode(auxCidade, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //****************************BUSCAR POSIÇÃO TOTAL******************************************

        String urlTotal = "";

        urlTotal = auxUrl + "makejsonPosicaoTotal.php?id_usuario=" + auxId + "&&cidade=" + auxCidade +"";


        Log.d("URLTOTALAQUIIIIIII", ""+urlTotal);

        StringRequest stringRequestTOTAL = new StringRequest(Request.Method.GET, urlTotal,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoPosicao;
                                JSONArray arrayPosicao = consultaArray.getJSONArray("pontuacaoTotal");


                                for(int i = 0; i < arrayPosicao.length(); i++){
                                    RankingItemNavigation rankingTotal = new RankingItemNavigation();
                                    infoPosicao = (JSONObject) arrayPosicao.get(i);

                                    rankingTotal.setId(infoPosicao.getString("id_cliente"));
                                    rankingTotal.setNome(infoPosicao.getString("nome_cliente"));
                                    rankingTotal.setPosicao(infoPosicao.getString("posicaoTotal"));
                                    rankingTotal.setPontuacao(infoPosicao.getString("pontostotais"));


                                    ListaRankingTotal.add(rankingTotal);
                                }

                                RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_pontosTotais);
                                rv.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                rv.setLayoutManager(llm);

                                GridRankingTotal mAdapter = new GridRankingTotal(ListaRankingTotal, auxId);
                                rv.setAdapter(mAdapter);

                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeoutTOTAL = 20000; // 20 segundos
        stringRequestTOTAL.setRetryPolicy(new DefaultRetryPolicy(
                timeoutTOTAL,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequestTOTAL);



        //****************************BUSCAR POSIÇÃO SEMANAL****************************************

       // String urlSemanal = auxUrl + "makejsonPosicaoSemanal.php";

        String urlSemanal = "";

        urlSemanal = auxUrl + "makePosicaoSemanal.php?id_usuario=" + auxId + "&&cidade=" + auxCidade +"";

        Log.d("URLSEMANALAQUIIIII", ""+urlSemanal);

        StringRequest stringRequestSEMANAL = new StringRequest(Request.Method.GET, urlSemanal,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArraySEMANAL = new JSONObject(data[0]);

                                JSONObject infoPosicaoSEMANAL;
                                JSONArray arrayPosicaoSEMANAL = consultaArraySEMANAL.getJSONArray("pontuacaoSemanal");


                                for(int j = 0; j < arrayPosicaoSEMANAL.length(); j++){

                                    infoPosicaoSEMANAL = (JSONObject) arrayPosicaoSEMANAL.get(j);

                                    Log.d("AQUIIIIIIIII", "---->"+infoPosicaoSEMANAL.getString("id_cliente"));
                                    Log.d("AQUIIIIIIIII", "---->"+infoPosicaoSEMANAL.getString("nome_cliente"));

                                    RankingItemNavigation rankingSemanal = new RankingItemNavigation(infoPosicaoSEMANAL.getString("nome_cliente"),infoPosicaoSEMANAL.getString("pontosSemanais"),infoPosicaoSEMANAL.getString("posicaoSemanal"),infoPosicaoSEMANAL.getString("id_cliente"));

                                    Log.d("AQUIIIIIIIII", "---->"+rankingSemanal.getNome());


                                    ListaRankingSemanal.add(rankingSemanal);
                                }

                                RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_pontosSemanais);
                                rv.setHasFixedSize(true);
                                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                rv.setLayoutManager(llm);

                                GridRankingSemanal mAdapter = new GridRankingSemanal(ListaRankingSemanal, auxId);
                                rv.setAdapter(mAdapter);

                            }
                        } catch (Exception e) {
                                Log.d("ERRORRRR",e.toString());
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeoutSEMANAL = 20000; // 20 segundos
        stringRequestSEMANAL.setRetryPolicy(new DefaultRetryPolicy(
                timeoutSEMANAL,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequestSEMANAL);

        //******************************************************************************************
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}



