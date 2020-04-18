package com.aquimercado.aquimercado.pedidos;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.ViewHolder;

public class PedidosNavigation extends AppCompatActivity
        implements  Response.ErrorListener, Response.Listener<JSONObject> {

    Adapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager lm;
    ViewHolder viewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new MenuPrincipal(this.getApplicationContext(), this);


        //Shared
        SharedPreferences produtosPref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxiliando = produtosPref.getString("logado", "");

        //URL do JSON
        String url = "http://projetos.compsi.univem.edu.br/aquimercado/makejsonPedido.php?id_user="+auxiliando;

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

        //Recycler pedido
        mRecyclerView  = (RecyclerView)findViewById(R.id.recycler_pedido);
        mRecyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

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
        List<Pedido> pedidos = new ArrayList<Pedido>();

        try {
            // Não precisamos converter o
            // InputStream em String \o/
            JSONObject jsonComboP = response.getJSONObject("produtos");
            JSONArray jsonCombo =
                    jsonComboP.getJSONArray("produto");

            for (int i = 0; i < jsonCombo.length(); i++) {
                JSONObject jsonComboItem =
                        jsonCombo.getJSONObject(i);
                String codigo =
                        jsonComboItem.getString("cod_pedido");
                String status =
                        jsonComboItem.getString("status_pedido");
                String imagem =
                        jsonComboItem.getString("imagem_mercado");
                String hora =
                        jsonComboItem.getString("hora_pedido");
                String valor =
                        jsonComboItem.getString("valor_total");
                String id =
                        jsonComboItem.getString("id_pedidofechado");
                String tipo_pedido =
                        jsonComboItem.getString("tipo_entregaRetirada");

                Pedido pedido = new Pedido(hora, imagem, valor, codigo, status,id, tipo_pedido);
                pedidos.add(pedido);

            }

            mAdapter = new GridPedido(pedidos);

            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
