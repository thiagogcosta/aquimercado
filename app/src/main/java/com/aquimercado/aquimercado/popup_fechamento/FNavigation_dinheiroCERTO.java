package com.aquimercado.aquimercado.popup_fechamento;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.VolleySingleton;
import com.aquimercado.aquimercado.fechamento.SplashFechamento;
import com.aquimercado.aquimercado.notificacao.NotificationEventReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FNavigation_dinheiroCERTO extends AppCompatActivity {

    public static Intent intent = null;
    public static Bundle b = null;
    private ImageLoader mImageLoader;
    private String auxUrlBase;
    private String auxUrlBaseImagem,auxsharedComboDepto, auxsharedCombo, auxsharedIdUser, auxLatitude, auxLongitude, auxEstado, auxCidade, auxRua, auxNum;
    private String  endEstado, endCidade, endRua, endNum;
    private Button bt;
    private TextView txtEnd, txtFrete;
    private float troco = 0;
    public static EditText edtTroco;
    private float eq,vTeq;
    private int escolhido;
    private NumberFormat moeda;
    private TextView txt_valorEQ;

    // campos alterar endereco
    private EditText CEP;
    private EditText estado;
    private EditText cidade;
    private EditText rua;
    private EditText numero;
    private int cep_valido=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fnavigation_dinheiro_certo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);

        //*****************************************************************************************************
        SharedPreferences settings = getApplicationContext().getSharedPreferences("produtos", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        //*************************************PEGANDO AS INFORMAÇÕES******************************************
        intent = getIntent();
        b = intent.getExtras();

        escolhido = getIntent().getIntExtra("entregaRetiradaESCOLHA",0);

        SharedPreferences sharedUrlBase = getApplication().getSharedPreferences("URLS", MODE_PRIVATE);
        auxUrlBase = sharedUrlBase.getString("url_base", "");

        SharedPreferences sharedUrlBaseImg = getApplication().getSharedPreferences("URLSIMAGEM", MODE_PRIVATE);
        auxUrlBaseImagem = sharedUrlBaseImg.getString("url_base", "");

        SharedPreferences sharedNomeMercado = getApplication().getSharedPreferences("INFOS_MERCADO", MODE_PRIVATE);
        String auxsharedNomeMercado = sharedNomeMercado.getString("nome_mercado", "");

        SharedPreferences sharedLogoMercado = getApplication().getSharedPreferences("INFOS_MERCADO", MODE_PRIVATE);
        String auxsharedLogoMercado = sharedLogoMercado.getString("logo_mercado", "");

        SharedPreferences sharedCombo = getApplication().getSharedPreferences("INFOS_MERCADO", MODE_PRIVATE);
        auxsharedCombo = sharedCombo.getString("id_combo", "");

        SharedPreferences sharedcombo_depto = getApplication().getSharedPreferences("INFOS_MERCADO", MODE_PRIVATE);
        auxsharedComboDepto = sharedcombo_depto.getString("combo_depto", "");

        SharedPreferences sharedIdUser = getApplication().getSharedPreferences("SessaoUser", MODE_PRIVATE);
        auxsharedIdUser = sharedIdUser.getString("id_cliente", "");

        SharedPreferences sharedLatitude = getApplication().getSharedPreferences("Latitude-Longitude", MODE_PRIVATE);
        auxLatitude = sharedLatitude.getString("latitude", "");

        SharedPreferences sharedLongitude = getApplication().getSharedPreferences("Latitude-Longitude", MODE_PRIVATE);
        auxLongitude = sharedLongitude.getString("longitude", "");

        SharedPreferences sEstado = getApplication().getSharedPreferences("enderecoEntrega", MODE_PRIVATE);
        auxEstado = sEstado.getString("estado", "");

        SharedPreferences sCidade = getApplication().getSharedPreferences("enderecoEntrega", MODE_PRIVATE);
        auxCidade = sCidade.getString("cidade", "");

        SharedPreferences sRua = getApplication().getSharedPreferences("enderecoEntrega", MODE_PRIVATE);
        auxRua = sRua.getString("rua", "");

        SharedPreferences sNumero= getApplication().getSharedPreferences("enderecoEntrega", MODE_PRIVATE);
        auxNum = sNumero.getString("numero", "");

        endEstado = auxEstado;
        endCidade = auxCidade;
        endRua = auxRua;
        endNum = auxNum;



        //*************************************SETANDO LAYOUT*************************************************

        TextView txt_horapedido = (TextView)findViewById(R.id.txt_horapedido);
        TextView txt_valorTotal = (TextView)findViewById(R.id.txt_valorTotal);
        TextView txt_valorFrete = (TextView)findViewById(R.id.txt_valorFrete);
        TextView txtFrete = (TextView)findViewById(R.id.idTextFrete);
        txt_valorEQ = (TextView)findViewById(R.id.txt_valorEQ);
        txtEnd = (TextView)findViewById(R.id.txtEnd);
        NetworkImageView img_mercado = (NetworkImageView)findViewById(R.id.img_mercadoFechamento);
        TextView txt_nomeMercado = (TextView)findViewById(R.id.txt_nomeMercado);
        bt = (Button)findViewById(R.id.btEndEntrega);

        switch (escolhido){
            case 1:
                txt_horapedido.setText("Retirada a partir de: "+b.get("diaestimado")+" - "+b.get("horaestimada"));
                break;
            case 2:
                txt_horapedido.setText("Entrega a partir de: "+b.get("diaestimado")+" - "+b.get("horaestimada"));
                break;
            default:
                txt_horapedido.setText("Disponível em: "+b.get("diaestimado")+" - "+b.get("horaestimada"));
                break;
        }



        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        txt_valorTotal.setText(moeda.format(Double.parseDouble(""+b.get("valorTotal"))));
        //txt_valorFrete.setText(moeda.format(Double.parseDouble(""+b.get("valor_frete"))));

        final Float valorTotal = Float.parseFloat("" +b.get("valorTotal"));
        final Float valorFrete = Float.parseFloat("" +b.get("valor_frete"));

        eq = valorTotal;

        if(escolhido == 1){
            txt_valorFrete.setVisibility(View.GONE);
            txtFrete.setVisibility(View.GONE);
        }else{
            eq += valorFrete;
            txt_valorFrete.setText(moeda.format(Double.parseDouble(""+b.get("valor_frete"))));
        }

        txt_valorEQ.setText(moeda.format(Double.parseDouble(eq+"")));

        txtEnd.setText(endRua+","+endNum+" - "+endCidade+"/"+endEstado);


        if(escolhido == 1){
            bt.setVisibility(View.GONE);
        }

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(FNavigation_dinheiroCERTO.this);

                alertDialog.setTitle("Cadastro de endereço de entrega");

                alertDialog.setMessage("Deseja cadastrar um novo endereço para entrega?");

                alertDialog.setIcon(R.drawable.mapmaker2);

                alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        exibirCadastroEnd();
                    }});

                alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                // FORÇA O TECLADO APARECER AO ABRIR O ALERT
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_shopping_cart_white_24dp));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(escolhido == 2){ // Entrega -- Apresenta mensagem de TROCO
                    registerUser();
                }
                else{ // Direto para janela de confirmação do pedido
                    vTeq = (float) 0;
                    msgConfirmacao();
                }
            }
        });



        mImageLoader = (VolleySingleton.getInstance(getApplicationContext())).getImageLoader();
        img_mercado.setImageUrl(auxsharedLogoMercado,mImageLoader);
        txt_nomeMercado.setText(auxsharedNomeMercado);

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

    private void registerUser() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FNavigation_dinheiroCERTO.this);

        alertDialog.setTitle("Cadastro de troco");

        alertDialog.setMessage("Você precisa de troco?");

        alertDialog.setIcon(R.drawable.cashicon);

        // DECLARACAO DO EDITTEXT
        edtTroco = new EditText(this);
        edtTroco.setHint("Troco: ");

        edtTroco.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(edtTroco);
        alertDialog.setView(ll);

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                final String edtValor = edtTroco.getText().toString().trim();

                if (!edtValor.equals("")) {
                    troco = Float.parseFloat(edtValor);

                    vTeq = troco - eq;

                    //edtTroco.getText().toString();

                        msgConfirmacao();}
                else{
                    Toast.makeText(getApplicationContext(),"Troco Inválido",Toast.LENGTH_SHORT).show();

                }

            }});

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                troco = eq;
                vTeq = (float) 0;
                msgConfirmacao();
            }
        });

        alertDialog.show();

    }

    public void exibirCadastroEnd(){
        AlertDialog.Builder mensagem = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
        mensagem.setTitle("Cadastro de novo endereço");

        // DECLARACAO DO EDITTEXT
        CEP= new EditText(this);
        CEP.setHint("CEP: ");


        estado = new EditText(this);
        estado.setHint("Estado: ");

        cidade = new EditText(this);
        cidade.setHint("Cidade: ");

        rua = new EditText(this);
        rua.setHint("Rua: ");

        numero = new EditText(this);
        numero.setHint("Numero: ");

        CEP.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        cidade.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        rua.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        estado.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        numero.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.addView(CEP);
        ll.addView(estado);
        ll.addView(cidade);
        ll.addView(rua);
        ll.addView(numero);
        mensagem.setView(ll);

        mensagem.setIcon(R.drawable.mapmaker2);

        // ############# consulta CEP ############
        CEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    consultaCEP(CEP.getText().toString(),1);
                    CEP.setBackgroundColor(Color.LTGRAY);
                }else{
                    CEP.setBackgroundColor(Color.parseColor("#FFF8E1"));
                    Toast.makeText(getApplicationContext(), "CEP", Toast.LENGTH_SHORT).show();

                }
            }
        });

        mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        mensagem.setNeutralButton("Confirmar", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences sharedUrlBase = getApplication().getSharedPreferences("URLS", MODE_PRIVATE);
                auxUrlBase = sharedUrlBase.getString("url_base", "");

                // FORÇA O TECLADO Ficar oculto
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);


                final String utf8CEP = CEP.getText().toString();
                final String utf8Estado = estado.getText().toString();
                final String utf8Cidade = cidade.getText().toString();
                final String utf8Rua = rua.getText().toString();
                final String utf8Numero = numero.getText().toString();



                String queryCEP = null;
                String queryE = null;
                String queryC = null;
                String queryR = null;
                String queryN = null;

                try {
                    queryCEP = URLEncoder.encode(utf8CEP, "utf-8");
                    queryE = URLEncoder.encode(utf8Estado, "utf-8");
                    queryC = URLEncoder.encode(utf8Cidade, "utf-8");
                    queryR = URLEncoder.encode(utf8Rua, "utf-8");
                    queryN = URLEncoder.encode(utf8Numero, "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String url = auxUrlBase+"validaEnderecoEntrega.php?cep="+queryE+"estado="+queryE+"&cidade="+queryC+"&rua="+queryR+"&numero="+queryN+"&id_mercado="+b.get("idmercado")+"&lat="+auxLatitude+"&lng="+auxLongitude;

                Log.d("url utf8: ",url);

                //valida


                final String finalQueryCEP = queryCEP;
                final String finalQueryE = queryE;
                final String finalQueryC = queryC;
                final String finalQueryR = queryR;
                final String finalQueryN = queryN;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onResponse(String response) {

                                final String[] data = {""};
                                try {


                                    if(response.toString().length() > 0 && response.toString() != " ") {
                                        data[0] = response.toString();
                                        JSONObject consultaArray = new JSONObject(data[0]);

                                        if (!consultaArray.isNull("entrega")) {

                                            JSONObject infoPedido;

                                            JSONArray arrayPedidos =  consultaArray.getJSONArray("entrega");
                                            infoPedido = (JSONObject) arrayPedidos.get(0);
                                            //JSONObject consultaArray = new JSONObject(arrayPedidos[0]);

                                            // 0 não entrega nesse endereço
                                            // 1 pedido fora das cidades autorizadas
                                            // 2 entrega autorizadada
                                            // 3 endereço de entrega não existe

                                            switch (infoPedido.getString("status")){
                                                case "0":
                                                    Toast.makeText(getApplicationContext(),"Não fazemos entrega no endereço fornecido!",Toast.LENGTH_LONG).show();
                                                    break;
                                                case "1":
                                                    Toast.makeText(getApplicationContext(),"Sua localização se encontra fora das cidades autorizadas para realização de pedido!",Toast.LENGTH_LONG).show();
                                                    break;
                                                case "2":
                                                    endEstado = utf8Estado;
                                                    endCidade = utf8Cidade;
                                                    endRua = utf8Rua;
                                                    endNum = utf8Numero;
                                                    txtEnd.setText(endRua+","+endNum+" - "+endCidade+"/"+endEstado);
                                                    break;
                                                case "3":
                                                    Toast.makeText(getApplicationContext(),"Endereço informado inválido!",Toast.LENGTH_LONG).show();
                                                    break;
                                                default:
                                                    Toast.makeText(getApplicationContext(),"Erro ao consultar a base de dados, logo contate o administrador!",Toast.LENGTH_LONG).show();
                                                    break;
                                            }



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
                VolleyProvider.getInstance(getBaseContext()).addRequest(stringRequest);

            }





        });



        mensagem.show();
        // FORÇA O TECLADO APARECER AO ABRIR O ALERT
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


    public void cadastrarPedido(final float vTeq){

Log.d("troco=",""+vTeq);

        if(vTeq >= 0){

            String cad_url = auxUrlBase+"cadFechamentoPEDIDO_dinheiroOK.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(getApplicationContext(), SplashFechamento.class);

                            //Chama o alarme de pedido pronto
                            NotificationEventReceiver.setupAlarm(getApplicationContext());
                            finish();
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(FNavigation_dinheiroCERTO.this, "Erro!", Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("id_combo",""+auxsharedCombo);
                    params.put("id_cliente",""+auxsharedIdUser);
                    params.put("id_mercado", ""+b.get("idmercado"));
                    params.put("produto", ""+b.get("produto"));
                    params.put("valor_total", ""+eq);
                    params.put("valor_informado", ""+troco);
                    params.put("valor_troco", ""+vTeq);
                    params.put("tipo_entregaRetirada", "" + escolhido);
                    params.put("latitude", "" + auxLatitude);
                    params.put("longitude", "" + auxLongitude);
                    params.put("estado", "" + endEstado);
                    params.put("cidade", "" + endCidade);
                    params.put("combo_depto",""+auxsharedComboDepto);
                    Log.d("combo_deptoF=",auxsharedComboDepto);
                    params.put("rua", "" + endRua);
                    params.put("numero", "" + endNum);
                    Log.d("diaHoraEstimada=", b.get("diaestimado")+" - "+b.get("horaestimada"));
                    params.put("datahoraestimada", b.get("diaestimado")+" - "+b.get("horaestimada"));

                    Log.d("params",params+"");
                    return params;

                }

            };

            int timeout= 20000; // 20 segundos
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyProvider.getInstance(this).addRequest(stringRequest);

        }else{
            Toast.makeText(getApplicationContext(),"Troco inválido!",Toast.LENGTH_SHORT).show();
        }
    }

    public int msgConfirmacao(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FNavigation_dinheiroCERTO.this);

        alertDialog.setTitle("Confirmação de Pedido");

        alertDialog.setMessage("Sua solicitação será enviada ao estabelecimento. Confirma seu pedido?");

        alertDialog.setIcon(R.drawable.mapmaker2);

        alertDialog.setPositiveButton("SIM", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int which) {
                cadastrarPedido(vTeq);
            }});

        alertDialog.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();

        return 1;
    }

    public void consultaCEP(String CEP, final int flag){

        //progressBar.setVisibility(View.VISIBLE);
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

                                //progressBar.setVisibility(View.INVISIBLE);
                                if (consultaArray.has("erro")) {
                                    Toast.makeText(getApplicationContext(), "CEP inválido! É muito importante o cadastro de um endereço válido.", Toast.LENGTH_SHORT).show();
                                    cep_valido = 1;
                                }
                                else{
                                    cidade.setText(consultaArray.getString("localidade"));
                                    estado.setText(consultaArray.getString("uf"));
                                    rua.setText(consultaArray.getString("logradouro"));

                                    if (flag==1){
                                        numero.requestFocus();
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
