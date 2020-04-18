package com.aquimercado.aquimercado.pedidos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GridPedido  extends RecyclerView.Adapter<GridPedido.ViewHolder> {
    int cont=0;
    public Context ctx;
    List<Pedido> mItems;
    private ImageLoader mImageLoader;
    public View v;
    private NumberFormat moeda;


    //Construtores
    public GridPedido(List<Pedido> pedido) {
        super();
        mItems = new ArrayList<Pedido>();
        for (int i = 0; i < pedido.size(); i++) {
            Pedido ped = new Pedido();
            ped.setIcone(pedido.get(i).getIcone());
            ped.setStatus(pedido.get(i).getStatus().toUpperCase());
            ped.setCodigo(pedido.get(i).getCodigo().toUpperCase());
            ped.setValor(pedido.get(i).getValor());
            ped.setId(pedido.get(i).getId());
            ped.setTipo_pedido(pedido.get(i).getTipo_pedido());

            // Split no -

            String[] data = pedido.get(i).getDataPedido().split("-");
            String[] dataaux = data[2].split(" ");
            ped.setDataPedido(dataaux[0]+"/"+data[1]+"/"+data[0]);

            mItems.add(ped);
        }
    }
    public GridPedido(){
    }

    //Métodos

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_pedido, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Pedido nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.dataPedido.setText(nature.getDataPedido());
        //viewHolder.valor.setText(nature.getValor());
        viewHolder.codigo.setText(nature.getCodigo());
        viewHolder.status.setText(nature.getStatus());
        viewHolder.id.setText(nature.getId());
        viewHolder.tipo.setText(nature.getTipo_pedido());
        viewHolder.icone.setImageUrl(nature.getIcone(), mImageLoader);

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.valor.setText(moeda.format(Double.parseDouble(nature.getValor())));

        viewHolder.ln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PedidoIndividualNavigation.class);
                intent.putExtra("id", viewHolder.id.getText().toString());
                intent.putExtra("status", viewHolder.status.getText().toString());
                intent.putExtra("tipo", viewHolder.tipo.getText().toString());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView icone;
        public TextView dataPedido;
        public TextView valor;
        public TextView codigo;
        public TextView status;
        public TextView id;
        public TextView tipo;
        public LinearLayout ln;


        public ViewHolder(View itemView) {
            super(itemView);
            icone = (NetworkImageView)itemView.findViewById(R.id.mercado_icone);
            dataPedido = (TextView)itemView.findViewById(R.id.txt_data);
            valor = (TextView)itemView.findViewById(R.id.txt_valor);
            codigo = (TextView)itemView.findViewById(R.id.txt_codigo);
            status = (TextView) itemView.findViewById(R.id.txt_status);
            id = (TextView) itemView.findViewById(R.id.txt_id);
            tipo = (TextView) itemView.findViewById(R.id.txt_tipo);
            ln = (LinearLayout) itemView.findViewById(R.id.linear_fetch);
        }
    }
}