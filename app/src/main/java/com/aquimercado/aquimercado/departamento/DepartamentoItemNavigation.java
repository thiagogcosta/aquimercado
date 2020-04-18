package com.aquimercado.aquimercado.departamento;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
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
import com.aquimercado.aquimercado.SQLite.DBHelper;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.fechamento.AFechamento;
import com.aquimercado.aquimercado.login.AlterarNavigation;
import com.aquimercado.aquimercado.login.LoginNavigation;
import com.aquimercado.aquimercado.oferta.OfertaItemNavigation;
import com.aquimercado.aquimercado.combo.ComboItemNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class DepartamentoItemNavigation extends AppCompatActivity
        implements  Response.Listener<JSONObject>, Response.ErrorListener {

    private Toolbar toolbar;
    RecyclerView mRecyclerViewBedidas,mRecyclerViewLimpeza,mRecyclerViewCarnes,
            mRecyclerViewBebes,mRecyclerViewAlimentos, mRecyclerViewPerfumaria, mRecyclerViewFeira,
            mRecyclerViewMassas, mRecyclerViewBiscoitos, mRecyclerViewPet, mRecyclerViewMolhos,
            mRecyclerViewNaturais;

    TextView txBebidas, txLimpeza,txCarnes,txBebes,txAlimentos,txPerfumaria,txFeira,txMassas,txBiscoitos,
             txPet,txMolhos,txNaturais;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    static List<DepartamentoItem> ListaPD = new ArrayList<DepartamentoItem>();
    int contador_prod=0, contador_prod_json=0, contador_notlistavazia=0, contador_prodlinha=0,  status1 = 0, status2 = 0, status3 = 0;
 public static String aux_clicado;
    public static TextView txtTotal,txtDesc,txtDescTotal = null;
    public View v;
    public static final String PREFS_NAME = "produtos";
    static DBHelper db ;
    public static float soma,acu,eq=0;

    public static Intent intent = null;
    public static Bundle b = null;
    public static String auxUrlimagem = null;
    int status;
    private ImageButton btInfo;

    private String tipo;

    public  MenuItem itemEntrega;
    public  MenuItem itemRetirada;
    public  MenuItem itemEntregaOff;
    public  MenuItem itemRetiradaOff;
    public  MenuItem itemComboView;
    public MenuItem itemDepartamentoView;

    public  Menu menu2;

    // Animation
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departamento_item_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);



        // ################### Criaçao de todos os departamentos ###########################


        // Bebidas
        LinearLayoutManager lbebidas = new LinearLayoutManager(getApplicationContext());
        lbebidas.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewBedidas = (RecyclerView) findViewById(R.id.recycler_view_bebidas);
        txBebidas = (TextView) findViewById(R.id.txbedidas);
        mRecyclerViewBedidas.setHasFixedSize(true);
        mRecyclerViewBedidas.setLayoutManager(lbebidas);

        // Limpeza
        LinearLayoutManager llimpeza = new LinearLayoutManager(getApplicationContext());
        llimpeza.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewLimpeza = (RecyclerView) findViewById(R.id.recycler_view_limpeza);
        txLimpeza = (TextView) findViewById(R.id.txlimpeza);
        mRecyclerViewLimpeza.setHasFixedSize(true);
        mRecyclerViewLimpeza.setLayoutManager(llimpeza);


        // Carnes
        LinearLayoutManager lcarnes = new LinearLayoutManager(getApplicationContext());
        lcarnes.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewCarnes = (RecyclerView) findViewById(R.id.recycler_view_carnes);
        txCarnes = (TextView) findViewById(R.id.txcarnes);
        mRecyclerViewCarnes.setHasFixedSize(true);
        mRecyclerViewCarnes.setLayoutManager(lcarnes);

        // Bebes
        LinearLayoutManager lbebes = new LinearLayoutManager(getApplicationContext());
        lbebes.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewBebes = (RecyclerView) findViewById(R.id.recycler_view_bebes);
        txBebes = (TextView) findViewById(R.id.txbebes);
        mRecyclerViewBebes.setHasFixedSize(true);
        mRecyclerViewBebes.setLayoutManager(lbebes);

        // Alimentos
        LinearLayoutManager lalimentos = new LinearLayoutManager(getApplicationContext());
        lalimentos.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewAlimentos = (RecyclerView) findViewById(R.id.recycler_view_alimentos);
        txAlimentos = (TextView) findViewById(R.id.txalimentos);
        mRecyclerViewAlimentos.setHasFixedSize(true);
        mRecyclerViewAlimentos.setLayoutManager(lalimentos);

       // Perfumaria
        LinearLayoutManager lperfumaria = new LinearLayoutManager(getApplicationContext());
        lperfumaria.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewPerfumaria = (RecyclerView) findViewById(R.id.recycler_view_perfumaria);
        txPerfumaria = (TextView) findViewById(R.id.txperfumaria);
        mRecyclerViewPerfumaria.setHasFixedSize(true);
        mRecyclerViewPerfumaria.setLayoutManager(lperfumaria);


        //Feira
        LinearLayoutManager lfeira = new LinearLayoutManager(getApplicationContext());
        lfeira.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewFeira = (RecyclerView) findViewById(R.id.recycler_view_feira);
        txFeira = (TextView) findViewById(R.id.txfeira);
        mRecyclerViewFeira.setHasFixedSize(true);
        mRecyclerViewFeira.setLayoutManager(lfeira);

        // Massas
        LinearLayoutManager lmassas = new LinearLayoutManager(getApplicationContext());
        lmassas.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewMassas = (RecyclerView) findViewById(R.id.recycler_view_massas);
        txMassas = (TextView) findViewById(R.id.txmassas);
        mRecyclerViewMassas.setHasFixedSize(true);
        mRecyclerViewMassas.setLayoutManager(lmassas);

        // Biscoitos
        LinearLayoutManager lbiscoitos = new LinearLayoutManager(getApplicationContext());
        lbiscoitos.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewBiscoitos = (RecyclerView) findViewById(R.id.recycler_view_biscoitos);
        txBiscoitos = (TextView) findViewById(R.id.txbiscoitos);
        mRecyclerViewBiscoitos.setHasFixedSize(true);
        mRecyclerViewBiscoitos.setLayoutManager(lbiscoitos);

        // Pet
        LinearLayoutManager lpet = new LinearLayoutManager(getApplicationContext());
        lpet.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewPet = (RecyclerView) findViewById(R.id.recycler_view_pet);
        txPet = (TextView) findViewById(R.id.txpet);
        mRecyclerViewPet.setHasFixedSize(true);
        mRecyclerViewPet.setLayoutManager(lpet);

        // Molhos
        LinearLayoutManager lmolhos = new LinearLayoutManager(getApplicationContext());
        lmolhos.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewMolhos = (RecyclerView) findViewById(R.id.recycler_view_molhos);
        txMolhos = (TextView) findViewById(R.id.txmolhos);
        mRecyclerViewMolhos.setHasFixedSize(true);
        mRecyclerViewMolhos.setLayoutManager(lmolhos);

        // Naturais
        LinearLayoutManager lnaturais = new LinearLayoutManager(getApplicationContext());
        lnaturais.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewNaturais = (RecyclerView) findViewById(R.id.recycler_view_naturais);
        txNaturais = (TextView) findViewById(R.id.txnaturais);
        mRecyclerViewNaturais.setHasFixedSize(true);
        mRecyclerViewNaturais.setLayoutManager(lnaturais);

        // ###########################################################################################

        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtDescTotal = (TextView) findViewById(R.id.txtDescTot);

        // ###########################################################################################

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prodBotao();

            }
        });

        //***************************************Pegando o ID **********************************
        intent = getIntent();

        b = intent.getExtras();


        //*************************Tipo Entrega ou Retirada**************************************
        tipo = ""+b.get("tipo");
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

        //Combo ou departamento
        SharedPreferences combo_deptoPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor combo_depto  = combo_deptoPref.edit();
        combo_depto.putString("combo_depto", "1");
        combo_depto.apply();

        //LOGO DO MERCADO
        SharedPreferences mercadoPref2 = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edMercado2 = mercadoPref2.edit();
        edMercado2.putString("logo_mercado", "" + b.get("logo_mercado"));
        edMercado2.apply();

        SharedPreferences id_comboPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edidCombo = id_comboPref.edit();
        edidCombo.putString("id_combo", ""+"X"); // "X" signfica que não será considerado as informacoes de combo para horarios e frete e sim departamentos
        edidCombo.apply();                       // Usado no AFechamento

        //ID DO MERCADO
        SharedPreferences idPref = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        SharedPreferences.Editor edPref = idPref.edit();
        edPref.putString("id_mercado", "" + b.get("id"));
        edPref.apply();

        //limpando o shared dos produtos
        SharedPreferences settings = getApplicationContext().getSharedPreferences("prodInfo", Context.MODE_PRIVATE);
        settings.edit().clear().apply();


        db = new DBHelper(getApplicationContext());
        db.onUpgrade(db.getReadableDatabase(),1,2);
        db.deleteAll();


        String url = auxUrl + "makejsonDepartamentos.php?id_mercado=" + b.get("id");

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
        configMercado(); // busca as configuracoes do departamento (tempo de preparo do pedido, tipo entrega/retirada, frete)

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

        String urlLL = auxUrl + "validaRetirada.php?lat=" + latitude + "&lng=" + longitude+"&id_mercado="+ b.get("id");



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
                                    Toast.makeText(getApplicationContext(), "Você está fora da área de atendimento do estabelecimento "+b.get("nome_mercado"), Toast.LENGTH_SHORT).show();
                                    status = 0;
                                }
                                if (infodistancia.getString("status").equals("1")) {
                                    //   Toast.makeText(getApplicationContext(), "Você está dentro da área de atuacao do mercado", Toast.LENGTH_LONG).show();
                                    status =1;
                                }

                                // -------------------   gravando status-------------------------
                                // status da regiao de atendimento
                                SharedPreferences regiao = getApplicationContext().getSharedPreferences("regiao", Context.MODE_PRIVATE);
                                SharedPreferences.Editor distancia = regiao.edit();
                                distancia.putString("status", ""+status);
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

        if (menu2 != null) {
            for (int i = 0; i < menu2.size(); i++) {
                Log.d("menu=","oncreate");
                Drawable drawable = menu2.getItem(i).getIcon();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        }

        // ***********************  verifica permissoes de Vendas
        SharedPreferences permissoes = getApplication().getSharedPreferences("Permite", Context.MODE_PRIVATE);
        final String permitedep = permissoes.getString("permitedep", "");
        final String permiteoferta = permissoes.getString("permiteoferta", "");
        final String permitecombo = permissoes.getString("permitecombo", "");
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
                    Toast.makeText(getApplicationContext(), "Este estabelecimento ainda não vende combos online!", Toast.LENGTH_SHORT).show();

                }

            }

        });


        // ############# Botao oferta
        ImageButton boferta = (ImageButton) findViewById(R.id.boferta);

        boferta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (permiteoferta.equals("1")) {
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
                    Toast.makeText(getApplicationContext(), "Este estabelecimento não tem ofertas anunciadas!", Toast.LENGTH_SHORT).show();

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
        List<DepartamentoItem> departamentolist = new ArrayList<DepartamentoItem>();
        final String[] data = {""};
        try {
            if (response.toString().length() > 0 && response.toString() != " ") {
                data[0] = response.toString();
                JSONObject consultaArray = new JSONObject(data[0]);

                JSONObject infoDepartamento;
                JSONArray arrayDepartamento;
                for (int j=1;j<=12;j++) {

                    if (consultaArray.getJSONArray(""+j)!=null && consultaArray.getJSONArray(""+j).length()>0) {


                        arrayDepartamento = consultaArray.getJSONArray(""+j);


                        for (int i = 0; i < arrayDepartamento.length(); i++) {
                            infoDepartamento = (JSONObject) arrayDepartamento.get(i);

                            String id_produto = infoDepartamento.getString("id_produto");

                            String nome_produto = infoDepartamento.getString("nome_produto");

                            String departamento = infoDepartamento.getString("departamento");

                            String preco_produto = infoDepartamento.getString("preco_produto");

                            String imagem = infoDepartamento.getString("imagem_produto");
                            String imagemDepartamento = auxUrlimagem + imagem;

                            String desconto = infoDepartamento.getString("vdesconto");

                            String apdesconto = infoDepartamento.getString("add_desconto");

                            String marca = infoDepartamento.getString("marca_produto");

                            String descricao = infoDepartamento.getString("descricaofull");

                            String quantidade = "1";

                            DepartamentoItem deptoItem = new DepartamentoItem(id_produto, b.get("id").toString(), nome_produto, departamento, preco_produto,
                                    imagemDepartamento, desconto, apdesconto,marca,descricao,quantidade.toString());
                            departamentolist.add(deptoItem);

                            db.addProdDepto(deptoItem);

                        }


                        Log.d("dep_list",":"+departamentolist.size());
                        if (j==1) { // Bebidas
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnbebidas);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewBedidas.setVisibility(View.VISIBLE);
                            txBebidas.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewBedidas.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowbebida);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==2) { // Limpeza
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnlimpeza);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewLimpeza.setVisibility(View.VISIBLE);
                            txLimpeza.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewLimpeza.setAdapter(mAdapter);

                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowlimpeza);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==3) { //carne
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lncarnes);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewCarnes.setVisibility(View.VISIBLE);
                            txCarnes.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewCarnes.setAdapter(mAdapter);



                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowcarne);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }


                        }

                        if (j==4) { // bebes
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnbebes);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewBebes.setVisibility(View.VISIBLE);
                            txBebes.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewBebes.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowbebes);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==5) { // alimentos
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnalimentos);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewAlimentos.setVisibility(View.VISIBLE);
                            txAlimentos.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewAlimentos.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowalimentos);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==6) { // perfumaria
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnperfumaria);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewPerfumaria.setVisibility(View.VISIBLE);
                            txPerfumaria.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewPerfumaria.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowperfumaria);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==7) { // Feira
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnfeira);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewFeira.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            txFeira.setVisibility(View.VISIBLE);
                            mRecyclerViewFeira.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowfeira);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==8) { // Massas
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnmassas);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewMassas.setVisibility(View.VISIBLE);
                            txMassas.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewMassas.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowmassas);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==8) { // Biscoitos
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnbiscoitos);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewBiscoitos.setVisibility(View.VISIBLE);
                            txBiscoitos.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewBiscoitos.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowbiscoitos);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==10) { // Pet
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnpet);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewPet.setVisibility(View.VISIBLE);
                            txPet.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewPet.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowpet);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==11) { // Molhos
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnmolhos);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewMolhos.setVisibility(View.VISIBLE);
                            txMolhos.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewMolhos.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrowmolhos);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }
                        if (j==12) { // Naturais
                            LinearLayout ln = (LinearLayout) findViewById(R.id.lnnaturais);
                            ln.setVisibility(View.VISIBLE);
                            mRecyclerViewNaturais.setVisibility(View.VISIBLE);
                            txNaturais.setVisibility(View.VISIBLE);
                            mAdapter = new GridAdapterDepartamento(departamentolist);
                            mRecyclerViewNaturais.setAdapter(mAdapter);


                            if (departamentolist.size()>2) {
                                ImageView iv = (ImageView) findViewById(R.id.arrownaturais);
                                iv.setVisibility(View.VISIBLE);

                                final Animation animation = new AlphaAnimation((float) 0.8, 0); // Change alpha from fully visible to invisible
                                animation.setDuration(1000); // duration - half a second
                                animation.setInterpolator(new LinearInterpolator()); // do not alter
                                animation.setRepeatCount(Animation.INFINITE); // Repeat animation
                                animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the
                                iv.startAnimation(animation);
                            }

                        }

                        departamentolist.clear();

                    }else{
                         // caso nao tenha o departamento
                        if (j==1){mRecyclerViewBedidas.setVisibility(View.GONE);txBebidas.setVisibility(View.GONE);}
                        if (j==2){mRecyclerViewLimpeza.setVisibility(View.GONE);txLimpeza.setVisibility(View.GONE);}
                        if (j==3){mRecyclerViewCarnes.setVisibility(View.GONE);txCarnes.setVisibility(View.GONE);}
                        if (j==4){mRecyclerViewBebes.setVisibility(View.GONE);txBebidas.setVisibility(View.GONE);}
                        if (j==5){mRecyclerViewAlimentos.setVisibility(View.GONE);txAlimentos.setVisibility(View.GONE);}
                        if (j==6){mRecyclerViewPerfumaria.setVisibility(View.GONE);txPerfumaria.setVisibility(View.GONE);}
                        if (j==7){mRecyclerViewFeira.setVisibility(View.GONE);txFeira.setVisibility(View.GONE);}
                        if (j==8){mRecyclerViewMassas.setVisibility(View.GONE);txMassas.setVisibility(View.GONE);}
                        if (j==9){mRecyclerViewBiscoitos.setVisibility(View.GONE);txBiscoitos.setVisibility(View.GONE);}
                        if (j==10){mRecyclerViewPet.setVisibility(View.GONE);txPet.setVisibility(View.GONE);}
                        if (j==11){mRecyclerViewMolhos.setVisibility(View.GONE);txMolhos.setVisibility(View.GONE);}
                        if (j==12){mRecyclerViewNaturais.setVisibility(View.GONE);txNaturais.setVisibility(View.GONE);}

                    }
                }
            }
        } catch (Exception e) {
        }

    }


    public void prodBotao(){



        //Verifico se o produto foi clicado
        SharedPreferences produtosClicados = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String auxProdClicado = produtosClicados.getString("prodClicado", "");

        //VERIFICAÇÃO DE LISTA VAZIA
        contador_notlistavazia = 0;

        String prodclicado[] = auxProdClicado.split(Pattern.quote("/"));

        contador_prod_json = prodclicado.length;


        SharedPreferences produtosPref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxiliando = produtosPref.getString("logado", "");
        SharedPreferences produtosPref2 = getApplication().getSharedPreferences("SessaoUserFULL", Context.MODE_PRIVATE);
        String auxiliando2 = produtosPref2.getString("preenchido", "");
        if(auxiliando.length() > 0) {
            if(!auxiliando2.equalsIgnoreCase("0")){
                if(contador_prod_json > 0){
                    Intent intent = new Intent(getApplicationContext(), AFechamento.class);
                    finish();
                    startActivity(intent);
                }else {

                    MyToast("Por favor, selecione pelo menos um produto!");

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

    public static  void setQtdeProduto(String idProduto, String qtde){
        for (DepartamentoItem pd : db.getAllProdDepto()) {
            if (pd.getidProduto().equalsIgnoreCase(idProduto)){
                pd.setQuantidade(qtde);
                db.updateProdQtde(pd);
                Log.d("totalq=",""+(Integer.parseInt(pd.getQuantidade())*Float.parseFloat(pd.getPrecoProduto())));
                addValores(aux_clicado);
            }
        }
        db.close();
    }

    public static void addValores(String clicado){

        Log.d("clicado=",""+clicado);
        aux_clicado = clicado;
        String produtosClicados[] =clicado.split(Pattern.quote("/"));

        soma = 0;
        acu = 0;
        eq = 0;

        ListaPD.clear();



        for (DepartamentoItem pd : db.getAllProdDepto()) {
            for (String c : produtosClicados) {
                if (pd.getidProduto().equalsIgnoreCase(c)) {
                     Log.d("preco=",pd.getPrecoProduto().toString());
                    ListaPD.add(pd);
                    break;
                }
            }
        }
        db.close();

        for (DepartamentoItem pd : ListaPD) {
            soma += Float.parseFloat(pd.getPrecoProduto()) * Integer.parseInt(pd.getQuantidade());
           // soma += Float.parseFloat(pd.getPrecoProduto());
            Log.d("quantidade=",pd.getQuantidade()+"");
            if(pd.getApDesconto().equalsIgnoreCase("1")){
                acu += (Float.parseFloat(pd.getPrecoProduto()) - Float.parseFloat(pd.getDesconto()))*Integer.parseInt(pd.getQuantidade());
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
        Log.d("soma=",soma+"");

    }

    public static void addsubValores(String clicado){


        String produtosClicados[] =clicado.split(Pattern.quote("/"));

        soma = 0;
        acu = 0;
        eq = 0;

        ListaPD.clear();

        for (DepartamentoItem pd : db.getAllProdDepto()) {
            for (String c : produtosClicados) {
                if (pd.getidProduto().equalsIgnoreCase(c)) {


                    ListaPD.add(pd);
                    break;
                }
            }
        }
        db.close();


        for (DepartamentoItem pd : ListaPD) {
            soma += Float.parseFloat(pd.getPrecoProduto()) * Integer.parseInt(pd.getQuantidade());
            if(pd.getApDesconto().equalsIgnoreCase("1")){
                acu += (Float.parseFloat(pd.getPrecoProduto()) - Float.parseFloat(pd.getDesconto()))*Integer.parseInt(pd.getQuantidade());
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

    public void configMercado(){

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "configMercadoDepto.php?id_mercado="+ b.get("id");;


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

                                if (!consultaArray.isNull("config")) {

                                    JSONObject infoConfig;

                                    JSONArray arrayPedidos = consultaArray.getJSONArray("config");

                                    infoConfig = (JSONObject) arrayPedidos.get(0);

                                    String depto_entrega_retirada = infoConfig.getString("depto_entrega_retirada");
                                    String depto_tempo_entrega = infoConfig.getString("depto_tempo_entrega");
                                    String depto_tempo_retirada = infoConfig.getString("depto_tempo_retirada");
                                    String depto_frete = infoConfig.getString("depto_frete");
                                    String depto_isentoFrete = infoConfig.getString("depto_isentoFrete");

                                    // Armazena no SHARED
                                    // ############################################ Dados de frete / valor isento ###########################

                                    //****************************GUARDO O COD_HORAPREVISTA**************************************
                                    SharedPreferences cod_hp = getApplication().getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor_cod_hp = cod_hp.edit();
                                    editor_cod_hp.putString("hora_prevista", depto_tempo_retirada);
                                    editor_cod_hp.apply();

                                    //****************************GUARDO O ENTREGARETIRADA**************************************
                                    SharedPreferences cod_er = getApplication().getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor_cod_er = cod_er.edit();
                                    editor_cod_er.putString("entregaretirada", depto_entrega_retirada);
                                    editor_cod_er.apply();

                                    //****************************GUARDO O TEM_FRETE**************************************
                                    SharedPreferences cod_temFrete = getApplication().getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor_cod_temFrete = cod_temFrete.edit();
                                    editor_cod_temFrete.putString("tem_frete", depto_frete);
                                    editor_cod_temFrete.apply();

                                    //*******************************************************************************************

                                    //****************************GUARDO O VALOR_ISENTO**************************************
                                    SharedPreferences cod_valor_isento = getApplication().getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor_valor_isento = cod_valor_isento.edit();
                                    editor_valor_isento.putString("valor_isento", depto_isentoFrete); // esta fixo
                                    editor_valor_isento.apply();

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

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences produtosPref = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
       // getMenuInflater().inflate(R.menu.departamento_item_navigation, menu);

        /*LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        final Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        itemDepartamentoView = menu.findItem(R.id.action_combo_depto);
        itemDepartamentoView.setActionView(iv);
       */
        //this.menu2 = menu;
        //itemComboView = menu.findItem(R.id.action_depto_combo);
        //itemComboView.setVisible(true);

        return true;
    }


}