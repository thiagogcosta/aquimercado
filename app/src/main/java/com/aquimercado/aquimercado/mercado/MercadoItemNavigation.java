package com.aquimercado.aquimercado.mercado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.config_oferta.NotificacaoOfertaNavigation;
import com.aquimercado.aquimercado.notificacao.NotificationEventReceiverOfertas;
import com.aquimercado.aquimercado.premiacao.PremioNavigation;
import com.aquimercado.aquimercado.ranking.RankingNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MercadoItemNavigation extends AppCompatActivity {

     Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    StaggeredGridLayoutManager gridLayoutManager;
    GridAdapter3 mAdapter;
    public static final String PREFS_NAME = "produtos";
    public static String auxUrlimagem = null;
    AutoCompleteTextView searchedt;
    public Context ctx;
    List<MercadoItem> mercados;
    int previousTotal = 0;
    int visibleThreshold = 50;
    int page=1;
    ProgressBar progressBar;
    int ibreak=0;
    private Double latitude;
    private Double longitude;
    private String auxStatusUsuario;
    private String versao;

    TextView txtpontos;
    TextView txtcidadeestado;

    String txtcidade;
    TextView txtpontossemanais;

    MenuItem msobre;
    MenuItem mnotificacoes;
    MenuItem mranking;
    MenuItem mnotifica;
    MenuItem mpremios;

    String msgnotifica;
    String datahoranotifica;
    String id_mensagem;

    boolean loading = true;
    int pastVisibleItems,firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercado_item_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        NotificationEventReceiverOfertas.setupAlarm(getApplicationContext());


       // configMercado();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mercado);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutFrozen(true);

        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        progressBar  = (ProgressBar) findViewById(R.id.progressBar1);
        txtpontos  = (TextView) findViewById(R.id.pontos);
        txtcidadeestado  = (TextView) findViewById(R.id.txtcidadeestado);
        //notifica = (MenuItem) findViewById(R.id.imbnotfica);
        txtpontossemanais  = (TextView) findViewById(R.id.pontossemanais);
        txtpontos.setText("0");
        txtpontossemanais.setText("0");
        progressBar.setVisibility(View.INVISIBLE);
        //********************************************URL JSON*******************************************//

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        //URL da IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        //URL da IMAGEM
        SharedPreferences prefversao = getApplication().getSharedPreferences("VERSAO", Context.MODE_PRIVATE);
        versao = prefversao.getString("numero", "");


        SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String id_cliente = clienteid.getString("id_cliente", "");

        final String id_cliente2 = clienteid.getString("id_cliente", "");
        auxStatusUsuario = clienteid.getString("tipo","");

        // captura latitude e longitude do smartphone (vem do splashscreenFirt)
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (getIntent().getBooleanExtra("EXIT", false)) {
            SharedPreferences prefVarVersao = getApplication().getSharedPreferences("VERSAO", Context.MODE_PRIVATE);
            SharedPreferences.Editor editVarVersao = prefVarVersao.edit();
            editVarVersao.putString("flag", "");
            editVarVersao.apply();
            finish();
        }else {
            latitude = (Double) b.get("latitude");
            longitude = (Double) b.get("longitude");
        }
         mercados = new ArrayList<MercadoItem>();
         mAdapter = new GridAdapter3(mercados,progressBar);
         mRecyclerView.setAdapter(mAdapter);
         mRecyclerView.setNestedScrollingEnabled(false);

        startLoadMercado(latitude,longitude, id_cliente);
        verificaCoord( latitude,  longitude, id_cliente);

        mRecyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        if(dy > 0) //check for scroll down
                        {
                            visibleItemCount = gridLayoutManager.getChildCount();
                            totalItemCount = gridLayoutManager.getItemCount();
                            int[] firstVisibleItems = null;
                            firstVisibleItems = gridLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                            if(firstVisibleItems != null && firstVisibleItems.length > 0) {
                                firstVisibleItem = firstVisibleItems[0];
                            }

                            if (loading) {
                                if (totalItemCount > previousTotal) {
                                    loading = false;
                                    previousTotal = totalItemCount;
                                    int idx = visibleThreshold-((page*visibleThreshold)-totalItemCount); //quantos elementos tem na última página
                                    mRecyclerView.scrollToPosition(previousTotal-idx);
                                    if (idx!=visibleThreshold){ibreak=1;}
                                }
                            }
                            if (!loading && ((previousTotal-firstVisibleItem))<=6) {

                                if (ibreak==0){
                                    page++;

                                    buscaMercados(latitude, longitude,id_cliente2);
                                    loading = true;
                                }
                            }
                        }
                    }
                });

        //*****************************Guardo no shared a latitude e longitude************************************
        SharedPreferences sPlatitude = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorLatitude = sPlatitude.edit();
        editorLatitude.putString("latitude",""+latitude);
        editorLatitude.apply();

        SharedPreferences sPlongitude = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorLongitude = sPlongitude.edit();
        editorLongitude.putString("longitude",""+longitude);
        editorLongitude.apply();

        //*******************************************************************************************************


        //********************************Shared preferences limpeza******************************
        SharedPreferences produtosPref = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        editor.clear();
        editor.apply();
        //****************************************************************************************

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        new MenuPrincipal(this.getApplicationContext(), this);



        String flag = prefversao.getString("flag", "");
        Log.d("flag1=","="+flag);

        if (flag.isEmpty()){
            SharedPreferences.Editor editVarVersao = prefversao.edit();
            editVarVersao.putString("flag", "1");
            editVarVersao.apply();
            if (id_cliente.isEmpty()){
                id_cliente= "-1";
                //txtpontos.setText("Você não está logado");
            }
            validaVersao(versao, id_cliente); // Esta automatico pelo google play deveploper

        }else{
            if (!id_cliente.isEmpty()){
                validaPedidosProntos(id_cliente);
                consultaPontos(id_cliente);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mercado_item_navigation, menu);


         msobre = menu.findItem(R.id.menusobre);
         mnotificacoes = menu.findItem(R.id.menunotificacoes);
        mranking = menu.findItem(R.id.menuranking);
        mnotifica = menu.findItem(R.id.menumsg);
        mpremios = menu.findItem(R.id.menupremios);
        //MenuItem pontos = menu.findItem(R.id.mpontototais);
        //pontos.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT|MenuItem.SHOW_AS_ACTION_IF_ROOM);
       // pontos.setIcon(R.drawable.coin);

        mranking.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent intent = new Intent(getApplicationContext(), RankingNavigation.class);
                startActivity(intent);


                return false;
            }
        });

        mnotifica.setVisible(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menusobre:
                /* intent = new Intent(getApplicationContext(), ComboItemNavigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", "" );
                intent.putExtra("nome_mercado", "");
                intent.putExtra("logo_mercado", "");
                intent.putExtra("tipo", "entrega");
                startActivity(intent);
                finish();*/
                return true;
            case R.id.menunotificacoes:
                intent = new Intent(getApplicationContext(), NotificacaoOfertaNavigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("latitude", latitude );
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                finish();
                return true;
            case R.id.menuranking:
                /*intent = new Intent(getApplicationContext(), ComboItemNavigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", "" );
                intent.putExtra("nome_mercado", "");
                intent.putExtra("logo_mercado", "");
                intent.putExtra("tipo", "entrega");
                startActivity(intent);
                finish();*/
                return true;
            case R.id.menupremios:
                intent = new Intent(getApplicationContext(), PremioNavigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("latitude", latitude );
                intent.putExtra("longitude", longitude);
                startActivity(intent);
                finish();
                return true;
            case R.id.menumsg:
               mnotifica.setVisible(false);
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MercadoItemNavigation.this);
                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                                String id_cliente = clienteid.getString("id_cliente", "");
                                insertMsgLida(id_cliente, id_mensagem);

                            }
                        });

                        builder.setMessage(msgnotifica)
                                .setTitle("Mensagem para você");
                        // Setting Icon to Dialog

                        builder.setIcon(R.drawable.alert);
                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();





                /*intent = new Intent(getApplicationContext(), ComboItemNavigation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("id", "" );
                intent.putExtra("nome_mercado", "");
                intent.putExtra("logo_mercado", "");
                intent.putExtra("tipo", "entrega");
                startActivity(intent);
                finish();*/
                return true;



            default:
                return super.onOptionsItemSelected(item);

        }
    }




    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Sair")
                .setMessage("Você deseja sair do aplicativo?")
                .setIcon(R.drawable.close)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }

                })
                .setNegativeButton("Não", null)
                .show();
    }



    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String id_cliente = clienteid.getString("id_cliente", "");
        consultaPontos( id_cliente);

    }


    public void validaPedidosProntos(String id_cliente){
        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "verificaPedidosProntos.php?id_cliente="+id_cliente;





        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                //String status ="0";
                                if (!consultaArray.isNull("prontos")) {

                                    JSONObject infoPedido;

                                    JSONArray arrayPedidos =  consultaArray.getJSONArray("prontos");

                                    infoPedido = (JSONObject) arrayPedidos.get(0);

                                    if (infoPedido.getString("status_pronto").equals("1")){
                                        //Toast.makeText(MercadoItemNavigation.this, "MEUS PEDIDOS: Você tem pedido(s) pronto(s)!", Toast.LENGTH_SHORT).show();
                                        MyToast("MEUS PEDIDOS: Você tem pedido(s) pronto(s)!",0);

                                    }
                                    if (infoPedido.getString("status_processamento").equals("1")){
                                        //Toast.makeText(MercadoItemNavigation.this, "MEUS PEDIDOS: Você tem pedido(s) em processamento!", Toast.LENGTH_SHORT).show();
                                        MyToast("MEUS PEDIDOS: Você tem pedido(s) em processamento!",0);
                                    }

                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //queue.add(stringRequest);
        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }

    public void buscaMercados(Double latitude, Double longitude,String id_cliente){

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl2 = auxUrl + "listaMercados.php?lat="+latitude+"&lng="+longitude+"&page="+page+"&numitens="+visibleThreshold+"&id_cliente="+id_cliente;

        Log.d("urlmercado=","aqui");
        Log.d("urlmercado=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {


                                JSONObject consultaArray = new JSONObject(response);
                                JSONArray jsonMercado = consultaArray.getJSONArray("mercados");


                                for (int i = 0; i < jsonMercado.length(); i++) {
                                    JSONObject jsonMercadoItem =
                                            jsonMercado.getJSONObject(i);
                                    String nome =
                                            jsonMercadoItem.getString("nome");

                                    String marca =jsonMercadoItem.getString("nome");

                                    String imagem =
                                            jsonMercadoItem.getString("imagem_mercado");
                                    String imagemOK = auxUrlimagem + imagem;

                                    String id =
                                            jsonMercadoItem.getString("id");
                                    String conta =
                                            jsonMercadoItem.getString("conta");
                                    if (conta.equals("1")) {conta = "Combo: "+conta;} else {conta = "Combos: "+conta;}

                                    String distancia =
                                            jsonMercadoItem.getString("distancia");

                                    String status =
                                            jsonMercadoItem.getString("status");


									String sobre = jsonMercadoItem.getString("sobreapp");

                                    // captura pontos do cliente em cada mercado
                                    String pontos = // = "400 pts";
                                    jsonMercadoItem.getString("pontos");

                                     pontos = pontos+" pts";

                                    if(sobre.equalsIgnoreCase("null")||sobre.isEmpty()){
                                        sobre = "64359b7192746a14740ad4bb7afe4e097327d0790190fd16";
                                    }


                                    Double dist = Double.parseDouble(distancia);
                                    DecimalFormat df = new DecimalFormat("0.00");

                                   // Log.d("status_usuario", "->"+auxStatusUsuario);

                                    if(auxStatusUsuario.equalsIgnoreCase("1") && status.equalsIgnoreCase("0")||(auxStatusUsuario.equalsIgnoreCase("8") && status.equalsIgnoreCase("7"))){
                                        MercadoItem mercadoItem = new MercadoItem(nome, imagemOK, conta, id, df.format(dist)+" Km", sobre, pontos);
                                        mercados.add(mercadoItem);
                                    }else{
                                        if(status.equalsIgnoreCase("0")) {
                                            MercadoItem mercadoItem = new MercadoItem(nome, imagemOK, conta, id, df.format(dist) + " Km", sobre,pontos);
                                            mercados.add(mercadoItem);
                                        }
                                    }
                                }
                                mAdapter = new GridAdapter3(mercados,progressBar);
                                mRecyclerView.setAdapter(mAdapter);
                                progressBar.setVisibility(View.INVISIBLE);

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


    public void startLoadMercado(Double latitude, Double longitude, String id_cliente){

        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "listaMercados.php?lat="+latitude+"&lng="+longitude+"&page="+page+"&numitens="+visibleThreshold+"&id_cliente="+id_cliente;

        Log.d("urlmercado=",strUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {

                                JSONObject consultaArray = new JSONObject(response);
                                JSONArray jsonMercado = consultaArray.getJSONArray("mercados");

                                for (int i = 0; i < jsonMercado.length(); i++) {
                                    JSONObject jsonMercadoItem =
                                            jsonMercado.getJSONObject(i);
                                    String nome =
                                            jsonMercadoItem.getString("nome");

                                    String marca = jsonMercadoItem.getString("nome");

                                    String imagem =
                                            jsonMercadoItem.getString("imagem_mercado");
                                    String imagemOK = auxUrlimagem + imagem;

                                    String id =
                                            jsonMercadoItem.getString("id");
                                    String conta =
                                            jsonMercadoItem.getString("conta");
                                    if (conta.equals("1")) {conta = "Combo: "+conta;} else {conta = "Combos: "+conta;}

                                    String distancia =
                                            jsonMercadoItem.getString("distancia");
                                   // distancia = distancia;


                                    String ender =
                                            jsonMercadoItem.getString("ender");

                                    String sobre = jsonMercadoItem.getString("sobreapp");

                                    String cidade = jsonMercadoItem.getString("cidade");
                                    txtcidade = cidade;
                                    //*****************************Guardo no shared a latitude e longitude************************************
                                    SharedPreferences sCidade = getApplication().getSharedPreferences("Cidade", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editorCidade = sCidade.edit();
                                    editorCidade.putString("cidade",""+cidade);
                                    editorCidade.apply();
                                   ///////////////////////////////////
                                    String estado = jsonMercadoItem.getString("estado");
                                    txtcidadeestado.setText(cidade+","+estado);

                                    String status =
                                            jsonMercadoItem.getString("status");

                                    // captura pontos do cliente em cada mercado
                                    String pontos = //"400 pts";
                                           jsonMercadoItem.getString("pontos");

                                    pontos = pontos+" pts";

                                    if(sobre.equalsIgnoreCase("null")||sobre.isEmpty()){
                                        //sobre = "64359b7192746a14740ad4bb7afe4e097327d0790190fd16";
                                        sobre = marca+"\n"+ender;
                                    }else{
                                        sobre = marca+"\n"+ender+"\n\n"+sobre;
                                    }


                                    Double dist = Double.parseDouble(distancia);
                                    DecimalFormat df = new DecimalFormat("0.00");

                                   // Log.d("status_usuario", "->"+auxStatusUsuario);
                                   // Log.d("status_mercado", "->"+status);

                                    if(auxStatusUsuario.equalsIgnoreCase("8") && (status.equalsIgnoreCase("7") || status.equalsIgnoreCase("9"))){
                                        MercadoItem mercadoItem = new MercadoItem(nome, imagemOK, conta, id, df.format(dist)+" Km", sobre, pontos);
                                        mercados.add(mercadoItem);
                                    }else {
                                        //if (auxStatusUsuario.equalsIgnoreCase("1") && (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("9"))) {
                                        if (auxStatusUsuario.equalsIgnoreCase("1") && status.equalsIgnoreCase("0")) {

                                            MercadoItem mercadoItem = new MercadoItem(nome, imagemOK, conta, id, df.format(dist) + " Km", sobre,pontos);
                                            mercados.add(mercadoItem);
                                        }else {
                                         //   if(status.equalsIgnoreCase("0")  && !auxStatusUsuario.equalsIgnoreCase("8")) {

                                             if((status.equalsIgnoreCase("0") || status.equalsIgnoreCase("9")) && !auxStatusUsuario.equalsIgnoreCase("8")) {
                                                MercadoItem mercadoItem = new MercadoItem(nome, imagemOK, conta, id, df.format(dist) + " Km", sobre,pontos);
                                                mercados.add(mercadoItem);
                                            }
                                        }
                                    }

                                }

                                SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                                final String id_cliente = clienteid.getString("id_cliente", "");

                                verificaMsg(id_cliente);


                                mAdapter = new GridAdapter3(mercados,progressBar);
                                mRecyclerView.setAdapter(mAdapter);
                                progressBar.setVisibility(View.INVISIBLE);

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

    public void MyToast(String msg, Integer tipo){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.alert);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        if (tipo==0)
            toast.setDuration(Toast.LENGTH_LONG);
        else
            toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void validaVersao(final String versao, final String id_cliente){
        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "validaVersao.php";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                if (!consultaArray.isNull("versao")) {

                                    JSONObject infoPedido;

                                    JSONArray arrayPedidos =  consultaArray.getJSONArray("versao");

                                    infoPedido = (JSONObject) arrayPedidos.get(0);

                                    Log.d("numero=",versao);
                                    Log.d("versao=",infoPedido.getString("numero"));
                                    if (!infoPedido.getString("numero").equals(versao)){
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MercadoItemNavigation.this);

                                        alertDialog.setTitle("Atualização Disponível");

                                        alertDialog.setMessage("Deseja Instalar Agora?");

                                        alertDialog.setIcon(R.drawable.mapmaker2);

                                        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {


                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                                        Uri.parse("https://play.google.com/store/apps/details?id=com.app.aquimercado"));
                                                startActivity(intent);
                                               // Intent intent = new Intent(Intent.ACTION_VIEW);
                                               // intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                                //startActivityForResult(intent, RC_INSTALL);
                                            }});

                                        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.cancel();
                                            }
                                        });

                                        alertDialog.show();

                                    }else{
                                        if (!id_cliente.equals("-1")){
                                            validaPedidosProntos(id_cliente);
                                        }
                                    }

                                }
                            }

                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }

   // Verifica se esta proximo a algum mercado
   public void verificaCoord(Double latitude, Double longitude, String id_cliente){

       progressBar.setVisibility(View.VISIBLE);

       SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
       String auxUrl = urlPref.getString("url_base", "");

       String strUrl2 = auxUrl + "verificaCoord.php?lat="+latitude+"&lng="+longitude+"&id_cliente="+id_cliente;

       //Log.d("urlmercado=","aqui");
      // Log.d("urlcoordenada=",strUrl2);

       StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
               new Response.Listener<String>() {
                   //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                   @Override
                   public void onResponse(String response) {
                       final String[] data = {""};
                       try {
                           if(response.toString().length() > 0 && response.toString() != " ") {


                               JSONObject consultaArray = new JSONObject(response);
                               JSONArray jsonMercado = consultaArray.getJSONArray("coord");

                               Log.d("tamcoord=",jsonMercado.length()+"");


                               for (int i = 0; i < jsonMercado.length(); i++) {
                                   JSONObject jsonMercadoItem =
                                           jsonMercado.getJSONObject(i);
                                   final String nome = jsonMercadoItem.getString("nome");
                                   String marca = jsonMercadoItem.getString("nome");
                                   String imagem = jsonMercadoItem.getString("imagem_mercado");
                                   final String imagemOK = auxUrlimagem + imagem;
                                   String id = jsonMercadoItem.getString("id");
                                   String pontos = jsonMercadoItem.getString("pontos");

                                   String distancia = jsonMercadoItem.getString("distancia");

                                   //String status = jsonMercadoItem.getString("status");
                                  //String sobre = jsonMercadoItem.getString("sobreapp");
                                   Double dist = Double.parseDouble(distancia);
                                   final DecimalFormat df = new DecimalFormat("0.00");



                                   String id_mercado = id;

                                   SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                                  final String id_cliente = clienteid.getString("id_cliente", "");

                                   android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MercadoItemNavigation.this);
                                   builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           //Log.d("confirmar=",id_cliente+"nada");
                                           if (!id_cliente.isEmpty() ){

                                                consultaPontos(id_cliente);

                                           }

                                       }
                                   });
                                   /*builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                       public void onClick(DialogInterface dialog, int id) {
                                           // User cancelled the dialog
                                       }
                                   });*/
                                   String msg = "";
                                   if (!pontos.equals("0")){ msg="\n\n ###  Você já pontuou hoje  ###";}
                                 // Log.d("id_cliente==",id_cliente);
                                   if (id_cliente.equals("") || id_cliente.isEmpty()  ){msg = "\n" +
                                           "\nVocê não está logado! \n" +
                                           "Seus pontos não serão acumulados";
                                           }

                                   if (!msg.equals("")){
                                       MyToast("Você está no supermercado " + nome + msg,1);
                                   }
                                   else {
                                       builder.setMessage("Você está no supermercado " + nome + msg)
                                               .setTitle("Prêmio - 10 pontos");
                                       // Setting Icon to Dialog

                                       if (!id_cliente.isEmpty()) {
                                           addPontos(id_mercado, id_cliente, "10", "1"); // adicona um ponto por entrar na oferta
                                       }
                                       builder.setIcon(R.drawable.alert);
                                       android.app.AlertDialog dialog = builder.create();
                                       dialog.show();
                                   }
                               }
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


    // Verifica se esta proximo a algum mercado
    public void consultaPontos(String id_cliente){

        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl2 = auxUrl + "consultaPontos.php?id_cliente="+id_cliente;

        //Log.d("urlmercado=","aqui");
        Log.d("urlcoordenada=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {


                                JSONObject consultaArray = new JSONObject(response);
                                JSONArray jsonMercado = consultaArray.getJSONArray("pontos");


                                for (int i = 0; i < jsonMercado.length(); i++) {
                                    JSONObject jsonMercadoItem =
                                            jsonMercado.getJSONObject(i);
                                     String pontostotais = jsonMercadoItem.getString("pontostotais");
                                    String pontossemanais = jsonMercadoItem.getString("pontossemanais");
                                    if (!pontostotais.equals("null") && !pontossemanais.equals("null")) {
                                        if (!txtpontos.getText().equals(pontostotais)){
                                            if (!txtpontos.getText().equals("0")){
                                                Vibrator v = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);
                                                v.vibrate(100);
                                            }
                                            txtpontos.setText(pontostotais);
                                            txtpontossemanais.setText(pontossemanais);

                                        }

                                    }
                                    else{

                                        MyToast("Você não está logado! Se ainda não tem uma conta, crie acessando o Menu",0);}

                                }
                            } else{
                                txtpontos.setText("0");
                                txtpontossemanais.setText("0");
                            }
                            progressBar.setVisibility(View.INVISIBLE);


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


    // Adiciona pontos
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
                       // final String[] data = {""};
                       // Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                       // v.vibrate(300);
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

    public void verificaMsg(String id_cliente){


        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");
         String cidade="";
        try {
             cidade = URLEncoder.encode(txtcidade, "UTF-8");

        }catch (Exception e){}
       //     String cidade = new String(txtcidade.getBytes(), "UTF-8")
        String strUrl2 = auxUrl + "verificaMsg.php?id_cliente="+id_cliente+"&cidade="+cidade;
        Log.d("urlpontos=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};

                        try {
                            JSONObject consultaArray = new JSONObject(response);
                            JSONArray jsonMensagem = consultaArray.getJSONArray("mensagem");

                            for (int i = 0; i < jsonMensagem.length(); i++) {
                                JSONObject jsonMsgItem =
                                        jsonMensagem.getJSONObject(i);
                                 msgnotifica =
                                        jsonMsgItem.getString("msg");
                                 datahoranotifica =
                                        jsonMsgItem.getString("datahora");
                                id_mensagem =
                                        jsonMsgItem.getString("id_mensagem");


                                if (!msgnotifica.equals("0")){
                                    mnotifica.setVisible(true);

                                }

                            }
                            Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(700);
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


    // Adiciona mensagem lida
    public void insertMsgLida(String id_cliente, String id_mensagem){


        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl2 = auxUrl + "insertMsgLida.php?id_cliente="+id_cliente+"&id_mensagem="+id_mensagem;
      //  Log.d("urlpontos=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        //Vibrator v = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                        //v.vibrate(300);
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
