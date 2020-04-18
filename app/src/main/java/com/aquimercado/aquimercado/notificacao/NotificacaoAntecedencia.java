package com.aquimercado.aquimercado.notificacao;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SplashscreenFIRST;

import org.json.JSONObject;



/**
 * Created by rafael on 10/05/16.
 */
public class NotificacaoAntecedencia extends Activity {
    public TextView txtcod_pedido,txthora_pedido,txtnome_mercado;
    public String cod_pedido,hora_pedido,nome_mercado;
    JSONObject jsonData;
    ListView mListView;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificacao);


        txtcod_pedido = (TextView) findViewById(R.id.cod_pedido);
        txthora_pedido = (TextView) findViewById(R.id.hora_pedido);
        txtnome_mercado = (TextView) findViewById(R.id.nome_mercado);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.cancel(1);

        Intent i = getIntent();
        
        cod_pedido = i.getStringExtra("cod_pedido");
        hora_pedido = i.getStringExtra("hora_pedido");
        nome_mercado = i.getStringExtra("nome_mercado");

        txtcod_pedido.setText(cod_pedido);
        txtnome_mercado.setText(nome_mercado);

        final Button button = (Button) findViewById(R.id.abrirAQM);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                startActivity(intent);
            }
        });


    }
}
