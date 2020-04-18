package com.aquimercado.aquimercado.notificacao;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SplashscreenFIRST;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONObject;

/**
 * Created by FabioNote on 09/01/2017.
 */

public class NotificacaoCancelamento extends Activity {
    public TextView txtcod_pedido,txthora_pedido,txtnome_mercado;
    public String cod_pedido,hora_pedido,nome_mercado;
    JSONObject jsonData;
    ListView mListView;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacaocancelamento);

        txtcod_pedido = (TextView) findViewById(R.id.cod_pedidoc);
        txthora_pedido = (TextView) findViewById(R.id.hora_pedidoc);
        txtnome_mercado = (TextView) findViewById(R.id.nome_mercadoc);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(1);

        Intent i = getIntent();

        cod_pedido = i.getStringExtra("cod_pedido");
        hora_pedido = i.getStringExtra("hora_pedido");
        nome_mercado = i.getStringExtra("nome_mercado");

        txtcod_pedido.setText(cod_pedido);
        //txthora_pedido.setText(hora_pedido);
        txtnome_mercado.setText(nome_mercado);



        final Button buttonconfirm = (Button) findViewById(R.id.confirmapedido);
        buttonconfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                gravarResposta(1);
                Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                startActivity(intent);
            }
        });

        final Button buttoncancel = (Button) findViewById(R.id.naoconfirmapedido);
        buttoncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gravarResposta(2);
                Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                startActivity(intent);
            }
        });





    }

    public void gravarResposta(int status){
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");
        SharedPreferences id_cliente = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String id = id_cliente.getString("id_cliente", "");

        String strUrl = auxUrl+"confirmaCancelamento.php?cod_pedido="+cod_pedido+"&id_cliente="+id+"&status_cancel="+status;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            final String[] data = {""};

                            data[0] = response.toString();

                        }catch(Exception e){

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
}

