package com.aquimercado.aquimercado.sobre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SplashscreenFIRST;
import com.aquimercado.aquimercado.VolleyProvider;

import java.util.HashMap;
import java.util.Map;

public class ContatoNavigation extends AppCompatActivity
        {

    //public static final String cad_url = "http://projetos.compsi.univem.edu.br/aquimercado/cadMensagem.php";

    public static final String nome = "nome";
    public static final String email = "email";
    public static final String mensagem = "mensagem";

    private EditText editNome;
    private EditText editEmail;
    private EditText editMensagem;
    private  Button btMensagem;
    private Contato contato;

    private Button btEnviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences emailPref = getApplication().getSharedPreferences("SessaoEMAIL", Context.MODE_PRIVATE);
        String auxEmail = emailPref.getString("email", "");

        SharedPreferences userPref = getApplication().getSharedPreferences("SessaoUserNOME", Context.MODE_PRIVATE);
        String auxNome = userPref.getString("nome", "");

        editEmail = (EditText) findViewById(R.id.editEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editMensagem = (EditText) findViewById(R.id.editMensagem);
        btMensagem = (Button) findViewById(R.id.btMensagem);

        editNome.setText(auxNome);
        editEmail.setText(auxEmail);

        new MenuPrincipal(this.getApplicationContext(), this);

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMSG();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }



    private void registerMSG(){
        final String nomeFinal = editNome.getText().toString().trim();
        final String emailFinal = editEmail.getText().toString().trim();
        final String msgFinal = editMensagem.getText().toString().trim();

        contato = new Contato(nomeFinal,emailFinal,msgFinal);

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String cad_url = auxUrl+"cadMensagem.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(ContatoNavigation.this, response, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                        finish();
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ContatoNavigation.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(nome,contato.getNome());
                params.put(email,contato.getEmail());
                params.put(mensagem,contato.getMensagem());
                return params;
            }

        };


        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }
}
