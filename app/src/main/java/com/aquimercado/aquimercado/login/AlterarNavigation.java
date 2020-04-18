package com.aquimercado.aquimercado.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.aquimercado.aquimercado.SplashscreenFIRST;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;


public class AlterarNavigation extends AppCompatActivity
        implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener{

    private EditText  senha, cpf, nascimento, telefone, cep, estado, rua, num, cidade;
    private  TextView email, nome;
    private String auxiliando;
    private TextView id;
    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    private String url = "";
    ProgressBar progressBar;
    int email_status=-1;
    int cep_valido=-1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new MenuPrincipal(this.getApplicationContext(), this);


        progressBar  = (ProgressBar) findViewById(R.id.pbAlterar);
        progressBar.setVisibility(View.INVISIBLE);


        //********************************************* ***************************************

        //**********************COMPONENTES*******************************************

         id = (TextView) findViewById(R.id.editAltId);
         nome = (TextView) findViewById(R.id.txtAltNome);
         email = (TextView) findViewById(R.id.editAltEmail);
         senha = (EditText) findViewById(R.id.editAltSenha);
         cpf = (EditText) findViewById(R.id.editAltCPF);
         nascimento = (EditText) findViewById(R.id.editAltNascimento);
         telefone = (EditText) findViewById(R.id.editAltCelular);
         cep = (EditText) findViewById(R.id.editAltCEP);
         estado = (EditText) findViewById(R.id.editAltEstado);
         cidade = (EditText) findViewById(R.id.editAltCidade);
         rua = (EditText) findViewById(R.id.editAltRua);
         num = (EditText) findViewById(R.id.editAltNum);

        //******************************MÁSCARA*********************************************

        MaskEditTextChangedListener maskCpf = new MaskEditTextChangedListener("###.###.###-##", cpf);
        MaskEditTextChangedListener maskNasc = new MaskEditTextChangedListener("##/##/####", nascimento);
        MaskEditTextChangedListener maskTelf = new MaskEditTextChangedListener("(##)#####-####", telefone);
        MaskEditTextChangedListener maskCep = new MaskEditTextChangedListener("#####-###", cep);


        cpf.addTextChangedListener(maskCpf);
        nascimento.addTextChangedListener(maskNasc);
        telefone.addTextChangedListener(maskTelf);
        cep.addTextChangedListener(maskCep);



        //*************************GET**********************************************
        SharedPreferences produtosPref = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        auxiliando = produtosPref.getString("logado", "");


        String[] array = auxiliando.split("");

        String aux2 =  "";

        for(int i = 0; i< array.length; i++){
            if(isNumeric(array[i])){
               aux2 += array[i];
            }
        }

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        SharedPreferences auxLogado = getApplicationContext().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxiliando = auxLogado.getString("logado", "");

        //********************************VERIFICAÇÃO DE CADASTRO COMPLETO APÓS LOGAR******************************
        // captura latitude e longitude do smartphone (vem do splashscreenFirt)
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (auxiliando.length() > 0) {
            url = auxUrl+"makejsonInfoUser.php?id="+auxiliando;
        }else {
            url = auxUrl+"makejsonInfoUserAlterar.php?email="+b.get("email")+"&&senha="+b.get("senha");
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

        Log.d("id_que_ta_vindo2","-->"+auxiliando);

        //Clique do Botão
        Button bt = (Button)findViewById(R.id.btAlterar);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(cep_valido==0){

                Log.d("id_que_ta_vindo4",""+id.getText().toString().trim());

                final String postId = id.getText().toString().trim();
                final String postNome = nome.getText().toString().trim();
                final String postEmail = email.getText().toString().trim();
                final String postSenha = senha.getText().toString().trim();
                final String postCpf = cpf.getText().toString().trim();
                final String postNascimento = nascimento.getText().toString().trim();
                final String postTelefone = telefone.getText().toString().trim();
                final String postCep = cep.getText().toString().trim();
                final String postEstado = estado.getText().toString().trim();
                final String postCidade = cidade.getText().toString().trim();
                final String postRua = rua.getText().toString().trim();
                final String postNum = num.getText().toString().trim();


                //******************************************Tirando a mascara****************************************

                String auxCPF = postCpf.replace(".", "");
                final String aux2CPF = auxCPF.replace("-", "");

                final String auxNascimento = postNascimento.replace("-", "");

                final String auxCep = postCep.replace("-", "");

                String auxTelefone = postTelefone.replace("-", "");
                String aux2Telefone = auxTelefone.replace("(", "");
                final String aux3Telefone = aux2Telefone.replace(")", "");

                Log.d("OKZ2id_post", postId);
                Log.d("OKZ2nome", postNome);
                Log.d("OKZ2email", postEmail);
                Log.d("OKZ2senha", postSenha);
                Log.d("OKZ2nascimento", auxNascimento);
                Log.d("OKZ2CEP", auxCep);
                Log.d("OKZ2estado", postEstado);
                Log.d("OKZ2cidade", postCidade);
                Log.d("OKZ2rua", postRua);
                Log.d("OKZ2numero", postNum);
                Log.d("OKZ2telefone", aux3Telefone);
                Log.d("OKZ2cpf", aux2CPF);

                if (!postNome.isEmpty() && !postId.isEmpty() && !postEmail.isEmpty() && !postSenha.isEmpty() && !auxNascimento.isEmpty() && !auxCep.isEmpty() && !postEstado.isEmpty() && !postCidade.isEmpty() && !postRua.isEmpty() && !postNum.isEmpty() && !aux3Telefone.isEmpty() && !aux2CPF.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);

                    //*******************************************************************

                    String queryId = null;
                    String queryNome = null;
                    String queryEmail= null;
                    String querySenha = null;
                    String queryNascimento = null;
                    String queryCEP = null;
                    String queryEstado = null;
                    String queryCidade = null;
                    String queryRua = null;
                    String queryNumero = null;
                    String queryTelefone = null;
                    String queryCpf = null;

                    try {
                        queryId = URLEncoder.encode(postId, "utf-8");
                        queryNome = URLEncoder.encode(postNome, "utf-8");
                        queryEmail = URLEncoder.encode(postEmail, "utf-8");
                        querySenha = URLEncoder.encode(postSenha, "utf-8");
                        queryNascimento = URLEncoder.encode(auxNascimento, "utf-8");
                        queryCEP = URLEncoder.encode(auxCep, "utf-8");
                        queryEstado = URLEncoder.encode(postEstado, "utf-8");
                        queryCidade = URLEncoder.encode(postCidade, "utf-8");
                        queryRua = URLEncoder.encode(postRua, "utf-8");
                        queryNumero = URLEncoder.encode(postNum, "utf-8");
                        queryTelefone = URLEncoder.encode(aux3Telefone, "utf-8");
                        queryCpf = URLEncoder.encode(aux2CPF, "utf-8");

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
                    String auxUrl = urlPref.getString("url_base", "");

                    String urlcad = auxUrl+"cadClienteFull.php?id="+queryId+"&nome="+queryNome+"&email="+queryEmail+"&senha="+querySenha+"&nascimento="+queryNascimento+"&cpf="+queryCpf+"&CEP="+queryCEP+"&estado="+queryEstado+"&cidade="+queryCidade+"&rua="+queryRua+"&numero="+queryNumero+"&telefone="+queryTelefone;

                    Log.d("url utf8: ",urlcad);

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlcad,
                            new Response.Listener<String>() {
                                //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onResponse(String response) {

                                    SharedPreferences auxLogado = getApplicationContext().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                                    String auxiliando = auxLogado.getString("logado", "");
                                    SharedPreferences emailPref = getApplication().getSharedPreferences("SessaoEMAIL", Context.MODE_PRIVATE);
                                    String auxEmail = emailPref.getString("email", "");

                                    progressBar.setVisibility(View.INVISIBLE);

                                    if (!auxEmail.equals(email.getText().toString())) {
                                        SharedPreferences.Editor editorEmail = emailPref.edit();
                                        editorEmail.putString("email", email.getText().toString());
                                        editorEmail.apply();
                                    }
                                    if(auxiliando.length() > 0){

                                        Toast.makeText(getApplicationContext(), "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                                        finish();
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Registrado com sucesso", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LoginNavigation.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            int timeout= 20000; // 20 segundos
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    timeout,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);

                } else {
                    Toast.makeText(getApplicationContext(), "Insira todos as informações, por favor!", Toast.LENGTH_SHORT).show();
                }

                }
                else{
                     /*if (email_status==1){
                         Toast.makeText(getApplicationContext(), "E-mail de usuário já cadastrado!", Toast.LENGTH_LONG).show();
                     }*/
                    if (cep_valido==1){
                        Toast.makeText(getApplicationContext(), "CEP Inválido!", Toast.LENGTH_LONG).show();
                    }
                    /*if (email_status==-1){
                        verificaUser(email.getText().toString());
                    }*/
                    if (cep_valido==-1){
                        consultaCEP(cep.getText().toString(),0);
                    }
                }
                    }
        });



        // ############# consulta CEP ############
        cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    consultaCEP(cep.getText().toString(),1);
                    cep.setBackgroundColor(Color.LTGRAY);
                }else{
                    cep.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "CEP", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // ############# consulta Email ############
        /*email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    verificaUser(email.getText().toString());
                    email.setBackgroundColor(Color.LTGRAY);
                }else{
                    email.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "E-mail", Toast.LENGTH_SHORT).show();

                }
            }
        });*/

        //   ############################## troca cor ##################
        senha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {senha.setBackgroundColor(Color.LTGRAY);}
                else{senha.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Senha", Toast.LENGTH_SHORT).show();

                }
            }
        });

        cpf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {cpf.setBackgroundColor(Color.LTGRAY);}
                else{cpf.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "CPF", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nascimento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {


                if (!hasFocus) {nascimento.setBackgroundColor(Color.LTGRAY);}
                else{nascimento.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Data de Nascimento", Toast.LENGTH_SHORT).show();
                }
            }
        });
        telefone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {telefone.setBackgroundColor(Color.LTGRAY);}
                else{telefone.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Celular", Toast.LENGTH_SHORT).show();
                }
            }
        });
        estado.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {estado.setBackgroundColor(Color.LTGRAY);}
                else{estado.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Estado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cidade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {cidade.setBackgroundColor(Color.LTGRAY);}
                else{cidade.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Cidade", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rua.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {rua.setBackgroundColor(Color.LTGRAY);}
                else{rua.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Rua/Avenida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {num.setBackgroundColor(Color.LTGRAY);}
                else{num.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "Número", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //new MenuPrincipal(this.getApplicationContext(), this);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,"Usuário Inválido",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        Cliente cliente = new Cliente();

        try {
            // Não precisamos converter o
            // InputStream em String \o/
            JSONObject jsonMercadoP =
                    response.getJSONObject("usuarios");
            JSONArray jsonMercado =
                    jsonMercadoP.getJSONArray("user");

            for (int i = 0; i < jsonMercado.length(); i++) {
                JSONObject jsonMercadoItem =
                        jsonMercado.getJSONObject(i);
                String jId =
                        jsonMercadoItem.getString("id");


                String jNome = jsonMercadoItem.getString("nome");


                String jEmail = jsonMercadoItem.getString("email");


                String jSenha = jsonMercadoItem.getString("senha");

                String jCpf =
                        jsonMercadoItem.getString("cpf");
                String jNascimento =
                        jsonMercadoItem.getString("nascimento");

                String jTelefone =
                        jsonMercadoItem.getString("telefone");
                String jCep =
                        jsonMercadoItem.getString("CEP");


                String jEstado =  jsonMercadoItem.getString("estado");


                String jCidade = jsonMercadoItem.getString("cidade");


                String jRua = jsonMercadoItem.getString("rua");


                String jNumero =
                        jsonMercadoItem.getString("numero");

                cliente.setId(jId);
                cliente.setNome(jNome);
                cliente.setEmail(jEmail);
                cliente.setSenha(jSenha);
                cliente.setCPF(jCpf);
                cliente.setNascimento(jNascimento);
                cliente.setCep(jCep);
                cliente.setEstado(jEstado);
                cliente.setCidade(jCidade);
                cliente.setRua(jRua);
                cliente.setNum(jNumero);
                cliente.setTelefone(jTelefone);

            }

            Log.d("id_que_ta_vindo3",""+cliente.getId());

            id.setText(cliente.getId());
            nome.setText(cliente.getNome());
            email.setText(cliente.getEmail());
            senha.setText(cliente.getSenha());
            cpf.setText(cliente.getCPF());
            nascimento.setText(cliente.getNascimento());
            telefone.setText(cliente.getTelefone());
            cep.setText(cliente.getCep());
            estado.setText(cliente.getEstado());
            cidade.setText(cliente.getCidade());
            rua.setText(cliente.getRua());
            num.setText(cliente.getNum());


        }catch(Exception e){
        }
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @Override
    public void onClick(View v) {

    }


   /* public void verificaUser(String email) {

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        SharedPreferences emailPref = getApplication().getSharedPreferences("SessaoEMAIL", Context.MODE_PRIVATE);
        String auxEmail = emailPref.getString("email", "");
        if (auxEmail.equals(email)) {
            email_status = 0;
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String urlverifica = auxUrl + "verificaUser.php?email=" + email;

            Log.d("urluser=",urlverifica);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlverifica,
                    new Response.Listener<String>() {
                        //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onResponse(String response) {

                            final String[] data = {""};
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                if (response.toString().length() > 0 && response.toString() != " ") {
                                    data[0] = response.toString();



                                    JSONObject consultaArray = null;
                                    consultaArray = new JSONObject(data[0]);

                                    JSONObject temcadastro;
                                    JSONArray arrayPedidos = consultaArray.getJSONArray("user");
                                    temcadastro = (JSONObject) arrayPedidos.get(0);

                                    if (temcadastro.getString("status").equals("1")) {
                                        email_status = 1;
                                        Toast.makeText(getApplicationContext(), "Usuário já cadastrado.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        email_status = 0;
                                    }

                                }

                            } catch (JSONException e) {
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            int timeout = 20000; // 20 segundos
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);

        }
    }
*/
    public void consultaCEP(String CEP, final int flag){

        progressBar.setVisibility(View.VISIBLE);
        String urlcep = "http://viacep.com.br/ws/"+CEP+"/json/";

        Log.d("url utf8: ",urlcep);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlcep,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {

                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                progressBar.setVisibility(View.INVISIBLE);
                                if (consultaArray.has("erro")) {
                                    Toast.makeText(getApplicationContext(), "CEP inválido! É muito importante o cadastro de um endereço válido.", Toast.LENGTH_SHORT).show();
                                    cep_valido = 1;
                                }
                                else{
                                    cidade.setText(consultaArray.getString("localidade"));
                                    estado.setText(consultaArray.getString("uf"));
                                    rua.setText(consultaArray.getString("logradouro"));

                                   if (flag==1){
                                       estado.setBackgroundColor(Color.LTGRAY);
                                       rua.setBackgroundColor(Color.LTGRAY);
                                       cidade.setBackgroundColor(Color.LTGRAY);
                                       num.requestFocus();
                                   }

                                    cep_valido = 0;

                                }

                            }

                    } catch (Exception e) {}

                                }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);
    }
}
