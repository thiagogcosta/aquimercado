package com.aquimercado.aquimercado.pedidos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PedidoIndividualNavigation extends AppCompatActivity
        implements Response.Listener<JSONObject>, Response.ErrorListener {


    public static final List<PedidoItem> produto = new ArrayList<PedidoItem>();
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager lm;
    String auxUrlimagem, auxUrl;


    private String cod_pedido;
    private String valor_total;
    private String hora_prevista;
    private String hora_pedido;
    private String id_cartao;
    private String id_mercado;
    private String troco_para;
    private String isCheque;
    private String tipo_entregaRetirada;
    private String nome;
    private String imagem_mercado;
    private String stat;
    private String cancel_url;
    public String instrucaoEntrega;
    public String instrucaoRetirada;
    public String fonesac;
    public String pais;
    public String estado;
    public String cidade;
    public String rua;
    public String numero;
    public String msg;
    public String latitude;
    public String longitude;
    public String nome_mercado;
    private int statInst = 0;
    private String aquiLatitude = "";
    private String aquiLongitude = "";


    //TextView
    private TextView txt_nomemercado;
    private TextView txt_codigo;
    private TextView txt_valorTotal;
    private TextView txt_tipo_pagamento;
    private TextView txt_tipo;
    private TextView txt_data;
    private TextView txt_status;
    private TextView txt_hora;

    //Button
    private Button btCancelar;
    private Button btInfo;
    private Button btComoChegar;

    //NetworkView
    private ImageView icone_mercado;

    public static Intent intent = null;
    public static Bundle b = null;

    private LocationManager locationManager;

    public NumberFormat moeda;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_individual_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new MenuPrincipal(this.getApplicationContext(), this);

        //***************************************Pegando o ID **********************************
        intent = getIntent();

        b = intent.getExtras();

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        auxUrl = urlPref.getString("url_base", "");

        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        //URL do JSON
        String url = auxUrl + "/makejsonPedidoIndividual.php?id_pedido=" + b.get("id");

        cancel_url = auxUrl + "/cadCancelarPedido.php";


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

        //Setando as informações
        txt_nomemercado = (TextView) findViewById(R.id.txt_nomemercado);
        txt_codigo = (TextView) findViewById(R.id.txt_cod);
        txt_valorTotal = (TextView) findViewById(R.id.txt_valorTotal);
        txt_tipo_pagamento = (TextView) findViewById(R.id.txt_tipo_pagamento);
        txt_tipo = (TextView) findViewById(R.id.txt_tipo);
        txt_data = (TextView) findViewById(R.id.txt_data);
        txt_status = (TextView) findViewById(R.id.txt_status);
        icone_mercado = (ImageView) findViewById(R.id.icone_mercado);
        txt_hora = (TextView) findViewById(R.id.txt_hora);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btInfo = (Button) findViewById(R.id.btInfo);
        btComoChegar = (Button) findViewById(R.id.btComochegar);


        //*********************************RecyclerView**************************************
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_pedIndividual);
        mRecyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);


        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelarPedido();
            }
        });

        //Se o pedido for cancelado ou finalizado coloco ele invisível
        if (b.get("status").toString().equalsIgnoreCase("cancelado") || b.get("status").toString().equalsIgnoreCase("finalizado") || b.get("status").toString().equalsIgnoreCase("pronto")) {
            btCancelar.setVisibility(View.GONE);
        }

        if(b.get("tipo").toString().equalsIgnoreCase("2")) {
            btComoChegar.setVisibility(View.GONE);
        }

        SharedPreferences locationLat = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        String lat = locationLat.getString("latitude", "");
        String longit = locationLat.getString("longitude", "");

        aquiLatitude = lat;
        aquiLongitude = longit;


        btComoChegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!aquiLongitude.equalsIgnoreCase("") && !aquiLatitude.equalsIgnoreCase("")){
                    String uri = "http://maps.google.com/maps?saddr=" + aquiLatitude + "," + aquiLongitude + "&daddr=" + latitude + "," + longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
            }
        });

        btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statInst == 1) {
                    if (tipo_entregaRetirada.equalsIgnoreCase("1")) {
                        msg = instrucaoRetirada;
                    } else {
                        if (tipo_entregaRetirada.equalsIgnoreCase("2")) {
                            msg = instrucaoEntrega;
                        } else {
                            msg = " ";
                        }
                    }

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                    // Setting Dialog Title
                    alertDialog.setTitle("Sobre o mercado");

                    //DECLARAÇÃO DO TEXTVIEW
                    final TextView endereco = new TextView(v.getContext());
                    endereco.setText("Localização: " + rua + "," +numero+" - "+cidade+"/"+estado);
                    endereco.setPadding(0, 5, 0, 0);
                    endereco.setTextSize(14);

                    final TextView telefone = new TextView(v.getContext());
                    if (fonesac.isEmpty()) {fonesac= "Não Disponível";}
                    telefone.setText("Telefone SAC: " + fonesac);
                    telefone.setPadding(0, 5, 0, 0);
                    telefone.setTextSize(14);

                    final TextView instrucoes = new TextView(v.getContext());
                    instrucoes.setText("Instruções: " + msg);
                    instrucoes.setPadding(0, 5, 0, 0);
                    instrucoes.setTextSize(14);

                    LinearLayout ll = new LinearLayout(v.getContext());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(endereco);
                    ll.addView(telefone);
                    ll.addView(instrucoes);
                    ll.setPadding(50,5,50,5);
                    alertDialog.setView(ll);


                    // Setting Dialog Message
                    //alertDialog.setMessage(msg);

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.btinfo);

                    // Setting Positive "Yes" Button
                    alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            //Limpando o produto
            produto.clear();

            JSONObject jPedidos = response.getJSONObject("pedidos");
            JSONArray jPedido = jPedidos.getJSONArray("pedido");
            JSONArray jProduto = jPedidos.getJSONArray("produtos");

            for (int i = 0; i < jPedido.length(); i++) {
                JSONObject jsonPedidoItem =
                        jPedido.getJSONObject(i);
                String cod =
                        jsonPedidoItem.getString("cod_pedido");
                String vt =
                        jsonPedidoItem.getString("valor_total");
                String hp =
                        jsonPedidoItem.getString("hora_prevista");
                String h_pedido =
                        jsonPedidoItem.getString("hora_pedido");
                String cart =
                        jsonPedidoItem.getString("id_cartao");
                String cheque =
                        jsonPedidoItem.getString("cheque");
                String troco =
                        jsonPedidoItem.getString("troco_para");
                String id_m =
                        jsonPedidoItem.getString("id_mercado");
                String er =
                        jsonPedidoItem.getString("tipo_entregaRetirada");

                String latit =
                        jsonPedidoItem.getString("latitude");

                String longit =
                        jsonPedidoItem.getString("longitude");




                String n = new String(jsonPedidoItem.getString("nome").getBytes("ISO-8859-1"), "UTF-8");

                String img =
                        jsonPedidoItem.getString("imagem_mercado");
                img = auxUrlimagem + img;

                String status =
                        jsonPedidoItem.getString("status");

                cod_pedido = cod;
                valor_total = vt;
                hora_prevista = hp;
                latitude = latit;
                longitude = longit;
                hora_pedido = h_pedido;
                id_cartao = cart;
                troco_para = troco;
                isCheque = cheque;
                tipo_entregaRetirada = er;
                nome = n;
                imagem_mercado = img;
                stat = status;
                id_mercado = id_m;
            }


            for (int i = 0; i < jProduto.length(); i++) {
                JSONObject jsonProdutoItem =
                        jProduto.getJSONObject(i);
                String nome = new String(jsonProdutoItem.getString("nome_produto").getBytes("ISO-8859-1"), "UTF-8");

                String imagem =
                        jsonProdutoItem.getString("imagem_produto");
                imagem = auxUrlimagem + imagem;

                String valor =
                        jsonProdutoItem.getString("valor");

                String quantidade =
                        jsonProdutoItem.getString("quantidade");

                Log.d("pd_qtde",quantidade);

                PedidoItem pedItem = new PedidoItem(nome, imagem, valor, i,quantidade);

                produto.add(pedItem);
            }

            txt_nomemercado.setText(nome);
            moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            txt_valorTotal.setText(moeda.format(Double.parseDouble(valor_total)));

            txt_data.setText(hora_prevista);
            txt_hora.setText(hora_pedido);

            if (troco_para.isEmpty() && !id_cartao.isEmpty()) {
                txt_tipo_pagamento.setText(" (Cartão)");
            }

            if (!troco_para.isEmpty() && id_cartao.isEmpty()) {
                txt_tipo_pagamento.setText(" (Dinheiro)");
            }
            if (!isCheque.isEmpty() ){
                txt_tipo_pagamento.setText(" (Cheque)");
            }

            validaInstrucao();

            if (tipo_entregaRetirada.equalsIgnoreCase("1")) {
                txt_tipo.setText("Retirada: ");
            } else {
                if (tipo_entregaRetirada.equalsIgnoreCase("2")) {
                    txt_tipo.setText("Entrega: ");
                } else {
                    txt_tipo.setText("Entrega ou Retirada: ");
                }
            }


            txt_data.setText(hora_prevista);

            stat = stat.toUpperCase();
            txt_status.setText(stat);


            Picasso.with(this).load(imagem_mercado).into(icone_mercado);


            String cod = "";
            for (int i = 0; i < cod_pedido.length(); i++) {
                cod += cod_pedido.charAt(i);
                if (i % 2 != 0 && i != 0) {
                    cod += " ";
                }
            }
            cod = cod.toUpperCase();
            txt_codigo.setText(cod);

            mAdapter = new GridPedidoIndividual(produto);

            mRecyclerView.setAdapter(mAdapter);


        } catch (Exception e) {
        }
    }

    public void cancelarPedido() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PedidoIndividualNavigation.this);

        alertDialog.setTitle("Cancelar");

        alertDialog.setMessage("Você tem certeza que deseja cancelar o pedido?");

        alertDialog.setIcon(R.drawable.cartoffline);

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

            //Se o cara clicar em SIM permite cancelar o pedido (pronto/processamento)

            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, cancel_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(getApplicationContext(), "Você cancelou o pedido!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), PedidosListNavigation.class);
                                intent.putExtra("tipo_pedido", "ambos");
                                finish();
                                startActivity(intent);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(PedidoIndividualNavigation.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("cod_pedido", cod_pedido);
                        params.put("id_mercado", id_mercado);
                        return params;
                    }

                };


                int timeout = 20000; // 20 segundos
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);
            }
        });

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    // --- método que baixa as informações de instrução para entrega e retirada ---
    public void validaInstrucao() {
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String urL = auxUrl + "makeJsonInstrucao.php?id=" + id_mercado;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, urL,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoEntrega;
                                JSONArray arrayPedidos = consultaArray.getJSONArray("instrucoes");
                                infoEntrega = (JSONObject) arrayPedidos.get(0);


                                instrucaoEntrega = infoEntrega.getString("instrucoes_entrega");

                                instrucaoRetirada = infoEntrega.getString("instrucoes_retirada");

                                fonesac = infoEntrega.getString("fonesac");

                                pais = infoEntrega.getString("pais");

                                estado = infoEntrega.getString("estado");

                                cidade = infoEntrega.getString("cidade");

                                rua = infoEntrega.getString("rua");

                                numero = infoEntrega.getString("numero");


                            }
                        } catch (Exception e) {

                        }

                        //status instução
                        statInst = 1;

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PedidoIndividualNavigation Page") // TODO: Define a title for the content shown.
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
