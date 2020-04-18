package com.aquimercado.aquimercado.pedidos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;

public class FiltrarNavigation extends AppCompatActivity {
        public static String auxURL = null;
        RadioGroup radio_pedido;
        RadioButton pedido_processamento,pedido_finalizado,pedido_cancelado;
        Button btFiltrarEsp;
        int aux_radio = 0;
        Intent i;
        Bundle b;
        String pedido_esta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtrar_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        new MenuPrincipal(this.getApplicationContext(), this);


        radio_pedido = (RadioGroup) findViewById(R.id.radio_Pedido);
        pedido_processamento = (RadioButton) findViewById(R.id.pedido_processamento);
        pedido_finalizado = (RadioButton) findViewById(R.id.pedido_finalizado);
        pedido_cancelado = (RadioButton) findViewById(R.id.pedido_cancelado);
        btFiltrarEsp = (Button) findViewById(R.id.btFiltrarEsp);

        pedido_processamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxURL = "ambos";
            }
        });

        pedido_finalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxURL = "finalizado";
            }
        });

        pedido_cancelado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxURL = "cancelado";
            }
        });

        //verificação se já clicou em algum radio
        radio_pedido.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                aux_radio = 1;
            }
        });

        btFiltrarEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(aux_radio == 1){
                   Intent intent = new Intent(getApplicationContext(), PedidosListNavigation.class);
                   intent.putExtra("tipo_pedido", auxURL);
                   finish();
                   startActivity(intent);
               }else {
                   Toast.makeText(getApplicationContext(),"Selecione o tipo pedido!",Toast.LENGTH_SHORT).show();
               }
            }
        });

        i = getIntent();

        b = i.getExtras();

        pedido_esta = ""+b.get("tipo_pedido");

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), PedidosListNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tipo_pedido", pedido_esta);
        finish();
        startActivity(intent);
    }
}
