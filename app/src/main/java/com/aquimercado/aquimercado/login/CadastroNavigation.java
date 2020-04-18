package com.aquimercado.aquimercado.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CadastroNavigation extends AppCompatActivity
        implements View.OnClickListener{

    public static final String senha = "senha";
    public static final String email = "email";
    public static final String nome = "nome";

    private EditText editEmail;
    private EditText editSenha;
    private EditText editNome;
    private Cliente usuario;
    Boolean aux_visible_pass;


    private Button btRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //***********************SETANDO O NOME NO NAV ***************************************

        new MenuPrincipal(this.getApplicationContext(), this);

        //************************************************************************************

        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editNome = (EditText) findViewById(R.id.editNome);

        //Limpando focuS
        editEmail.clearFocus();
        editSenha.clearFocus();
        editNome.clearFocus();

        btRegistrar = (Button) findViewById(R.id.btRegistrar);

        btRegistrar.setOnClickListener(this);

        aux_visible_pass = false;

        editSenha.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (editSenha.getRight() - editSenha.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                /* REMOVE A MASCARA */
                        if(!aux_visible_pass) {
                            aux_visible_pass = true;
                            editSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.visivel, 0);
                            editSenha.setTransformationMethod(null);
                        }else{
                    /* VOLTA A MASCARA */
                            aux_visible_pass = false;
                            editSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.invisivel, 0);
                            editSenha.setTransformationMethod(new PasswordTransformationMethod());
                        }
                        return true;
                    }else{

                    }
                }
                return false;
            }
        });
    }

    private void registerUser() {
        final String senhaFinal = editSenha.getText().toString().trim();
        final String emailFinal = editEmail.getText().toString().trim();
        final String nomeFinal = editNome.getText().toString().trim();

        if (!senhaFinal.isEmpty() && !emailFinal.isEmpty() && !nomeFinal.isEmpty()) {
            usuario = new Cliente(nomeFinal,emailFinal,senhaFinal);



            SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
            String auxUrl = urlPref.getString("url_base", "");

            String cad_url = auxUrl+"cadCliente.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //*******************ALTERNATIVA PARA A REALIZAÇÃO DE CADASTRO COMPLETO*******************
                            final String[] data = {""};
                            try {
                                if (response.toString().length() > 0 && response.toString() != " ") {
                                    data[0] = response.toString();
                                    JSONObject consultaArray = new JSONObject(data[0]);
                                    JSONObject temcadastro;
                                    JSONArray arrayPedidos = consultaArray.getJSONArray("user");
                                    temcadastro = (JSONObject) arrayPedidos.get(0);

                                    if (temcadastro.getString("status").equals("1")){
                                        Toast.makeText(getApplicationContext(),"Usuário já cadastrado!", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        new AlertDialog.Builder(CadastroNavigation.this)
                                                .setIcon(android.R.drawable.ic_dialog_info)
                                                .setTitle("Cadastrar")
                                                .setMessage("Você deseja fazer o cadastro completo? Somente assim poderá finalizar uma compra. É rápido!")
                                                .setIcon(R.drawable.alert)
                                                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                                                {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(getApplicationContext(), AlterarNavigation.class);
                                                        intent.putExtra("email", ""+usuario.getEmail());
                                                        intent.putExtra("senha", ""+usuario.getSenha());
                                                        startActivity(intent);
                                                    }

                                                })
                                                .setNegativeButton("Não", new DialogInterface.OnClickListener(){
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(getApplicationContext(), LoginNavigation.class);
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                })
                                                .show();

                                    }
                                }
                            } catch (Exception e) {}

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CadastroNavigation.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(nome, usuario.getNome());
                    params.put(email, usuario.getEmail());
                    params.put(senha, usuario.getSenha());
                    return params;
                }

            };


            int timeout= 20000; // 20 segundos
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyProvider.getInstance(this).addRequest(stringRequest);
        }else {
            Toast.makeText(getApplicationContext(),"Insira todas as informações, por favor!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        registerUser();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }
}
