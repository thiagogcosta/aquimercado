package com.aquimercado.aquimercado.indicar;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class IndicarNavigation extends AppCompatActivity {

    private ImageButton imbwhatsapp;
    private ImageButton imbemail;
    private EditText editEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indicar_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        final String id_cliente = clienteid.getString("id_cliente", "");

        imbwhatsapp = (ImageButton) findViewById(R.id.imbwhatsapp);
        imbemail = (ImageButton) findViewById(R.id.imbemail);


        imbemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(IndicarNavigation.this);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        enviaEmail(id_cliente, editEmail.getText().toString());
                        // Toast.makeText(getApplicationContext(), "Indicado com sucesso!"+editEmail.getText(), Toast.LENGTH_SHORT).show();


                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                builder.setMessage("Digite o e-mail do seu amigo: ")
                        .setTitle("Prêmio - 30 pontos");
                // Setting Icon to Dialog

                // DECLARACAO DO EDITTEXT
                editEmail = new EditText(IndicarNavigation.this);
                editEmail.setHint("Email: ");
                editEmail.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
                LinearLayout ll = new LinearLayout(IndicarNavigation.this);
                ll.setOrientation(LinearLayout.VERTICAL);
                ll.addView(editEmail);
                builder.setView(ll);


                builder.setIcon(R.drawable.alert);
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        imbwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enviaWhatsapp(id_cliente,"");
            }

        });

        new MenuPrincipal(this.getApplicationContext(), this);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }


    private void enviaEmail(String id_cliente, String email) {

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String cad_url = auxUrl + "insertConvite.php?id_cliente=" + id_cliente + "&email=" + email;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(IndicarNavigation.this, "Foi enviado um e-mail para seu amigo!", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IndicarNavigation.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };


        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }

    private void enviaWhatsapp(String id_cliente, String email) {

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String cad_url = auxUrl + "insertConvite.php?id_cliente=" + id_cliente + "&email=" + email;
        Log.d("urlconvite1=",cad_url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jObject  = new JSONObject(response); // json
                            JSONArray jsonImg = jObject.getJSONArray("link");
                            JSONObject jsonMercadoItem =
                                    jsonImg.getJSONObject(0);

                            String url = jsonMercadoItem.getString("url");
                            //data[0] = response.toString();
                            //JSONObject consultaArray = new JSONObject(data[0]);
                            //JSONObject infoLink;
                            //Log.d("urlconvite=",response.toString());
                            //JSONArray arrayLink = consultaArray.getJSONArray("link");
                            //infoLink = (JSONObject) arrayLink.get(0);

                            //String url = infoLink.getString("url");
                            shareItem(url);
                            //Toast.makeText(IndicarNavigation.this, "Foi enviado um e-mail para seu amigo!", Toast.LENGTH_SHORT).show();


                        } catch (Exception e) {}
                    }



                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IndicarNavigation.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };


        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }
    //*****************************************COMPARTILHAR PUBLICIDADE***********************************
    public void shareItem(String url) {
                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "indicação: aquiMercado.com");
        i.putExtra(Intent.EXTRA_TEXT, url);

                startActivity(Intent.createChooser(i, "Compartilhar Link"));
    }


}
