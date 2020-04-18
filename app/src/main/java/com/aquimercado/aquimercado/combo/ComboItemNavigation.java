package com.aquimercado.aquimercado.combo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.aquimercado.aquimercado.departamento.DepartamentoItemNavigation;
import com.aquimercado.aquimercado.oferta.OfertaItemNavigation;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComboItemNavigation extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {

    private Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    public static final String PREFS_NAME = "Produtos";
    public static Intent intent = null;
    public static Bundle b = null;
    public static String auxUrlimagem = null;
    int status;
    private ImageButton btInfo;

    private String tipo;

    public MenuItem itemEntrega;
    public MenuItem itemRetirada;
    public MenuItem itemEntregaOff;
    public MenuItem itemRetiradaOff;
    public MenuItem itemDepartamentoView;

    private ImageView iv;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_item_navigation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);


        //Calling the RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        //***************************************Pegando o ID **********************************
        intent = getIntent();

        b = intent.getExtras();

        //configMercado();
        //*************************Tipo Entrega ou Retirada**************************************
        tipo = "" + b.get("tipo");
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


        //*****************************************Zerar o prodClicado********************************

        SharedPreferences produtosClicados = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorPC = produtosClicados.edit();

        editorPC.clear();
        editorPC.apply();


        String url = auxUrl + "makeJsonCombo.php?id=" + b.get("id") + "&tipo=" + b.get("tipo");


        //NOME DO MERCADO
        SharedPreferences mercadoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edMercado = mercadoPref.edit();
        edMercado.putString("nome_mercado", "" + b.get("nome_mercado"));
        edMercado.apply();

        //Combo ou departamento
        SharedPreferences combo_deptoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor combo_depto = combo_deptoPref.edit();
        combo_depto.putString("combo_depto", "2");
        combo_depto.apply();

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

        String urlLL = auxUrl + "validaRetirada.php?lat=" + latitude + "&lng=" + longitude + "&id_mercado=" + b.get("id");


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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        // ***********************  verifica permissoes de Vendas
        SharedPreferences permissoes = getApplication().getSharedPreferences("Permite", Context.MODE_PRIVATE);
        final String permitedep = permissoes.getString("permitedep", "");
        final String permiteoferta = permissoes.getString("permiteoferta", "");
        final String permitecombo = permissoes.getString("permitecombo", "");

        // ############# Botao Departamento
        ImageButton bdepto = (ImageButton) findViewById(R.id.bdepto);

        bdepto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (permitedep.equals("1")){
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
                else{
                    Toast.makeText(getApplicationContext(), "Este estabelecimento ainda não vende produtos por departamento no aplicativo aquiMercado.com!!", Toast.LENGTH_SHORT).show();

                }

            }

        });

        // ############# Botao oferta
        ImageButton boferta = (ImageButton) findViewById(R.id.boferta);

        boferta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (permiteoferta.equals("1")){
                    Toast.makeText(getApplicationContext(), "Ofertas Especiais", Toast.LENGTH_SHORT).show();
                    Intent intentDepto = new Intent(getApplicationContext(), OfertaItemNavigation.class);
                    intentDepto.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentDepto.putExtra("id", "" + b.get("id"));
                    intentDepto.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intentDepto.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intentDepto.putExtra("tipo", "ambos");
                    intentDepto.putExtra("permitedepto", "1");
                    startActivity(intentDepto);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Este estabelecimento não tem ofertas cadastradas!", Toast.LENGTH_SHORT).show();

                }

            }

        });

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


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        List<ComboItem> combos = new ArrayList<ComboItem>();
        final String[] data = {""};
        try {
            if (response.toString().length() > 0 && response.toString() != " ") {
                data[0] = response.toString();
                JSONObject consultaArray = new JSONObject(data[0]);


                JSONObject infoEntrega;
                JSONArray arrayPedidos = consultaArray.getJSONArray("combod");

                for (int i = 0; i < arrayPedidos.length(); i++) {
                    infoEntrega = (JSONObject) arrayPedidos.get(i);

                    String id_mercado = infoEntrega.getString("id_mercado");

                    String id_combo = infoEntrega.getString("id_combo");

                    String nome = infoEntrega.getString("nome");

                    String nomeCompleto = infoEntrega.getString("nomeCompleto");

                    String validade = infoEntrega.getString("validade");

                    String imagem = infoEntrega.getString("url_foto");
                    String imagemComp = auxUrlimagem + imagem;

                    String tem_frete = infoEntrega.getString("tem_frete");

                    String entregaretirada = infoEntrega.getString("entregaretirada");

                    String cod_horaprevista = infoEntrega.getString("descricao");

                    String valor_isento = infoEntrega.getString("valor_isento");

                    String config = infoEntrega.getString("config");

                    String sobreapp = infoEntrega.getString("sobreapp");
                    if (sobreapp.equalsIgnoreCase("null") || sobreapp.isEmpty()) {
                        sobreapp = "64359b7192746a14740ad4bb7afe4e097327d0790190fd16";
                    }

                    ComboItem comboItem = new ComboItem(nome, imagemComp, validade, id_combo, nomeCompleto, id_mercado, entregaretirada, cod_horaprevista, tem_frete, valor_isento, config, sobreapp);
                    combos.add(comboItem);
                }

                Application app = getApplication();
                mAdapter = new GridAdapter(combos);

                mRecyclerView.setAdapter(mAdapter);

            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences produtosPref = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.combo_item_navigation, menu);


        //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);

        /*iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
        */
        //final Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        //rotation.setRepeatCount(Animation.INFINITE);
        //iv.startAnimation(rotation);
        //itemDepartamentoView = menu.findItem(R.id.action_combo_depto2);
        //itemDepartamentoView.setActionView(iv);

        if ( b.get("permitedepto").equals("1")){
        //itemDepartamentoView.setVisible(true);
        }
        else{
        //    itemDepartamentoView.setVisible(false);

        }
        //configMercado();




        itemEntrega = menu.findItem(R.id.action_entrega);
        itemRetirada = menu.findItem(R.id.action_retirada);
        itemEntregaOff = menu.findItem(R.id.action_entregaOff);
        itemRetiradaOff = menu.findItem(R.id.action_retiradaOff);
        //itemDepartamentoView = menu.findItem(R.id.action_combo_depto);


        //itemDepartamentoView.setVisible(true);

        switch (tipo) {
            case "ambos":
                itemRetirada.setVisible(true);
                itemEntrega.setVisible(true);
                break;
            case "entrega":
                itemRetiradaOff.setVisible(true);
                itemEntrega.setVisible(true);
                break;
            case "retirada":
                itemRetirada.setVisible(true);
                itemEntregaOff.setVisible(true);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Problema ao criar o menu", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_retirada:
                if (!itemEntregaOff.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Exibição de todos os combos com possibilidade de entrega!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ComboItemNavigation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("id", "" + b.get("id"));
                    intent.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intent.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intent.putExtra("tipo", "entrega");
                    startActivity(intent);
                    finish();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível concluir essa ação", Toast.LENGTH_SHORT).show();
                    return false;
                }
            case R.id.action_entrega:
                if (!itemRetiradaOff.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Exibição de todos os combos com possibilidade de retirada!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), ComboItemNavigation.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("id", "" + b.get("id"));
                    intent2.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intent2.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intent2.putExtra("tipo", "retirada");
                    startActivity(intent2);
                    finish();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível concluir essa ação", Toast.LENGTH_SHORT).show();
                    return false;
                }

            case R.id.action_entregaOff:
                if (!itemRetiradaOff.isVisible() && itemRetirada.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Exibição de todos os combos!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), ComboItemNavigation.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("id", "" + b.get("id"));
                    intent2.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intent2.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intent2.putExtra("tipo", "ambos");
                    startActivity(intent2);
                    finish();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível concluir essa ação", Toast.LENGTH_SHORT).show();
                    return false;
                }

            case R.id.action_retiradaOff:
                if (!itemEntregaOff.isVisible() && itemEntrega.isVisible()) {
                    Toast.makeText(getApplicationContext(), "Exibição de todos os combos!", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), ComboItemNavigation.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.putExtra("id", "" + b.get("id"));
                    intent2.putExtra("nome_mercado", "" + b.get("nome_mercado"));
                    intent2.putExtra("logo_mercado", "" + b.get("logo_mercado"));
                    intent2.putExtra("tipo", "ambos");
                    startActivity(intent2);
                    finish();
                    return true;
                } else {
                    Toast.makeText(getApplicationContext(), "Não foi possível concluir essa ação", Toast.LENGTH_SHORT).show();
                    return false;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ComboItemNavigation Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}