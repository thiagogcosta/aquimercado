package com.aquimercado.aquimercado.mercado;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.VolleySingleton;
import com.aquimercado.aquimercado.oferta.OfertaItemNavigation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 06/01/2016.
 */
public class GridAdapter3 extends RecyclerView.Adapter<GridAdapter3.ViewHolder>{

    List<MercadoItem> mItems;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Context ctx;
    public TextView mId;
    public View v;
    ProgressBar progressBar;

    public GridAdapter3(List<MercadoItem> mercado, ProgressBar pbar) {
        super();
        mItems = new ArrayList<MercadoItem>();
        progressBar = pbar;
        for (int i = 0; i < mercado.size(); i++){
            MercadoItem mercados = new MercadoItem();

            mercados.setmName(mercado.get(i).getmName());

            mercados.setmThumbnail(mercado.get(i).getmThumbnail());

            mercados.setmId(mercado.get(i).getmId());

            mercados.setmDistancia(mercado.get(i).getmDistancia());


            //mercados.setmContagem(mercado.get(i).getmContagem());
            mercados.setmSobreapp(mercado.get(i).getmSobreapp());
            mercados.setmPontos(mercado.get(i).getmPontos());

            mItems.add(mercados);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_mercado, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);



        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validaVendas(viewHolder.mId.getText().toString(),viewHolder.imagemMercado.getText().toString(), viewHolder.nomeMercado.getText().toString());
            }
        });


        viewHolder.btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o estabelecimento");

                // Setting Dialog Message
                alertDialog.setMessage(""+viewHolder.txtSobre.getText().toString());

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.btinfo);

                // Setting Positive "Yes" Button
                alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MercadoItem nature = mItems.get(i);
        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nmDistancia.setText("  "+nature.getmDistancia());
        viewHolder.imgThumbnail.setImageUrl(nature.getmThumbnail(), mImageLoader);
        //viewHolder.mContagem.setText(nature.getmContagem());
        viewHolder.nomeMercado.setText(nature.getmName());
        viewHolder.imagemMercado.setText(nature.getmThumbnail());
        viewHolder.mId.setText(nature.getmId());
        viewHolder.txtSobre.setText(nature.getmSobreapp());

        if(!viewHolder.txtSobre.getText().toString().equalsIgnoreCase("64359b7192746a14740ad4bb7afe4e097327d0790190fd16")){
            viewHolder.btInfo.setVisibility(View.VISIBLE);
        }
        viewHolder.txtPontos.setText(nature.getmPontos());


    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgThumbnail;
        public TextView nmDistancia;
        public TextView mId;
        public TextView nomeMercado;
        public TextView imagemMercado;
        public TextView txtSobre,txtPontos;
        public ImageButton btInfo;


        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (NetworkImageView)itemView.findViewById(R.id.img_mercado);
            nmDistancia = (TextView)itemView.findViewById(R.id.distancia);
            //mContagem = (TextView)itemView.findViewById(R.id.contagem);
            mId = (TextView)itemView.findViewById(R.id.idmercado);
            nomeMercado = (TextView)itemView.findViewById(R.id.nomeMercado);
            imagemMercado = (TextView)itemView.findViewById(R.id.imagemMercado);
            btInfo = (ImageButton)itemView.findViewById(R.id.btInfo);
            txtSobre = (TextView) itemView.findViewById(R.id.txtsobre);
            txtPontos = (TextView)  itemView.findViewById(R.id.pontos_cliente);
        }
    }

    public void  configMercado(final String id, final String logo, final String nome) {

        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences urlPref = ctx.getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "configMercadoDepto.php?id_mercado=" +id;

        Log.d("urldesptp=",strUrl);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
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
                                if (!consultaArray.isNull("config")) {



                                    JSONObject infoConfig;

                                    JSONArray arrayPedidos = consultaArray.getJSONArray("config");

                                    infoConfig = (JSONObject) arrayPedidos.get(0);

                                    String permitedepto = infoConfig.getString("permitedep");
                                    String temponotifica = infoConfig.getString("temponotifica");

                                    //*****************************Tempo da notificacao shared*******************
                                    SharedPreferences notificaPref = ctx.getSharedPreferences("Notificacao", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = notificaPref.edit();
                                    editor.putString("notifica", temponotifica);
                                    editor.apply();



                                    Intent intent = new Intent(ctx, OfertaItemNavigation.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("logo_mercado", logo);
                                    //intent.putExtra("latitude", latitude);
                                    //intent.putExtra("longitude", longitude);
                                    intent.putExtra("nome_mercado", nome);
                                    intent.putExtra("tipo","ambos");
                                    intent.putExtra("permitedepto",""+permitedepto);
                                    ctx.startActivity(intent);


                                }else{
                                    //*****************************Tempo da notificacao shared*******************
                                    SharedPreferences notificaPref = ctx.getSharedPreferences("Notificacao", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = notificaPref.edit();
                                    editor.putString("notifica", "5"); // cinco minutos
                                    editor.apply();

                                    Intent intent = new Intent(ctx, OfertaItemNavigation.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("logo_mercado", logo);
                                    intent.putExtra("nome_mercado", nome);
                                    intent.putExtra("tipo","ambos");
                                    intent.putExtra("permitedepto","0");
                                    ctx.startActivity(intent);
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

        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(ctx).addRequest(stringRequest);


    }

    public void  validaVendas(final String id, final String logo, final String nome) {

        SharedPreferences urlPref = ctx.getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String strUrl = auxUrl + "validaVendas.php?id_mercado=" +id;

        Log.d("urlvendas=",strUrl);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                if (!consultaArray.isNull("vendas")) {



                                    JSONObject infoConfig;

                                    JSONArray arrayPedidos = consultaArray.getJSONArray("vendas");

                                    infoConfig = (JSONObject) arrayPedidos.get(0);

                                    String permitedep = infoConfig.getString("permitedep");
                                    String permitecombo = infoConfig.getString("permitecombo");
                                    String permiteoferta = infoConfig.getString("permiteoferta");

                                    //*****************************Permite depto,combo e oferta*******************
                                    SharedPreferences notificaPref = ctx.getSharedPreferences("Permite", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = notificaPref.edit();
                                    editor.putString("permitedep", permitedep);
                                    editor.putString("permitecombo", permitecombo);
                                    editor.putString("permiteoferta", permiteoferta);
                                    editor.apply();

                                    configMercado(id,logo,nome);


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

        int timeout = 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(ctx).addRequest(stringRequest);


    }
}
