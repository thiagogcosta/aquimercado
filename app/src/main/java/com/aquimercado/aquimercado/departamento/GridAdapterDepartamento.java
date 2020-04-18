package com.aquimercado.aquimercado.departamento;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleySingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class GridAdapterDepartamento extends RecyclerView.Adapter<GridAdapterDepartamento.ViewHolder>{

    List<DepartamentoItem> mItems;
    private ImageLoader mImageLoader;
    public Context ctx;
    public View v;
    public static final String PREFS_NAME = "produtos";
    public NumberFormat moeda;



    public GridAdapterDepartamento(List<DepartamentoItem> deptoinfo) {
        super();
        mItems = new ArrayList<DepartamentoItem>();


        for (int i = 0; i < deptoinfo.size(); i++){
            DepartamentoItem depto = new DepartamentoItem();
            depto.setIdProduto(deptoinfo.get(i).getidProduto());
            depto.setidMercado(deptoinfo.get(i).getidMercado());
            depto.setNomeProduto(deptoinfo.get(i).getNomeProduto());
            depto.setDepartamento(deptoinfo.get(i).getDepartamento());
            depto.setPrecoProduto(deptoinfo.get(i).getPrecoProduto());
            depto.setImagemProduto(deptoinfo.get(i).getImagemProduto());
            depto.setDesconto(deptoinfo.get(i).getDesconto());
            depto.setApDesconto(deptoinfo.get(i).getApDesconto());
            depto.setMarca(deptoinfo.get(i).getMarca());
            depto.setDescricao(deptoinfo.get(i).getDescricao());

            mItems.add(depto);

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {


        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_item_depto, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        final int[] clique = {0};

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

        viewHolder.btplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  qtde = Integer.parseInt(viewHolder.qdteproduto.getText().toString());
                qtde++;
                viewHolder.qdteproduto.setText(qtde+"");
                DepartamentoItemNavigation.setQtdeProduto(viewHolder.idProduto.getText().toString(), qtde+"");
            }
        });

        viewHolder.btless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int  qtde = Integer.parseInt(viewHolder.qdteproduto.getText().toString());
                if (qtde>1) {
                    qtde--;
                    viewHolder.qdteproduto.setText(qtde+"");
                    DepartamentoItemNavigation.setQtdeProduto(viewHolder.idProduto.getText().toString(), qtde+"");

                }
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String aux = "";

                SharedPreferences dist = ctx.getApplicationContext().getSharedPreferences("regiao", Context.MODE_PRIVATE);
                String alcance = new String(dist.getString("status", ""));

                if (alcance.equals("0")) {
                    Toast.makeText(v.getContext(), "Você está fora da área de atendimento do estabelecimento", Toast.LENGTH_LONG).show();

                } else {

                    /*Intent intent = new Intent(v.getContext(), ProdutoItemActivity.class);
                    intent.putExtra("id", viewHolder.idCombo.getText().toString());
                    intent.putExtra("nome", viewHolder.nmComboCompleto.getText().toString());
                    intent.putExtra("config", viewHolder.config.getText().toString());
                ctx.startActivity(intent);*/
                }



                if(clique[0] %2 == 0){
                    v.findViewById(R.id.insidedepto).setBackgroundColor(Color.parseColor("#DCEDC8"));
                    viewHolder.btless.setVisibility(View.VISIBLE);
                    viewHolder.btplus.setVisibility(View.VISIBLE);
                    viewHolder.qdteproduto.setVisibility(View.VISIBLE);
                    clique[0]++;
                    //Magia do shared prefences
                    SharedPreferences produtosPref = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = produtosPref.edit();
                    String auxiliando = produtosPref.getString("prodClicado","");
                    editor.putString("prodClicado",auxiliando+"/"+ viewHolder.idProduto.getText());
                    editor.apply();

                    //Log.d("viewholder",auxiliando+"/"+ viewHolder.preco_produto.getText());
                    DepartamentoItemNavigation.addValores(auxiliando+"/"+ viewHolder.idProduto.getText());

                    } else
                    {
                        viewHolder.btless.setVisibility(View.INVISIBLE);
                        viewHolder.btplus.setVisibility(View.INVISIBLE);
                        viewHolder.qdteproduto.setVisibility(View.INVISIBLE);

                        //****************************Tirando do shared**************************************
                        SharedPreferences produtosPref = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = produtosPref.edit();
                        String auxiliando = produtosPref.getString("prodClicado", "");

                        String produtosClicados[] = auxiliando.split(Pattern.quote("/"));

                        for(int i=0; i<produtosClicados.length; i++){
                            if(produtosClicados[i].equalsIgnoreCase(viewHolder.idProduto.getText().toString())){
                                aux = produtosClicados[i];
                                break;
                            }
                        }

                        String auxiliandoall = auxiliando.replace(aux, "");

                        editor.putString("prodClicado", auxiliandoall);
                        editor.apply();


                        v.findViewById(R.id.insidedepto).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        clique[0]++;
                        DepartamentoItemNavigation.addsubValores(auxiliandoall);



                    }


            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DepartamentoItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nome_produto.setText(nature.getNomeProduto());
        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.preco_produto.setText(moeda.format(Double.parseDouble(nature.getPrecoProduto())));
        viewHolder.imagem_produto.setImageUrl(nature.getImagemProduto(), mImageLoader);
       // viewHolder.preco_produto.setText(nature.getPrecoProduto());
        viewHolder.idProduto.setText(nature.getidProduto());
        viewHolder.marca.setText(nature.getMarca());
        viewHolder.desconto.setText(nature.getDesconto());
        viewHolder.apdesconto.setText(nature.getApDesconto());
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
        public TextView desconto;
        public TextView apdesconto;
        public ImageButton btInfo;
        public ImageButton btless;
        public  ImageButton btplus;
        public TextView qdteproduto;

        public NetworkImageView imagem_produto;

        public ViewHolder(View itemView) {
            super(itemView);
            imagem_produto = (NetworkImageView)itemView.findViewById(R.id.image_produto);
            nome_produto = (TextView)itemView.findViewById(R.id.nome_produto);
            idProduto = (TextView)itemView.findViewById(R.id.idProdutoDepto);
            preco_produto = (TextView)itemView.findViewById(R.id.preco_produto);
            marca = (TextView)itemView.findViewById(R.id.marca);
            desconto = (TextView)itemView.findViewById(R.id.desconto);
            apdesconto = (TextView)itemView.findViewById(R.id.apdesconto);
            descricao = (TextView)itemView.findViewById(R.id.descricao);
            btInfo = (ImageButton) itemView.findViewById(R.id.btInfo);
            btplus = (ImageButton) itemView.findViewById(R.id.btPlus);
            btless = (ImageButton) itemView.findViewById(R.id.btLess);
            qdteproduto = (TextView) itemView.findViewById(R.id.qtdeproduto);


        }
    }
}