package com.aquimercado.aquimercado.premiacao;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thiago on 06/01/2016.
 */
public class GridAdapterPremio extends RecyclerView.Adapter<GridAdapterPremio.ViewHolder>{

    List<PremioItem> mItems;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Context ctx;
    public TextView mId;
    public View v;

    public GridAdapterPremio(List<PremioItem> premio) {
        super();
        mItems = new ArrayList<PremioItem>();

        for (int i = 0; i < premio.size(); i++){
            PremioItem premios = new PremioItem(
                    premio.get(i).getmNome_produto(),
                    premio.get(i).getmNome_mercado(),
                    premio.get(i).getmImagemProduto(),
                    premio.get(i).getmImagemMercado(),
                    premio.get(i).getmDescricaopremio(),
                    premio.get(i).getmVencimento(),
                    premio.get(i).getmTipo()
                    );

            mItems.add(premios);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_premio, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o Prêmio");

                String tipo="";
                if (viewHolder.tipo.getText().toString().equals("0")){tipo="Tipo: Disputa no Ranking Geral";}
                if (viewHolder.tipo.getText().toString().equals("1")){tipo="Tipo: Disputa Ranking deste Supermercado";}
                if (viewHolder.tipo.getText().toString().equals("2")){tipo="Tipo: Concorre no Sorteio deste Supermercado";}
                // Setting Dialog Message
                alertDialog.setMessage(tipo+"\n\n"+viewHolder.descricapremio.getText().toString());

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
        PremioItem nature = mItems.get(i);
        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();

        viewHolder.nomeProduto.setText(nature.getmNome_produto());
        viewHolder.nomeMercado.setText(nature.getmNome_mercado());
        viewHolder.imgProduto.setImageUrl(nature.getmImagemProduto(), mImageLoader);
        viewHolder.imgMercado.setImageUrl(nature.getmImagemMercado(), mImageLoader);
        viewHolder.vencimento.setText(nature.getmVencimento());
        viewHolder.descricapremio.setText(nature.getmDescricaopremio());
        viewHolder.tipo.setText(nature.getmTipo());

        //viewHolder.txtSobre.setText(nature.getmSobreapp());

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeProduto;
        public TextView nomeMercado;
        public NetworkImageView imgProduto;
        public NetworkImageView imgMercado;
        public TextView descricapremio;
        public TextView vencimento;
        public  TextView tipo;


        //public TextView txtSobre;
        public ImageButton btInfo;


        public ViewHolder(View itemView) {
            super(itemView);
            nomeProduto = (TextView)itemView.findViewById(R.id.nomeproduto_oferta);
            nomeMercado = (TextView)itemView.findViewById(R.id.nomeMercado);
            imgProduto  = (NetworkImageView)itemView.findViewById(R.id.img_produto);
            imgMercado = (NetworkImageView)itemView.findViewById(R.id.img_mercado);
            vencimento = (TextView)itemView.findViewById(R.id.vencimento);
            tipo = (TextView)itemView.findViewById(R.id.tipopremio);
            descricapremio = (TextView)itemView.findViewById(R.id.descricaopremio);
            btInfo = (ImageButton)itemView.findViewById(R.id.btInfo);
            //txtSobre = (TextView) itemView.findViewById(R.id.txtsobre);
        }
    }


}
