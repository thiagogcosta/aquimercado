package com.aquimercado.aquimercado.pedidos;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridPedidoIndividual  extends RecyclerView.Adapter<GridPedidoIndividual.ViewHolder> {
    int cont=0;
    public Context ctx;
    List<PedidoItem> mItems;
    private ImageLoader mImageLoader;
    public NumberFormat moeda;

    //Construtores
    public GridPedidoIndividual(List<PedidoItem> pedido) {
        super();
        mItems = new ArrayList<PedidoItem>();
        for (int i = 0; i < pedido.size(); i++) {
            PedidoItem ped = new PedidoItem();
            ped.setNome(pedido.get(i).getNome());
            ped.setValor(pedido.get(i).getValor());
            ped.setImagem(pedido.get(i).getImagem());
            ped.setQuantidade(pedido.get(i).getQuantidade());
            mItems.add(ped);
        }
    }
    public GridPedidoIndividual(){
    }

    //Métodos

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_fechamento, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        cont++;
        if(cont%2==0) {
            v.findViewById(R.id.relative_fech).setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PedidoItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nomeProduto.setText(nature.getNome()+" ("+nature.getQuantidade()+"x)");
        viewHolder.imgProduto.setImageUrl(nature.getImagem(), mImageLoader);

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.precoProduto.setText(moeda.format(Double.parseDouble(nature.getValor())));

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgProduto;
        public TextView idProd;
        public TextView nomeProduto;
        public TextView precoProduto;
        public TextView RSAF;
        public TextView RSOTHERAF;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduto = (NetworkImageView)itemView.findViewById(R.id.produto_foto);
            idProd = (TextView)itemView.findViewById(R.id.idProd);
            nomeProduto = (TextView)itemView.findViewById(R.id.produto_nome);
            precoProduto = (TextView)itemView.findViewById(R.id.produto_preco);
            RSAF = (TextView) itemView.findViewById(R.id.RSAF);
            RSOTHERAF = (TextView) itemView.findViewById(R.id.RSotherAF);
        }
    }
}