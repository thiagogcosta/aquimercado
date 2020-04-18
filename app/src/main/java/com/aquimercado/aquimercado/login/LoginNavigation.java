package com.aquimercado.aquimercado.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class LoginNavigation extends AppCompatActivity
        implements View.OnClickListener,Response.Listener<JSONObject>, Response.ErrorListener {

    //***************************************pegando o caminho da url pelo shared******************************


    private EditText editEmail;
    private EditText editSenha;
    private TextView txtLogin;
    private TextView txtEsqueceu;
    private Cliente usuario;
    private Cliente user;
    private Button btEntrar;
    private String auxNulo;
    private String auxNome;
    private String auxIdCliente;
    private String auxEstado;
    private String auxCidade;
    private String auxRua;
    private String auxNumero;
    private String auxTipo;
    Boolean aux_visible_pass;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    String response = null;
    //SharedPreferences infos

    public static final String MyPREFERENCES = "SessaoUser";
    public static final String id_pref = "id_key";
    public static final String nome_pref = "nome_key";
    public static final String email_pref = "email_key";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        //Componentes
        txtLogin = (TextView) findViewById(R.id.txtCadastrar);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastroNavigation.class);
                finish();
                startActivity(intent);
            }
        });

        txtEsqueceu = (TextView) findViewById(R.id.txtEsqueceu);
        txtEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // alertDialog recuperar senha
                alertRecuperar();

            }
        });


        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editEmail.clearFocus();
        editSenha.clearFocus();

        //************************* editEmail e EditSenha SHARED ********************************************
        SharedPreferences emailPref = getApplication().getSharedPreferences("SessaoEMAIL", Context.MODE_PRIVATE);
        String auxEmail = emailPref.getString("email", "");

        SharedPreferences senhaPref = getApplication().getSharedPreferences("SessaoSENHA", Context.MODE_PRIVATE);
        String auxSenha = senhaPref.getString("senha", "");

        if(auxEmail.length() > 0) {
            editEmail.setText(auxEmail);
            editSenha.setText(auxSenha);

        }
        //*****************************************************************************************************8

        btEntrar = (Button) findViewById(R.id.btEntrar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new MenuPrincipal(this.getApplicationContext(), this);

        //************************************************************************************

        btEntrar.setOnClickListener(this);

        //************************************************************************************

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }


    private void verificaUser() {
        final String senha = editSenha.getText().toString().trim();
        final String email = editEmail.getText().toString().trim();

        //--------------------------checkbox--------------------------------------
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_autologin);

        if (checkBox.isChecked()) {
            //email
            SharedPreferences emailPref = getApplication().getSharedPreferences("SessaoEMAIL", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorEmail = emailPref.edit();
            editorEmail.putString("email", editEmail.getText().toString().trim());
            editorEmail.apply();

            //senha
            SharedPreferences senhaPref = getApplication().getSharedPreferences("SessaoSENHA", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorSenha = senhaPref.edit();
            editorSenha.putString("senha", editSenha.getText().toString().trim());
            editorSenha.apply();
        }

        //-------------------------------------------------------------------------
        usuario = new Cliente(email, senha);
        //Aqui eu faço a verificação de logado...

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String log_url = auxUrl+"logCliente.php";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, log_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject jObject  = new JSONObject(response); // json
                            JSONObject jUsuarios = jObject.getJSONObject("usuarios");
                            JSONArray jUsuario = jUsuarios.getJSONArray("usuario");

                            for (int i = 0; i < jUsuario.length(); i++) {
                                JSONObject jsonUsuarioItem =
                                        jUsuario.getJSONObject(i);
                                String login =
                                        jsonUsuarioItem.getString("login");
                                String nulo =
                                        jsonUsuarioItem.getString("nulo");
                                String nome =
                                        jsonUsuarioItem.getString("nome");
                                String id_cliente =
                                        jsonUsuarioItem.getString("id_cliente");
                                String estado =
                                        jsonUsuarioItem.getString("estado");
                                String cidade =
                                        jsonUsuarioItem.getString("cidade");
                                String rua =
                                        jsonUsuarioItem.getString("rua");
                                String numero =
                                        jsonUsuarioItem.getString("numero");
                                String tipo =
                                        jsonUsuarioItem.getString("tipo");

                                user = new Cliente(login);
                                auxNulo = nulo;
                                auxNome = nome;
                                auxIdCliente = id_cliente;
                                auxEstado = estado;
                                auxCidade = cidade;
                                auxRua = rua;
                                auxNumero = numero;
                                auxTipo = tipo;



                            }
                        }catch(Exception e){
                        }



                                //Shared de Logar
                        SharedPreferences produtosPref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = produtosPref.edit();
                        editor.putString("logado", user.getId());
                        editor.putString("id_cliente",""+auxIdCliente);
                        editor.putString("tipo", ""+auxTipo);
                        editor.apply();


                        SharedPreferences produtosPref2 = getApplication().getSharedPreferences("SessaoUserFULL", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = produtosPref2.edit();
                        editor2.putString("preenchido", auxNulo);
                        editor2.apply();

                        SharedPreferences produtosPref3 = getApplication().getSharedPreferences("SessaoUserNOME", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor3 = produtosPref3.edit();
                        editor3.putString("nome", auxNome);
                        editor3.apply();

                        SharedPreferences produtosEndEntrega = getApplication().getSharedPreferences("enderecoEntrega", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edtEndEntrega = produtosEndEntrega.edit();
                        edtEndEntrega.putString("estado", auxEstado);
                        edtEndEntrega.putString("cidade", auxCidade);
                        edtEndEntrega.putString("rua", auxRua);
                        edtEndEntrega.putString("numero", auxNumero);
                        edtEndEntrega.apply();


                        //login
                        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                        finish();
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        switch (error.networkResponse.statusCode) {
                            case 401:
                                Toast.makeText(LoginNavigation.this, "Não autorizado!", Toast.LENGTH_SHORT).show();
                                break;
                            case 417:
                                Toast.makeText(LoginNavigation.this, "Erros nos parâmetros!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(LoginNavigation.this, "Erro inesperado!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", usuario.getEmail());
                params.put("senha", usuario.getSenha());
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


    public void alertRecuperar(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
        builder.setTitle("E-mail de recuperação de senha");
        builder.setIcon(R.drawable.alert);

        final EditText email = new EditText(this);
        email.setHint("E-mail: ");

        email.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(email);

        builder.setView(ll);


        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            // enviar e-mail
                SharedPreferences sharedUrlBase = getApplication().getSharedPreferences("URLS", MODE_PRIVATE);
                String auxUrlBase = sharedUrlBase.getString("url_base", "");
                String emailUTF8="";
                try {
                    emailUTF8 = URLEncoder.encode(email.getText().toString(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = auxUrlBase+"recpassAndroid.php?email="+emailUTF8;
                Toast.makeText(getApplicationContext(), "Solicitação Enviada!", Toast.LENGTH_SHORT).show();
                //valida

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                //final String[] data = {""};
                               // try {
                                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                //} catch (Exception e) {

                                //}
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

                /* --------------- Retarda o timeout VOLLEY... evita o reenvio da requisicao*/
                int timeout= 20000; // 20 segundos
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        timeout,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onClick(View v) {
        verificaUser();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(LoginNavigation.this, "" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

    }


}