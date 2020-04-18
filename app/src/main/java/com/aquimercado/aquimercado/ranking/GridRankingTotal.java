package com.aquimercado.aquimercado.ranking;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class GridRankingTotal extends RecyclerView.Adapter<GridRankingTotal.ViewHolder>{

    List<RankingItemNavigation> mItems;
    public Context ctx;
    public View v;
    public NumberFormat moeda;
    public String aux_id;



    public GridRankingTotal(List<RankingItemNavigation> ofertainfo, String id) {
        super();
        mItems = new ArrayList<RankingItemNavigation>();

        aux_id = id;

        for (int i = 0; i < ofertainfo.size(); i++){
            RankingItemNavigation oferta = new RankingItemNavigation();
            oferta.setPosicao(ofertainfo.get(i).getPosicao());
            oferta.setPontuacao(ofertainfo.get(i).getPontuacao());
            oferta.setNome(ofertainfo.get(i).getNome());
            oferta.setId(ofertainfo.get(i).getId());


            mItems.add(oferta);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {


        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_ranking, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RankingItemNavigation nature = mItems.get(i);

        String[] arrayValores = nature.getNome().split(" ");

        for (String s: arrayValores) {
            viewHolder.nome.setText(s);
            break;
        }

        viewHolder.id_cliente.setText(nature.getId());
        viewHolder.pontuacao_ranking.setText(nature.getPontuacao());
        viewHolder.posicao_ranking.setText(nature.getPosicao());


        if(aux_id.equalsIgnoreCase(nature.getId())){
            viewHolder.ln_ranking.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView id_cliente;
        public TextView nome;
        public TextView pontuacao_ranking;
        public TextView posicao_ranking;
        public LinearLayout ln_ranking;

        public NetworkImageView imagem_produto;

        public ViewHolder(View itemView) {
            super(itemView);
            ln_ranking = (LinearLayout) itemView.findViewById(R.id.ln_ranking);
            id_cliente = (TextView) itemView.findViewById(R.id.id_ranking);
            nome = (TextView)itemView.findViewById(R.id.nome_ranking);
            pontuacao_ranking = (TextView)itemView.findViewById(R.id.pontuacao_ranking);
            posicao_ranking = (TextView)itemView.findViewById(R.id.posicao_ranking);
        }
    }
}