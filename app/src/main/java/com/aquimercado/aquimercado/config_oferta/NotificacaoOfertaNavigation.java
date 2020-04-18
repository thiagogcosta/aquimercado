package com.aquimercado.aquimercado.config_oferta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.mercado.MercadoItemNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotificacaoOfertaNavigation extends AppCompatActivity {


  private Switch swbebidas,swlimpeza,swcarnes,swbebes,swalimentos,swperfumaria,
                    swfeira,swmassas,swbiscoitos,swpet,swmolhos,swnaturais;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacaooferta_navigation);
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

        swbebidas = (Switch) findViewById(R.id.swbebidas);
        swlimpeza = (Switch) findViewById(R.id.swlimpeza);
        swcarnes = (Switch) findViewById(R.id.swcarnes);
        swbebes = (Switch) findViewById(R.id.swbebes);
        swalimentos = (Switch) findViewById(R.id.swalimentos);
        swperfumaria = (Switch) findViewById(R.id.swperfumaria);
        swfeira = (Switch) findViewById(R.id.swfeira);
        swmassas = (Switch) findViewById(R.id.swmassas);
        swbiscoitos = (Switch) findViewById(R.id.swbiscoitos);
        swpet = (Switch) findViewById(R.id.swpet);
        swmolhos = (Switch) findViewById(R.id.swmolhos);
        swnaturais = (Switch) findViewById(R.id.swnaturais);
        Intent intent = getIntent();
        b = intent.getExtras();
       carregarstatusnotifica(id_cliente);




        new MenuPrincipal(this.getApplicationContext(), this);


    }

    @Override
    public void onBackPressed() {
        super.onResume();
        Double latitude = (Double) b.get("latitude");
        Double longitude = (Double) b.get("longitude");
        Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("latitude", latitude );
        intent.putExtra("longitude", longitude);
        startActivity(intent);
        finish();
    }


    private boolean carregarstatusnotifica(final String id_cliente) {

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String cad_url = auxUrl + "carregarstatusnotifica.php?id_cliente=" + id_cliente;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObject = new JSONObject(response); // json
                            JSONArray jsonImg = jObject.getJSONArray("statusoferta");
                            JSONObject jsonMercadoItem = jsonImg.getJSONObject(0);

                            String sbebidas = jsonMercadoItem.getString("sbebidas");
                            String slimpeza = jsonMercadoItem.getString("slimpeza");
                            String scarnes = jsonMercadoItem.getString("scarnes");
                            String sbebes = jsonMercadoItem.getString("sbebes");
                            String salimentos = jsonMercadoItem.getString("salimentos");
                            String sperfumaria = jsonMercadoItem.getString("sperfumaria");
                            String sfeira = jsonMercadoItem.getString("sfeira");
                            String smassas = jsonMercadoItem.getString("smassas");
                            String sbiscoitos = jsonMercadoItem.getString("sbiscoitos");
                            String spet = jsonMercadoItem.getString("spet");
                            String smolhos = jsonMercadoItem.getString("smolhos");
                            String snaturais = jsonMercadoItem.getString("snaturais");

                            if(sbebidas.equals("1")){swbebidas.setChecked(true);}else{swbebidas.setChecked(false);}
                            if(slimpeza.equals("1")){swlimpeza.setChecked(true);}else{swlimpeza.setChecked(false);}
                            if(scarnes.equals("1")){swcarnes.setChecked(true);}else{swcarnes.setChecked(false);}
                            if(sbebes.equals("1")){swbebes.setChecked(true);}else{swbebes.setChecked(false);}
                            if(salimentos.equals("1")){swalimentos.setChecked(true);}else{swalimentos.setChecked(false);}
                            if(sperfumaria.equals("1")){swperfumaria.setChecked(true);}else{swperfumaria.setChecked(false);}
                            if(sfeira.equals("1")){swfeira.setChecked(true);}else{swfeira.setChecked(false);}
                            if(smassas.equals("1")){swmassas.setChecked(true);}else{swmassas.setChecked(false);}
                            if(sbiscoitos.equals("1")){swbiscoitos.setChecked(true);}else{swbiscoitos.setChecked(false);}
                            if(spet.equals("1")){swpet.setChecked(true);}else{swpet.setChecked(false);}
                            if(smolhos.equals("1")){swmolhos.setChecked(true);}else{swmolhos.setChecked(false);}
                            if(snaturais.equals("1")){swnaturais.setChecked(true);}else{swnaturais.setChecked(false);}

                            listernersSW(id_cliente);

                        } catch (Exception e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificacaoOfertaNavigation.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

        };


        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);
        return true;
    }


    private void switchNotficacaoOferta(String id_cliente, final String op, String tipo) {

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String cad_url = auxUrl + "switchNotficacaoOferta.php?id_cliente="+id_cliente+"&op="+op+"&tipo="+tipo;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, cad_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                             if (op.equals("1")){Toast.makeText(getApplicationContext(), "Ativado", Toast.LENGTH_SHORT).show();}
                            else {Toast.makeText(getApplicationContext(), "Desativado", Toast.LENGTH_SHORT).show();}

                        } catch (Exception e) {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NotificacaoOfertaNavigation.this, error.toString(), Toast.LENGTH_SHORT).show();
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

    public void listernersSW(final String id_cliente){
        swbebidas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","1");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","1");
                }
            }
        });
        swlimpeza.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","2");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","2");
                }
            }
        });
        swcarnes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","3");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","3");
                }
            }
        });
        swbebes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","4");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","4");
                }
            }
        });
        swalimentos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","5");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","5");
                }
            }
        });
        swperfumaria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","6");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","6");
                }
            }
        });
        swfeira.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","7");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","7");
                }
            }
        });
        swmassas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","8");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","8");
                }
            }
        });

        swbiscoitos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","9");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","9");
                }
            }
        });
        swpet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","10");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","10");
                }
            }
        });
        swmolhos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","11");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","11");
                }
            }
        });

        swnaturais.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Foi Desativado
                if (!isChecked){switchNotficacaoOferta(id_cliente,"0","12");
                }else{ // Foi ativado
                    switchNotficacaoOferta(id_cliente,"1","12");
                }
            }
        });
    }


}
