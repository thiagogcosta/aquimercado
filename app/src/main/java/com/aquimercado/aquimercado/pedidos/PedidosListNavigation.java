package com.aquimercado.aquimercado.pedidos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SplashscreenFIRST;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PedidosListNavigation extends AppCompatActivity
        implements Response.ErrorListener, Response.Listener<JSONObject>  {
    private TextView txtView;
    RecyclerView.Adapter mAdapter;
    RecyclerView mRecyclerView;
    LinearLayoutManager lm;
    Intent intent;
    Bundle b;
    public static String auxUrlimagem = null;
    Button btFiltrar;
    String pedido_esta = "";
    private int contPedidoPronto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_list_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        new MenuPrincipal(this.getApplicationContext(), this);


        //Shared

        SharedPreferences idClientePref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxIdCliente = idClientePref.getString("id_cliente", "");

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        //URL IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        //Pegando dados intent
        intent = getIntent();

        b = intent.getExtras();

        //URL do JSON


        String url = auxUrl+"makejsonPedido.php?id_user="+auxIdCliente+"&&tipo_pedido="+b.get("tipo_pedido");


        txtView = (TextView) findViewById(R.id.tipo_pedido);
        if(b.get("tipo_pedido").equals("ambos")){
            txtView.setText("Processamento/Pronto");
        }else{
            txtView.setText(""+b.get("tipo_pedido"));
        }

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
        mRecyclerView  = (RecyclerView)findViewById(R.id.recycler_pedido_list);
        mRecyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(lm);

        btFiltrar = (Button) findViewById(R.id.btFiltrar);

        pedido_esta = ""+b.get("tipo_pedido");



        btFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FiltrarNavigation.class);
                intent.putExtra("tipo_pedido", pedido_esta);
                finish();
                startActivity(intent);
            }
        });

    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }

    //Pegar do JSON
    @Override
    public void onResponse(JSONObject response) {
        List<Pedido> pedidos = new ArrayList<Pedido>();
        try {
            JSONObject jsonComboP = response.getJSONObject("produtos");
            JSONArray jsonCombo =
                    jsonComboP.getJSONArray("produto");

            for (int i = 0; i < jsonCombo.length(); i++) {
                JSONObject jsonComboItem =
                        jsonCombo.getJSONObject(i);
                String codigo =
                        jsonComboItem.getString("cod_pedido");


                String status = new String(jsonComboItem.getString("status_pedido").getBytes("ISO-8859-1"),"UTF-8");

                String imagem =
                        jsonComboItem.getString("imagem_mercado");

                imagem = auxUrlimagem + imagem;

                String hora =
                        jsonComboItem.getString("hora_pedido");
                String valor =
                        jsonComboItem.getString("valor_total");
                String id =
                        jsonComboItem.getString("id_pedidofechado");

                String tipo_pedido = jsonComboItem.getString("tipo_entregaRetirada");

                //Verificação se há produtos prontos e emissão de avisão em somente um...
                if(status.equalsIgnoreCase("pronto")){
                    if(contPedidoPronto == 0){
                        alert();
                    }
                    contPedidoPronto++;
                }

                Pedido pedido = new Pedido(hora, imagem, valor, codigo, status,id,tipo_pedido);
                pedidos.add(pedido);

            }

            mAdapter = new GridPedido(pedidos);

            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    //Mensagem falando que tem produtos prontos
    public void alert(){

        Toast.makeText(getApplicationContext(), "Você possui pedidos prontos!", Toast.LENGTH_LONG).show();

    }

}
