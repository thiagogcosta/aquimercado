package com.aquimercado.aquimercado.produto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class GridAdapter2  extends RecyclerView.Adapter<GridAdapter2.ViewHolder> {

    List<ProdutoItem> mItems;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public Context ctx;
    public View v;
    protected int id = 0;
    int contando =0;
    private List<String> listaNome = new ArrayList<String>();
    private List<String> listaValor = new ArrayList<String>();
    private String numLista;
    private int  contadorProd;
    private float soma = 0;
    public NumberFormat moeda;

    //Shared Preferences para os produtos do fechamento
    public static final String PREFS_NAME = "produtos";
    //**************************************************


    //Construtores
    public GridAdapter2(List<ProdutoItem> produto, String n) {
        super();

        numLista = n;
        mItems = new ArrayList<ProdutoItem>();
        for (int i = 0; i < produto.size(); i++){
            ProdutoItem prod = new ProdutoItem();
            prod.setNome(produto.get(i).getNome());
            prod.setPreco(produto.get(i).getPreco());
            prod.setDescricao(produto.get(i).getDescricao());
            prod.setThumbnail(produto.get(i).getThumbnail());
            prod.setId(produto.get(i).getId());
            prod.setpDesconto(produto.get(i).getpDesconto());
            prod.setApDesconto(produto.get(i).getApDesconto());
            prod.setPsobre(produto.get(i).getPsobre());
            mItems.add(prod);
        }
    }
    public GridAdapter2(){
    }

    //Métodos
    public List<String> getListaNome(){
       return listaNome;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        ctx = viewGroup.getContext();
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.grid_produto, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        final int[] clique = {0};


        viewHolder.btInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Sobre o produto");

                // Setting Dialog Message
                alertDialog.setMessage(""+viewHolder.txtSobre.getText().toString());

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


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux = "";

                if(clique[0] %2 == 0){
                    v.findViewById(R.id.inside).setBackgroundColor(Color.parseColor("#DCEDC8"));
                    clique[0]++;
                    viewHolder.getAdapterPosition();
                    soma += Float.parseFloat(mItems.get(i).getPreco());
                    //*****************************************************************************
                    //Magia do shared prefences
                    SharedPreferences produtosPref = ctx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = produtosPref.edit();
                    String auxiliando = produtosPref.getString("prodClicado","");
                    editor.putString("prodClicado",auxiliando+"/"+ viewHolder.idProduto.getText());
                    editor.apply();

                    ProdutoItemActivity.addValores(auxiliando+"/"+ viewHolder.idProduto.getText());


                }else{

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


                    v.findViewById(R.id.inside).setBackgroundColor(Color.parseColor("#F5F5F5"));
                    clique[0]++;
                    ProdutoItemActivity.addsubValores(auxiliandoall);

                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ProdutoItem nature = mItems.get(i);

        mImageLoader = (VolleySingleton.getInstance(ctx)).getImageLoader();
        viewHolder.nomeProduto.setText(nature.getNome());
        viewHolder.descricaoProduto.setText(nature.getDescricao());
        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        viewHolder.precoProduto.setText(moeda.format(Double.parseDouble(nature.getPreco())));
        viewHolder.imgProduto.setImageUrl(nature.getThumbnail(), mImageLoader);
        viewHolder.idProduto.setText(nature.getId());
        viewHolder.txtSobre.setText(nature.getPsobre());


        //Toast.makeText(ctx, nature.getId(), Toast.LENGTH_SHORT).show();

        for (int a = 0; a < mItems.size(); a++){
            if((viewHolder.idProduto.getText().toString().equalsIgnoreCase(mItems.get(a).getId())) && mItems.get(a).getApDesconto().equalsIgnoreCase("1")){
                viewHolder.precoProduto.setPaintFlags(viewHolder.precoProduto.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

                viewHolder.RSOTHER.setText(moeda.format(Double.parseDouble(mItems.get(a).getpDesconto())));


            }
        }

        if(!viewHolder.txtSobre.getText().toString().equalsIgnoreCase("64359b7192746a14740ad4bb7afe4e097327d0790190fd16")){
            viewHolder.btInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public NetworkImageView imgProduto;
        public TextView nomeProduto;
        public TextView descricaoProduto;
        public TextView precoProduto;
        public TextView idProduto;
        public TextView txtTotal;
        //public TextView RS;
        public TextView RSOTHER;
        public TextView idCombo;
        public TextView txtSobre;
        public ImageButton btInfo;


        public ViewHolder(View itemView) {
            super(itemView);
            idProduto  = (TextView)itemView.findViewById(R.id.idProduto);
            imgProduto = (NetworkImageView)itemView.findViewById(R.id.imgProduto);
            nomeProduto = (TextView)itemView.findViewById(R.id.nomeProduto);
            descricaoProduto = (TextView)itemView.findViewById(R.id.descricaoProduto);
            precoProduto = (TextView)itemView.findViewById(R.id.precoProduto);
            //RS = (TextView)itemView.findViewById(R.id.RS);
            RSOTHER = (TextView)itemView.findViewById(R.id.RSOTHER);
            idCombo = (TextView)itemView.findViewById(R.id.idCombo);
            txtSobre = (TextView) itemView.findViewById(R.id.txtsobre);
            btInfo = (ImageButton) itemView.findViewById(R.id.btInfo);
        }
    }


}