package com.aquimercado.aquimercado.combo;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;
import com.aquimercado.aquimercado.produto.ProdutoItemActivity;

import java.util.ArrayList;
import java.util.List;


public class GridAdapter  extends RecyclerView.Adapter<GridAdapter.ViewHolder>{

    List<ComboItem> mItems;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Context ctx;
    public Application application;
    public View v;



    public GridAdapter(List<ComboItem> combo) {
        super();
        mItems = new ArrayList<ComboItem>();



        for (int i = 0; i < combo.size(); i++){
            ComboItem combo2 = new ComboItem();
            combo2.setIdCombo(combo.get(i).getIdCombo());
            combo2.setName(combo.get(i).getName());
            combo2.setNomeCompleto(combo.get(i).getNomeCompleto());
            combo2.setThumbnail(combo.get(i).getThumbnail());
            combo2.setValidade(combo.get(i).getValidade());
            combo2.setIdMercado(combo.get(i).getIdMercado());
            combo2.setEntregaretirada(combo.get(i).getEntregaretirada());
            combo2.setCod_horaprevista(combo.get(i).getCod_horaprevista());
            combo2.setTemFrete(combo.get(i).getTemFrete());
            combo2.setValor_isento(combo.get(i).getValor_isento());
            combo2.setConfig(combo.get(i).getConfig());
            combo2.setSobre(combo.get(i).getSobre());
            mItems.add(combo2);

        }

    }

   public void setApp(Application app){
       application =app;
   }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {


        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        final ComboItem cb = mItems.get(i);
        viewHolder.idCombo.setText(cb.getIdCombo());
        viewHolder.nmComboCompleto.setText(cb.getNomeCompleto());
        //viewHolder.btInfo.bringToFront();

        viewHolder.btImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o mercado");

                // Setting Dialog Message
                alertDialog.setMessage(""+viewHolder.sobre.getText().toString());

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

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*********************GUARDANDO O ID DO COMBO NO SHARED*************************************
                SharedPreferences produtosPref = ctx.getSharedPreferences("Combo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = produtosPref.edit();
                editor.putString("id", "" + viewHolder.idCombo.getText().toString());
                editor.apply();
                //*******************************************************************************************


                //****************************GUARDO O COD_HORAPREVISTA**************************************
                SharedPreferences cod_hp = ctx.getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_cod_hp = cod_hp.edit();
                editor_cod_hp.putString("hora_prevista", "" + viewHolder.cod_horaprevista.getText().toString());
                editor_cod_hp.apply();

                //*******************************************************************************************

                //****************************GUARDO O ENTREGARETIRADA**************************************
                SharedPreferences cod_er = ctx.getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_cod_er = cod_er.edit();
                editor_cod_er.putString("entregaretirada", "" + viewHolder.entregaRetirada.getText().toString());
                editor_cod_er.apply();

                //*******************************************************************************************

                //****************************GUARDO O TEM_FRETE**************************************
                SharedPreferences cod_temFrete = ctx.getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_cod_temFrete = cod_temFrete.edit();
                editor_cod_temFrete.putString("tem_frete", "" + viewHolder.tem_frete.getText().toString());
                editor_cod_temFrete.apply();

                //*******************************************************************************************

                //****************************GUARDO O VALOR_ISENTO**************************************
                SharedPreferences cod_valor_isento = ctx.getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_valor_isento = cod_valor_isento.edit();
                editor_valor_isento.putString("valor_isento", "" + viewHolder.valor_isento.getText().toString());
                editor_valor_isento.apply();

                //*******************************************************************************************

                SharedPreferences dist = ctx.getApplicationContext().getSharedPreferences("regiao", Context.MODE_PRIVATE);
                String alcance = new String(dist.getString("status", ""));

                SharedPreferences mercado = ctx.getApplicationContext().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
                String nome_mercado = new String(mercado.getString("nome_mercado", ""));

                if (alcance.equals("0")) {
                    Toast.makeText(v.getContext(), "Você está fora da área de atendimento do estabelecimento " + nome_mercado, Toast.LENGTH_LONG).show();

                } else {

                    Intent intent = new Intent(v.getContext(), ProdutoItemActivity.class);
                    intent.putExtra("id", viewHolder.idCombo.getText().toString());
                    intent.putExtra("nome", viewHolder.nmComboCompleto.getText().toString());
                    intent.putExtra("config", viewHolder.config.getText().toString());
                ctx.startActivity(intent);
                }
            }
        });


        viewHolder.btImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o combo");

                // Setting Dialog Message
                alertDialog.setMessage(""+viewHolder.sobre.getText().toString());

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
        ComboItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nmCombo.setText(nature.getName());
        viewHolder.validadeCombo.setText(nature.getValidade());
        viewHolder.imgThumbnail.setImageUrl(nature.getThumbnail(), mImageLoader);
        viewHolder.idCombo.setText(nature.getIdCombo());
        viewHolder.nmComboCompleto.setText(nature.getNomeCompleto());
        viewHolder.entregaRetirada.setText(nature.getEntregaretirada());
        viewHolder.cod_horaprevista.setText(nature.getCod_horaprevista());
        viewHolder.tem_frete.setText(nature.getTemFrete());
        viewHolder.valor_isento.setText(nature.getValor_isento());
        viewHolder.config.setText(nature.getConfig());
        viewHolder.sobre.setText(nature.getSobre());

        if(!viewHolder.sobre.getText().toString().equalsIgnoreCase("64359b7192746a14740ad4bb7afe4e097327d0790190fd16")){
            viewHolder.btImage.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgThumbnail;
        public TextView nmCombo;
        public TextView validadeCombo;
        public TextView idCombo;
        public TextView nmComboCompleto;
        public TextView entregaRetirada;
        public TextView cod_horaprevista;
        public TextView tem_frete;
        public TextView valor_isento;
        public TextView config;
        public TextView sobre;
        public ImageButton btImage;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (NetworkImageView)itemView.findViewById(R.id.img_thumbnail);
            nmCombo = (TextView)itemView.findViewById(R.id.nome_combo);
            validadeCombo = (TextView)itemView.findViewById(R.id.validade_combo);
            idCombo = (TextView)itemView.findViewById(R.id.idCombo);
            nmComboCompleto = (TextView)itemView.findViewById(R.id.nomeCompleto);
            entregaRetirada = (TextView)itemView.findViewById(R.id.entregaRetirada);
            cod_horaprevista = (TextView)itemView.findViewById(R.id.cod_horaprevista);
            tem_frete = (TextView)itemView.findViewById(R.id.tem_frete);
            valor_isento = (TextView)itemView.findViewById(R.id.valor_isento);
            config = (TextView)itemView.findViewById(R.id.config);
            sobre = (TextView) itemView.findViewById(R.id.txtsobre);
            btImage = (ImageButton) itemView.findViewById(R.id.btInfo);
        }
    }
}