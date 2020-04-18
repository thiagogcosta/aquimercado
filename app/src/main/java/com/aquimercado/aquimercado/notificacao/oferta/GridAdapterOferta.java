package com.aquimercado.aquimercado.notificacao.oferta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class GridAdapterOferta extends RecyclerView.Adapter<GridAdapterOferta.ViewHolder>{

    List<OfertaNotificacaoItem> mItems;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Context ctx;
    public TextView mId;
    public View v;

    public GridAdapterOferta(List<OfertaNotificacaoItem> oferta) {
        super();
        mItems = new ArrayList<OfertaNotificacaoItem>();

        for (int i = 0; i < oferta.size(); i++){
            OfertaNotificacaoItem ofertas = new OfertaNotificacaoItem(
                    oferta.get(i).getmNome_produto(),
                    oferta.get(i).getmNome_mercado(),
                    oferta.get(i).getmValidade(),
                    oferta.get(i).getmPreco(),
                    oferta.get(i).getmImagemProduto(),
                    oferta.get(i).getmImagemMercado()
                    );

            mItems.add(ofertas);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_oferta, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        OfertaNotificacaoItem nature = mItems.get(i);
        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();

        viewHolder.nomeProduto.setText(nature.getmNome_produto());
        viewHolder.nomeMercado.setText(nature.getmNome_mercado());
        viewHolder.validade.setText(nature.getmValidade());
        viewHolder.preco.setText(nature.getmPreco());
        viewHolder.imgProduto.setImageUrl(nature.getmImagemProduto(), mImageLoader);
        viewHolder.imgMercado.setImageUrl(nature.getmImagemMercado(), mImageLoader);

        //viewHolder.txtSobre.setText(nature.getmSobreapp());

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nomeProduto;
        public TextView nomeMercado;
        public TextView validade;
        public TextView preco;
        public NetworkImageView imgProduto;
        public NetworkImageView imgMercado;

        //public TextView txtSobre;
        //public ImageButton btInfo;


        public ViewHolder(View itemView) {
            super(itemView);
            nomeProduto = (TextView)itemView.findViewById(R.id.nomeproduto_oferta);
            nomeMercado = (TextView)itemView.findViewById(R.id.nomeMercado);
            validade = (TextView)itemView.findViewById(R.id.validade_oferta);
            preco = (TextView)itemView.findViewById(R.id.preco_oferta);
            imgProduto  = (NetworkImageView)itemView.findViewById(R.id.img_produto);
            imgMercado = (NetworkImageView)itemView.findViewById(R.id.img_mercado);
            //btInfo = (ImageButton)itemView.findViewById(R.id.btInfo);
            //txtSobre = (TextView) itemView.findViewById(R.id.txtsobre);
        }
    }


}
