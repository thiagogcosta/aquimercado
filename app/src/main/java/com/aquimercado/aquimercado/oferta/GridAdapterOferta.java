package com.aquimercado.aquimercado.oferta;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class GridAdapterOferta extends RecyclerView.Adapter<GridAdapterOferta.ViewHolder>{

    List<OfertaItem> mItems;
    private ImageLoader mImageLoader;
    public Context ctx;
    public View v;
    public static final String PREFS_NAME = "produtos";
    public NumberFormat moeda;



    public GridAdapterOferta(List<OfertaItem> ofertainfo) {
        super();
        mItems = new ArrayList<OfertaItem>();


        for (int i = 0; i < ofertainfo.size(); i++){
            OfertaItem oferta = new OfertaItem();
            Log.d("nomePeoduto=",ofertainfo.get(i).getNomeProduto());
            oferta.setIdProduto(ofertainfo.get(i).getidProduto());
            oferta.setidMercado(ofertainfo.get(i).getidMercado());
            oferta.setNomeProduto(ofertainfo.get(i).getNomeProduto());
            oferta.setDepartamento(ofertainfo.get(i).getDepartamento());
            oferta.setPrecoProduto(ofertainfo.get(i).getPrecoProduto());
            oferta.setImagemProduto(ofertainfo.get(i).getImagemProduto());
            oferta.setMarca(ofertainfo.get(i).getMarca());
            oferta.setDescricao(ofertainfo.get(i).getDescricao());

            mItems.add(oferta);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {


        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_item_oferta, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o produto");

                // Setting Dialog Message
                alertDialog.setMessage(""+viewHolder.descricao.getText().toString());

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
        OfertaItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nome_produto.setText(nature.getNomeProduto());
        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.preco_produto.setText(moeda.format(Double.parseDouble(nature.getPrecoProduto())));
        viewHolder.imagem_produto.setImageUrl(nature.getImagemProduto(), mImageLoader);
        viewHolder.idProduto.setText(nature.getidProduto());
        viewHolder.marca.setText(nature.getMarca());
        viewHolder.descricao.setText(nature.getDescricao());

        if(!viewHolder.descricao.getText().toString().equalsIgnoreCase("")){
            viewHolder.btInfo.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView idProduto;
        public TextView nome_produto;
        public TextView preco_produto;
        public TextView marca;
        public TextView descricao;
        public ImageButton btInfo;

        public NetworkImageView imagem_produto;

        public ViewHolder(View itemView) {
            super(itemView);
            imagem_produto = (NetworkImageView)itemView.findViewById(R.id.image_produto);
            nome_produto = (TextView)itemView.findViewById(R.id.nome_produto);
            idProduto = (TextView)itemView.findViewById(R.id.idProdutoDepto);
            preco_produto = (TextView)itemView.findViewById(R.id.preco_produto);
            marca = (TextView)itemView.findViewById(R.id.marca);
            descricao = (TextView)itemView.findViewById(R.id.descricao);
            btInfo = (ImageButton) itemView.findViewById(R.id.btInfo);

        }
    }
}