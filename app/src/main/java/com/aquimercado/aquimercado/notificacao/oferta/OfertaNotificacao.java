package com.aquimercado.aquimercado.notificacao.oferta;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OfertaNotificacao extends AppCompatActivity {


    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager gridLayoutManager;
    GridAdapterOferta mAdapter;
    public static String auxUrlimagem = null;
    public Context ctx;
    List<OfertaNotificacaoItem> ofertas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofertaitem_item_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mercado_oferta);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutFrozen(true);
        gridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        ofertas = new ArrayList<OfertaNotificacaoItem>();
        mAdapter = new GridAdapterOferta(ofertas);
        mRecyclerView.setAdapter(mAdapter);
        SharedPreferences clienteid = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String id_cliente = clienteid.getString("id_cliente", "");
        //URL da IMAGEM
        SharedPreferences urlImagem = getApplication().getSharedPreferences("URLSIMAGEM", Context.MODE_PRIVATE);
        auxUrlimagem = urlImagem.getString("url_base", "");

        startLoadMercado(id_cliente);
    }


    public void startLoadMercado(String id_cliente){

        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "verificaOfertas.php?id_cliente="+id_cliente;

        Log.d("urlmercado=",strUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if(response.toString().length() > 0 && response.toString() != " ") {

                                JSONObject consultaArray = new JSONObject(response);
                                JSONArray jsonMercado = consultaArray.getJSONArray("ofertasnotificacao");
                                OfertaNotificacaoItem ofertaItem;
                                for (int i = 0; i < jsonMercado.length(); i++) {
                                    JSONObject jsonMercadoItem =
                                            jsonMercado.getJSONObject(i);
                                    String nome_produto =
                                            jsonMercadoItem.getString("descricaofull");
                                    String nome_mercado =
                                            jsonMercadoItem.getString("nome_mercado");
                                    String preco =
                                            jsonMercadoItem.getString("preco");
                                    String validade =
                                            jsonMercadoItem.getString("validade");
                                    validade = "Val.: "+validade;

                                    String imagem =
                                            jsonMercadoItem.getString("imagem_mercado");
                                    String imagemOK = auxUrlimagem + imagem;

                                    String imagem_produto =
                                            jsonMercadoItem.getString("imagem_produto");
                                    String imagemOK2 = auxUrlimagem + imagem_produto;

                                    Log.d("img=",nome_mercado);
                                    Log.d("imgok=",imagemOK);

                                     ofertaItem = new OfertaNotificacaoItem(
                                            nome_produto,
                                            nome_mercado,
                                             validade,
                                             preco,
                                             imagemOK2,
                                            imagemOK);
                                    ofertas.add(ofertaItem);

                                }
                                mAdapter = new GridAdapterOferta(ofertas);
                                mRecyclerView.setAdapter(mAdapter);

                            }
                        } catch (Exception e) {

                        }
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

        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }

    public void MyToast(String msg){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        ImageView image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.alert);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }








    // Verifica se esta proximo a algum mercado
    public void addPontos(String id_mercado,String id_cliente, String pontos, String tipo){


        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl2 = auxUrl + "insertPontos.php?id_cliente="+id_cliente+"&id_mercado="+id_mercado+"&pontos="+pontos+"&tipo="+tipo;
        Log.d("urlpontos=",strUrl2);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl2,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};


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

        VolleyProvider.getInstance(this).addRequest(stringRequest);
    }

}
