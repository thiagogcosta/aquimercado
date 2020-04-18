package com.aquimercado.aquimercado.fechamento;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;
import com.aquimercado.aquimercado.produto.ProdutoItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridFechamento  extends RecyclerView.Adapter<GridFechamento.ViewHolder> {
    int cont=0;
    public Context ctx;
    List<ProdutoItem> mItems;
    private ImageLoader mImageLoader;
    private NumberFormat moeda;

    //Construtores
    public GridFechamento(List<ProdutoItem> produto) {
        super();
        mItems = new ArrayList<ProdutoItem>();
        for (int i = 0; i < produto.size(); i++) {
            ProdutoItem prod = new ProdutoItem();
            prod.setNome(produto.get(i).getNome());
            prod.setPreco(produto.get(i).getPreco());
            prod.setDescricao(produto.get(i).getDescricao());
            prod.setThumbnail(produto.get(i).getThumbnail());
            prod.setId(produto.get(i).getId());
            prod.setpDesconto(produto.get(i).getpDesconto());
            prod.setApDesconto(produto.get(i).getApDesconto());
            prod.setQuantidade(produto.get(i).getQuantidade());
            mItems.add(prod);
        }
    }
    public GridFechamento(){
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
        ProdutoItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.idProd.setText(nature.getId());
        viewHolder.nomeProduto.setText(nature.getNome()+" ("+nature.getQuantidade()+"x)");
        viewHolder.imgProduto.setImageUrl(nature.getThumbnail(), mImageLoader);

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.precoProduto.setText(moeda.format(Double.parseDouble(nature.getPreco())));


        for (int a = 0; a < mItems.size(); a++){
            if((viewHolder.idProd.getText().toString().equalsIgnoreCase(mItems.get(a).getId())) && mItems.get(a).getApDesconto().equalsIgnoreCase("1")){
                viewHolder.precoProduto.setPaintFlags(viewHolder.precoProduto.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.RSAF.setPaintFlags(viewHolder.RSAF.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.RSOTHERAF.setText(moeda.format(Double.parseDouble(mItems.get(a).getpDesconto())));
            }
        }
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