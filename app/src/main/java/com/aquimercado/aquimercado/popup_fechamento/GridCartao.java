package com.aquimercado.aquimercado.popup_fechamento;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.util.ArrayList;
import java.util.List;


public class GridCartao extends RecyclerView.Adapter<GridCartao.ViewHolder>{
    public Context ctx;
    List<CartaoItem> mItems;
    private ImageLoader mImageLoader;
    public View v;

    public GridCartao(List<CartaoItem> cartao) {
        super();
        mItems = new ArrayList<CartaoItem>();

        for (int i = 0; i < cartao.size(); i++){
            CartaoItem cartoes = new CartaoItem();

            cartoes.setId(cartao.get(i).getId());

            cartoes.setImagem(cartao.get(i).getImagem());

            cartoes.setNome(cartao.get(i).getNome());

            mItems.add(cartoes);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_cartao, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);


        final int[] clique = {0};


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        CartaoItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.imgThumbnail.setImageUrl(nature.getImagem(), mImageLoader);
        viewHolder.id.setText(nature.getId());
        viewHolder.nome.setText(nature.getNome());

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgThumbnail;
        public TextView id;
        public TextView nome;



        public ViewHolder(View itemView) {
            super(itemView);

            imgThumbnail = (NetworkImageView)itemView.findViewById(R.id.img_cartao);
            id = (TextView)itemView.findViewById(R.id.txt_id);
            nome = (TextView)itemView.findViewById(R.id.txt_nome);
        }
    }
}
