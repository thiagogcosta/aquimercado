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

public class GridCartaoOK extends RecyclerView.Adapter<GridCartaoOK.ViewHolder>{
    List<CartaoItem> mItems;
    public Context ctx;
    public View v;
    private ImageLoader mImageLoader;

    public GridCartaoOK(List<CartaoItem> cartao) {
        super();
        mItems = new ArrayList<CartaoItem>();

        for (int i = 0; i < cartao.size(); i++){
            CartaoItem cartoes = new CartaoItem();
            cartoes.setNome(cartao.get(i).getNome());
            cartoes.setImagem(cartao.get(i).getImagem());
            cartoes.setId(cartao.get(i).getId());
            mItems.add(cartoes);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_cartao_ok, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartaoItem nature = mItems.get(position);
        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        holder.cartao_nome.setText(nature.getNome());
        holder.cartao_id.setText(nature.getId());
        holder.cartao_img.setImageUrl(nature.getImagem(), mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cartao_nome;
        public TextView cartao_id;
        NetworkImageView cartao_img;


        public ViewHolder(View itemView) {
            super(itemView);
            cartao_nome = (TextView)itemView.findViewById(R.id.cartao_nome);
            cartao_id = (TextView)itemView.findViewById(R.id.cartao_id);
            cartao_img = (NetworkImageView)itemView.findViewById(R.id.cartao_img);
        }

    }
}
