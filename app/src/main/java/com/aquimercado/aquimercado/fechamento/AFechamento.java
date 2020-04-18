package com.aquimercado.aquimercado.fechamento;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SQLite.DBHelper;
import com.aquimercado.aquimercado.VolleyProvider;
import com.aquimercado.aquimercado.popup_fechamento.FNavigation_cartaoCERTO;
import com.aquimercado.aquimercado.popup_fechamento.FNavigation_dinheiroCERTO;
import com.aquimercado.aquimercado.produto.ProdutoItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class AFechamento extends AppCompatActivity {

    private String produtosArray[];
    private int iTipo=-1;
    public static final String PREFS_NAME = "produtos";
    int tamanho;
    int refresh = 0;
    DBHelper db;
    List<ProdutoItem> ListaPD = new ArrayList<ProdutoItem>();
    private PopupWindow pwindo;
    Button btnConfirm;
    Button btnVoltar;
    EditText edtInfo;
    TextView txtInfo;
    TextView text_frete;
    ImageView imgVisib;
    RadioGroup radioMpag;
    private RadioButton radioBMPAG;
    RadioGroup radioPag;
    RadioButton radioEntrega, radioRetirada,radioCartao,radioDinheiro, radioCheque;
    Point p;
    String aux = "";
    public int ret_indip = 0;
    public int ent_indip = 0;
    public String id_mercado = "";
    public int statusEntrega;
    public int statusRetirada;

    public int statusHoraR;
    public String horaestimadaR;
    public String diaestimadoR;

    public int statusHoraE;
    public String horaestimadaE;
    public String diaestimadoE;
    public int tipoER=-1;
    public int tipoDC=-1;

    public int meioDinheiro=-1;
    public int meioCartao=-1;
    public int meioCheque=-1;

    public NumberFormat moeda;
    private Float valorFrete;
    private int isCheque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_afechamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));
        setSupportActionBar(toolbar);


        //*************************************PARTE DOS SHARED PREFERENCES************************************

        SharedPreferences sharedAuxHora = getApplication().getSharedPreferences("AUX_COMPRA", MODE_PRIVATE);
        String auxsharedAuxHora = sharedAuxHora.getString("hora_prevista", "");

        SharedPreferences sharedAuxEntrega = getApplication().getSharedPreferences("AUX_COMPRA", MODE_PRIVATE);
        final String auxsharedAuxEntrega = sharedAuxEntrega.getString("entregaretirada", "");

        SharedPreferences sharedAuxFrete = getApplication().getSharedPreferences("AUX_COMPRA", MODE_PRIVATE);
        String auxsharedAuxFrete = sharedAuxFrete.getString("tem_frete", "");

        SharedPreferences sharedAuxValor = getApplication().getSharedPreferences("AUX_COMPRA", MODE_PRIVATE);
        String auxsharedAuxValor = sharedAuxValor.getString("valor_isento", "");

        SharedPreferences mercados = getApplication().getSharedPreferences("Mercado", Context.MODE_PRIVATE);
        final String auxMercado = mercados.getString("id", "");
        id_mercado = auxMercado;



        //****************************************************************************************************
        statusHoraR=-1;
        horaestimadaR="";
        diaestimadoR="";

        statusHoraE=-1;
        horaestimadaE="";
        diaestimadoE="";

        //********************************************VALIDAÇÕES**********************************************
        validaMeios();
        validaEntrega();
        validaRetirada();
        validaHorario(1);
        validaHorario(2);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        //Initialize the Point with x, and y positions

        //Shared Preferences do prodClicado
        SharedPreferences produtosPref = getApplication().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = produtosPref.edit();
        String auxiliando = produtosPref.getString("prodClicado", "");


        String produtosClicados[] = auxiliando.split(Pattern.quote("/"));

        db = new DBHelper(this);

        for (ProdutoItem pd :  db.getAllProd()) {
            for (String c :  produtosClicados) {
                if(pd.getId().equalsIgnoreCase(c)){
                    //Log.d("bateu->",pd.getNome());
                    ListaPD.add(pd);
                    break;
                }
            }
        }

        db.close();
        //Limpar o shared!
        //editor.clear().apply();


        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_fechamento);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

        GridFechamento mAdapter = new GridFechamento(ListaPD);
        rv.setAdapter(mAdapter);


        float soma=0;
        float acu = 0;
        float eq = 0;

        for (ProdutoItem pd : ListaPD) {

            soma += Float.parseFloat(pd.getPreco())* Integer.parseInt(pd.getQuantidade());
            //soma += Float.parseFloat(pd.getPrecoProduto()) * Integer.parseInt(pd.getQuantidade());

            if(pd.getApDesconto().equalsIgnoreCase("1")){
                acu += (Float.parseFloat(pd.getPreco()) - Float.parseFloat(pd.getpDesconto()))*Integer.parseInt(pd.getQuantidade());
                aux += "["+pd.getId()+"/"+pd.getpDesconto()+"/"+pd.getQuantidade()+"]";
            }
            else{
                aux += "["+pd.getId()+"/"+pd.getPreco()+"/"+pd.getQuantidade()+"]";
            }

        }


        TextView tv = (TextView) findViewById(R.id.txtFooter2);
        TextView tvDesc = (TextView) findViewById(R.id.txtDescAF);
        final TextView tvTotalDesc = (TextView) findViewById(R.id.txtDescTotAF);
        final TextView text_frete = (TextView) findViewById(R.id.text_frete);
        final TextView text_freteVALOR = (TextView) findViewById(R.id.text_freteVALOR);

        text_freteVALOR.setText("");

        eq = soma-acu;

        moeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        tv.setText(moeda.format(Double.parseDouble(""+soma)));
        tvDesc.setText(moeda.format(Double.parseDouble(""+acu)));

        if(!tvDesc.getText().toString().equalsIgnoreCase("0,00")){
            tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        tvTotalDesc.setText(moeda.format(Double.parseDouble(""+eq)));



        //RADIO BUTTOM


        radioMpag = (RadioGroup) findViewById(R.id.radioMPag);
        radioPag = (RadioGroup) findViewById(R.id.radioPag);
        radioEntrega = (RadioButton) findViewById(R.id.radioEntreg);
        radioRetirada = (RadioButton) findViewById(R.id.radioRet);
        radioCartao = (RadioButton) findViewById(R.id.radioCart);
        radioDinheiro = (RadioButton) findViewById(R.id.radioDinheiro);
        radioCheque = (RadioButton) findViewById(R.id.radioCheque);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((tipoER==-1) && (tipoDC==-1)){
                    Toast.makeText(getApplicationContext(), "Selecione Entrega/Retirada e o Meio de Pagamento", Toast.LENGTH_SHORT).show();
                } else if (tipoER==-1){
                    Toast.makeText(getApplicationContext(), "Selecione Entrega/Retirada", Toast.LENGTH_SHORT).show();

                }else if (tipoDC==-1){
                    Toast.makeText(getApplicationContext(), "Selecione o Meio de Pagamento", Toast.LENGTH_SHORT).show();
                }

            }
        });

        radioCartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoDC = 1;isCheque=0;
            }
        });
        radioDinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoDC = 1;isCheque=0;
            }
        });
        radioCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoDC = 1; isCheque=1;
            }
        });

        final Float valorIsento = Float.parseFloat(auxsharedAuxValor);
        valorFrete = Float.parseFloat(auxsharedAuxFrete);


        //Esquema de visibilidade do RadioButton

        final float finalEq = eq;



        radioEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ent_indip = 0;
                ret_indip = 0;
                tipoER=1;
                switch(auxsharedAuxEntrega) {
                    case "0":
                            text_frete.setText("");
                        break;
                    case "2":
                            text_frete.setText("Entrega Indisponível");
                            text_frete.setTextColor(Color.parseColor("#F5DC49"));
                            ent_indip = 1;
                        break;
                    case "1":
                        if (valorIsento >= finalEq) {
                            text_frete.setText("Frete: "+moeda.format(Double.parseDouble(""+valorFrete)));
                            text_freteVALOR.setText(moeda.format(Double.parseDouble(""+valorFrete)));

                        } else {
                                text_frete.setText("Frete: Grátis!");
                                text_freteVALOR.setText(moeda.format(Double.parseDouble("0")));
                                valorFrete = Float.parseFloat("0");
                        }
                        text_frete.setTextColor(Color.parseColor("#F5DC49"));
                        break;
                    case "3":
                            if(valorIsento >= finalEq){
                                text_frete.setText("Frete: "+moeda.format(Double.parseDouble(""+valorFrete)));
                                text_freteVALOR.setText(moeda.format(Double.parseDouble(""+valorFrete)));
                            }else{
                                text_frete.setText("Frete: Grátis!");
                                text_freteVALOR.setText(moeda.format(Double.parseDouble("0")));
                                valorFrete = Float.parseFloat("0");
                            }
                            break;
                    default:
                            text_frete.setText("");
                        break;
                }
            }
        });

        radioRetirada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ent_indip = 0;
                ret_indip = 0;
                tipoER = 2;
                switch(auxsharedAuxEntrega) {
                    case "0":
                            text_frete.setText("");
                        break;
                    case "2":
                            text_frete.setText("");
                            text_freteVALOR.setText(moeda.format(Double.parseDouble("0")));
                        break;
                    case "1":
                            text_frete.setText("Retirada Indisponível");
                            text_frete.setTextColor(Color.parseColor("#F5DC49"));
                            ret_indip = 1;
                        break;
                    case "3":
                            text_frete.setText("");
                            text_freteVALOR.setText(moeda.format(Double.parseDouble("0")));
                            break;
                    default:
                            text_frete.setText("");
                        break;
                }
            }
        });
        //**************************************************************************************************************

        //************************************OBRIGADOR DE CLICKS NO RADIO**********************************************
        final float finalEq1 = eq;


        radioPag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedIdPAG) {
                radioMpag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, final int checkedId) {
                        tipoDC=1;
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                           // trava o botao até que todos os metodos retornam os resultados
                                if (((statusEntrega!=-1) || (statusRetirada!=-1)) && ((statusHoraE!=-1) && (statusHoraR!=-1))) {
                                    if (ent_indip == 1) {
                                        Toast.makeText(getApplicationContext(), "Entrega Indiponível!", Toast.LENGTH_SHORT).show();
                                    }
                                    if (ret_indip == 1) {
                                        Toast.makeText(getApplicationContext(), "Retirada Indiponível!", Toast.LENGTH_SHORT).show();
                                    }

                                    if (ent_indip != 1 && ret_indip != 1) {

                                        if (tipoER == 1) { // Selecionou entrega

                                            if (checkedId == R.id.radioDinheiro) {
                                                int status = statusEntrega;



                                                if ((status == 2) && (statusHoraE==1)) {
                                                    Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                    intent.putExtra("produto", aux);
                                                    intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                    intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                    intent.putExtra("idmercado", auxMercado);
                                                    intent.putExtra("valor_frete", ""+valorFrete);
                                                    intent.putExtra("valorTotal", "" + finalEq1);
                                                    intent.putExtra("diaestimado", "" + diaestimadoE);
                                                    intent.putExtra("horaestimada", "" + horaestimadaE);
                                                    finish();
                                                    startActivity(intent);
                                                } else if (status == 1) {
                                                    Toast.makeText(v.getContext(), "Você está fora da área de atendimento deste estabelecimento", Toast.LENGTH_LONG).show();
                                                } else if (status == 0) {
                                                    Toast.makeText(v.getContext(), "Não entregamos no seu endereço", Toast.LENGTH_LONG).show();
                                                } else if (statusHoraE==0){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User clicked OK button
                                                            Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                            intent.putExtra("produto", aux);
                                                            intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                            intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                            intent.putExtra("idmercado", auxMercado);
                                                            intent.putExtra("valor_frete", ""+valorFrete);
                                                            intent.putExtra("valorTotal", "" + finalEq1);
                                                            intent.putExtra("diaestimado", "" + diaestimadoE);
                                                            intent.putExtra("horaestimada", "" + horaestimadaE);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User cancelled the dialog
                                                        }
                                                    });
                                                    builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoE+" às "+horaestimadaE)
                                                            .setTitle("Atenção");
                                                    // Setting Icon to Dialog
                                                    builder.setIcon(R.drawable.alert);
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();

                                                }
                                            } else {
                                                if (checkedId == (R.id.radioCart) || checkedId==(R.id.radioCheque)) {
                                                    int status = statusEntrega;

                                                    if ((status == 2) && (statusHoraE==1)) {
                                                        Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                        intent.putExtra("produto", aux);
                                                        intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                        intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                        intent.putExtra("idmercado", auxMercado);
                                                        intent.putExtra("ischeque", isCheque);
                                                        intent.putExtra("valor_frete", ""+valorFrete);
                                                        intent.putExtra("valorTotal", "" + finalEq1);
                                                        intent.putExtra("diaestimado", "" + diaestimadoE);
                                                        intent.putExtra("horaestimada", "" + horaestimadaE);
                                                        finish();
                                                        startActivity(intent);
                                                    } else if (status == 1) {
                                                        Toast.makeText(v.getContext(), "Você está fora da área de atendimento deste estabelecimento", Toast.LENGTH_LONG).show();
                                                    } else if (status == 0) {
                                                        Toast.makeText(v.getContext(), "Não entregamos no seu endereço", Toast.LENGTH_LONG).show();
                                                    }else if (statusHoraE==0){
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // User clicked OK button
                                                                Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                                intent.putExtra("produto", aux);
                                                                intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                                intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                                intent.putExtra("idmercado", auxMercado);
                                                                intent.putExtra("ischeque", isCheque);
                                                                intent.putExtra("valor_frete", ""+valorFrete);
                                                                intent.putExtra("valorTotal", "" + finalEq1);
                                                                intent.putExtra("diaestimado", "" + diaestimadoE);
                                                                intent.putExtra("horaestimada", "" + horaestimadaE);
                                                                finish();
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // User cancelled the dialog
                                                            }
                                                        });
                                                        builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoE+" às "+horaestimadaE)
                                                                .setTitle("Atenção");
                                                        builder.setIcon(R.drawable.alert);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Opção Inválida!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        } else {

                                            if (checkedId == R.id.radioDinheiro) {

                                                int status = statusRetirada;

                                                if ((status == 1) && (statusHoraR==1)) {
                                                    Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                    intent.putExtra("produto", aux);
                                                    intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                    intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                    intent.putExtra("idmercado", auxMercado);
                                                    intent.putExtra("valor_frete", ""+valorFrete);
                                                    intent.putExtra("valorTotal", "" + finalEq1);
                                                    intent.putExtra("diaestimado", "" + diaestimadoR);
                                                    intent.putExtra("horaestimada", "" + horaestimadaR);
                                                    finish();
                                                    startActivity(intent);
                                                } else if (status == 0) {
                                                    Toast.makeText(getApplicationContext(), "Fora da área de atendimento desse estabelecimento", Toast.LENGTH_LONG).show();
                                                }else if (statusHoraR==0){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User clicked OK button
                                                            Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                            intent.putExtra("produto", aux);
                                                            intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                            intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                            intent.putExtra("idmercado", auxMercado);
                                                            intent.putExtra("valor_frete", ""+valorFrete);
                                                            intent.putExtra("valorTotal", "" + finalEq1);
                                                            intent.putExtra("diaestimado", "" + diaestimadoR);
                                                            intent.putExtra("horaestimada", "" + horaestimadaR);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User cancelled the dialog
                                                        }
                                                    });
                                                    builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoR+" às "+horaestimadaR)
                                                            .setTitle("Atenção");
                                                    builder.setIcon(R.drawable.alert);
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();
                                                }


                                            } else {
                                                if (checkedId == (R.id.radioCart) || checkedId==(R.id.radioCheque)) {
                                                    int status = statusRetirada;

                                                    if ((status == 1) && (statusHoraR==1)) {
                                                        Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                        intent.putExtra("produto", aux);
                                                        intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                        intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                        intent.putExtra("idmercado", auxMercado);
                                                        intent.putExtra("ischeque", isCheque);
                                                        intent.putExtra("valor_frete", ""+valorFrete);
                                                        intent.putExtra("valorTotal", "" + finalEq1);
                                                        intent.putExtra("diaestimado", "" + diaestimadoR);
                                                        intent.putExtra("horaestimada", "" + horaestimadaR);
                                                        finish();
                                                        startActivity(intent);
                                                    } else if (status == 0) {
                                                        Toast.makeText(getApplicationContext(), "Fora da área de atendimento desse estabelecimento", Toast.LENGTH_LONG).show();
                                                    }else if (statusHoraR==0){
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // User clicked OK button
                                                                Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                                intent.putExtra("produto", aux);
                                                                intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                                intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                                intent.putExtra("idmercado", auxMercado);
                                                                intent.putExtra("ischeque", isCheque);
                                                                intent.putExtra("valor_frete", ""+valorFrete);
                                                                intent.putExtra("valorTotal", "" + finalEq1);
                                                                intent.putExtra("diaestimado", "" + diaestimadoR);
                                                                intent.putExtra("horaestimada", "" + horaestimadaR);
                                                                finish();
                                                                startActivity(intent);
                                                            }
                                                        });
                                                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                // User cancelled the dialog
                                                            }
                                                        });
                                                        builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoR+" às "+horaestimadaR)
                                                                .setTitle("Atenção");
                                                        builder.setIcon(R.drawable.alert);
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        });

                    }
                });
            }
        });

        //Dupliquei os dois para resolver o problema de seleção de ordem de pagamento com tipo depagamento

        radioMpag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, final int checkedId) {
                radioPag.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, final int checkedIdPAG) {
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // trava o botao até que todos os metodos retornam os resultados
                                if (((statusEntrega!=-1) || (statusRetirada!=-1)) && ((statusHoraE!=-1) && (statusHoraR!=-1))) {
                                    if (tipoER==1 && !text_frete.equals("Entrega indisponível")) {

                                        if (checkedId == R.id.radioDinheiro) {
                                        int status = statusEntrega;

                                        if ((status == 2) && (statusHoraE==1)) {
                                            Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                            intent.putExtra("produto", aux);
                                            intent.putExtra("entregaRetiradaESCOLHA", 2);
                                            intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                            intent.putExtra("idmercado", auxMercado);
                                            intent.putExtra("valor_frete", ""+valorFrete);
                                            intent.putExtra("valorTotal", "" + finalEq1);
                                            intent.putExtra("diaestimado", "" + diaestimadoE);
                                            intent.putExtra("horaestimada", "" + horaestimadaE);
                                            finish();
                                            startActivity(intent);
                                        }else if (status==1){
                                            Toast.makeText(v.getContext(),"Você está fora da área de atendimento deste estabelecimento", Toast.LENGTH_LONG).show();
                                        }
                                        else if (status==0){
                                            Toast.makeText(v.getContext(),"Não entregamos no seu endereço", Toast.LENGTH_LONG).show();
                                        }else if (statusHoraE==0){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User clicked OK button
                                                    Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                    intent.putExtra("produto", aux);
                                                    intent.putExtra("entregaRetiradaESCOLHA",2);
                                                    intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                    intent.putExtra("idmercado", auxMercado);
                                                    intent.putExtra("valor_frete", ""+valorFrete);
                                                    intent.putExtra("valorTotal", "" + finalEq1);
                                                    intent.putExtra("diaestimado", "" + diaestimadoE);
                                                    intent.putExtra("horaestimada", "" + horaestimadaE);
                                                    finish();
                                                    startActivity(intent);
                                                }
                                            });
                                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    // User cancelled the dialog
                                                }
                                            });
                                            builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoE+" às "+horaestimadaE)
                                                    .setTitle("Atenção");
                                            builder.setIcon(R.drawable.alert);
                                            AlertDialog dialog = builder.create();
                                            dialog.show();

                                        }
                                    } else {
                                        if (checkedId == (R.id.radioCart) || checkedId==(R.id.radioCheque) ) {
                                            int status = statusEntrega;

                                            if ((status == 2) && (statusHoraE==1)) {
                                                Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                intent.putExtra("produto", aux);
                                                intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                intent.putExtra("idmercado", auxMercado);
                                                intent.putExtra("ischeque", isCheque);
                                                intent.putExtra("valor_frete", ""+valorFrete);
                                                intent.putExtra("valorTotal", "" + finalEq1);
                                                intent.putExtra("diaestimado", "" + diaestimadoE);
                                                intent.putExtra("horaestimada", "" + horaestimadaE);
                                                finish();
                                                startActivity(intent);
                                            }else if (status==1){
                                                Toast.makeText(v.getContext(),"Você está fora da área de atendimento deste estabelecimento", Toast.LENGTH_LONG).show();
                                            }
                                            else if (status==0){
                                                Toast.makeText(v.getContext(),"Não entregamos no seu endereço", Toast.LENGTH_LONG).show();
                                            }else if (statusHoraE==0){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User clicked OK button
                                                        Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                        intent.putExtra("produto", aux);
                                                        intent.putExtra("entregaRetiradaESCOLHA", 2);
                                                        intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                        intent.putExtra("idmercado", auxMercado);
                                                        intent.putExtra("ischeque", isCheque);
                                                        intent.putExtra("valor_frete", ""+valorFrete);
                                                        intent.putExtra("valorTotal", "" + finalEq1);
                                                        intent.putExtra("diaestimado", "" + diaestimadoE);
                                                        intent.putExtra("horaestimada", "" + horaestimadaE);
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                    }
                                                });
                                                builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoE+" às "+horaestimadaE)
                                                        .setTitle("Atenção");
                                                builder.setIcon(R.drawable.alert);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Opção Inválida!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    if (!text_frete.equals("Retirada Indisponível")) {
                                        if (checkedId == R.id.radioDinheiro) {
                                            int status = statusRetirada; // Selecionou Retirada

                                            if ((status == 1) && (statusHoraR==1)) {
                                                Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                intent.putExtra("produto", aux);
                                                intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                intent.putExtra("idmercado", auxMercado);
                                                intent.putExtra("valor_frete", ""+valorFrete);
                                                intent.putExtra("valorTotal", "" + finalEq1);
                                                intent.putExtra("diaestimado", "" + diaestimadoR);
                                                intent.putExtra("horaestimada", "" + horaestimadaR);
                                                finish();
                                                startActivity(intent);
                                            } else if (status==0){
                                                Toast.makeText(getApplicationContext(), "Fora da área de atendimento desse estabelecimento", Toast.LENGTH_LONG).show();
                                            }else if (statusHoraR==0){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User clicked OK button
                                                        Intent intent = new Intent(getApplicationContext(), FNavigation_dinheiroCERTO.class);
                                                        intent.putExtra("produto", aux);
                                                        intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                        intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                        intent.putExtra("idmercado", auxMercado);
                                                        intent.putExtra("valor_frete", ""+valorFrete);
                                                        intent.putExtra("valorTotal", "" + finalEq1);
                                                        intent.putExtra("diaestimado", "" + diaestimadoR);
                                                        intent.putExtra("horaestimada", "" + horaestimadaR);
                                                        finish();
                                                        startActivity(intent);
                                                    }
                                                });
                                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // User cancelled the dialog
                                                    }
                                                });
                                                builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoR+" às "+horaestimadaR)
                                                        .setTitle("Atenção");
                                                builder.setIcon(R.drawable.alert);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                            }
                                        } else {
                                            if (checkedId == (R.id.radioCart) || checkedId==(R.id.radioCheque)) {
                                                int status = statusRetirada;

                                                if ((status == 1) && (statusHoraR==1)) {
                                                    Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                    intent.putExtra("produto", aux);
                                                    intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                    intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                    intent.putExtra("idmercado", auxMercado);
                                                    intent.putExtra("ischeque", isCheque);
                                                    intent.putExtra("valor_frete", ""+valorFrete);
                                                    intent.putExtra("valorTotal", "" + finalEq1);
                                                    intent.putExtra("diaestimado", "" + diaestimadoR);
                                                    intent.putExtra("horaestimada", "" + horaestimadaR);
                                                    finish();
                                                    startActivity(intent);
                                                } else if (status==0){
                                                    Toast.makeText(getApplicationContext(), "Fora da área de atendimento desse estabelecimento", Toast.LENGTH_LONG).show();
                                                }else if (statusHoraR==0){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(AFechamento.this);
                                                    builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User clicked OK button
                                                            Intent intent = new Intent(getApplicationContext(), FNavigation_cartaoCERTO.class);
                                                            intent.putExtra("produto", aux);
                                                            intent.putExtra("entregaRetiradaESCOLHA", 1);
                                                            intent.putExtra("entregaRetirada", auxsharedAuxEntrega);
                                                            intent.putExtra("idmercado", auxMercado);
                                                            intent.putExtra("ischeque", isCheque);
                                                            intent.putExtra("valor_frete", ""+valorFrete);
                                                            intent.putExtra("valorTotal", "" + finalEq1);
                                                            intent.putExtra("diaestimado", "" + diaestimadoR);
                                                            intent.putExtra("horaestimada", "" + horaestimadaR);
                                                            finish();
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            // User cancelled the dialog
                                                        }
                                                    });
                                                    builder.setMessage("Seu pedido estará disponível no dia "+diaestimadoR+" às "+horaestimadaR)
                                                            .setTitle("Atenção");
                                                    builder.setIcon(R.drawable.alert);
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();

                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Opção Inválida!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }else{
                                        Toast.makeText(getApplicationContext(), ""+text_frete.getText(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                }
                            }
                        });

                    }
                });
            }
        });

        //************************************FIM - OBRIGADOR DE CLICKS NO RADIO**********************************************
                }



    // ---- #########  metodo que verifica os meios de pagamento ativos
    public void validaMeios(){

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        String urlLL;
        urlLL = auxUrl + "validaMeios.php?id_mercado=" + id_mercado;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLL,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoMeios;
                                JSONArray arrayPedidos = consultaArray.getJSONArray("validameios");
                                infoMeios = (JSONObject) arrayPedidos.get(0);


                                /* Meios de Pagamento
                                *  1 - Dinheiro
                                *  2 - Dinheiro e Cartao
                                *  3 - Dinheiro, Cartão e Cheque
                                *  4 - Dinheiro e Cheque
                                * */

                                if (infoMeios.getString("tipo").equals("1")) {
                                    meioDinheiro =1;
                                    meioCartao = 0;
                                    meioCheque = 0;
                                }
                                if (infoMeios.getString("tipo").equals("2")) {
                                    meioDinheiro =1;
                                    meioCartao = 1;
                                    meioCheque = 0;
                                }
                                if (infoMeios.getString("tipo").equals("3")) {
                                    meioDinheiro =1;
                                    meioCartao = 1;
                                    meioCheque = 1;
                                }
                                if (infoMeios.getString("tipo").equals("4")) {
                                    meioDinheiro =1;
                                    meioCartao = 0;
                                    meioCheque = 1;
                                }

                                if (meioCartao==0){radioCartao.setVisibility(View.INVISIBLE);}
                                if (meioCheque==0){radioCheque.setVisibility(View.INVISIBLE);}

                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);


    }

    // ---- #########  metodo que verifica se é possível a ENTREGA
    public int validaEntrega(){

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        // ----  ID_Cliente
        SharedPreferences cliente = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String id_cliente = cliente.getString("id_cliente", "");

        //recuperando latitude e logitude
        SharedPreferences sPlatitude = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        String latitude = sPlatitude.getString("latitude", "");
        String longitude = sPlatitude.getString("longitude", "");


        String urlLL = auxUrl + "validaEntrega.php?lat=" + latitude + "&lng=" + longitude+"&id_mercado="+id_mercado+"&id_cliente="+id_cliente;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLL,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoEntrega;
                                JSONArray arrayPedidos = consultaArray.getJSONArray("entrega");
                                infoEntrega = (JSONObject) arrayPedidos.get(0);

                                // 0 (Bloqueado) --> Não entrega nesse endereço
                                if (infoEntrega.getString("status").equals("0")) {
                                    statusEntrega = 0;
                                }
                                //  1 (Bloqueado) -->  pedido feito fora das ciadades autorizadas
                                if (infoEntrega.getString("status").equals("1")) {
                                    statusEntrega =1;
                                }
                                // 2 (OK) --> Liberado o pedido de entrega
                                if (infoEntrega.getString("status").equals("2")) {
                                    statusEntrega =2;
                                }


                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);

        return statusEntrega;

    }



    // ---- #########  metodo que verifica se é possível a RETIRADA
    public int validaRetirada(){

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");

        //recuperando latitude e logitude
        SharedPreferences sPlatitude = getApplication().getSharedPreferences("Latitude-Longitude", Context.MODE_PRIVATE);
        String latitude = sPlatitude.getString("latitude", "");
        String longitude = sPlatitude.getString("longitude", "");


        String urlLL = auxUrl + "validaRetirada.php?lat=" + latitude + "&lng=" + longitude+"&id_mercado="+id_mercado;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLL,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoEntrega;
                                JSONArray arrayPedidos = consultaArray.getJSONArray("retirada");
                                infoEntrega = (JSONObject) arrayPedidos.get(0);

                                // 0 (Bloqueado) --> Pedido feito fora das cidades autorizadas
                                if (infoEntrega.getString("status").equals("0")) {
                                    statusRetirada = 0;
                                }
                                //  1 (OK) -->  pedido feito dentro das ciadades autorizadas
                                if (infoEntrega.getString("status").equals("1")) {
                                    statusRetirada =1;
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyProvider.getInstance(this).addRequest(stringRequest);

        return statusRetirada;

    }

    // ---- #########  metodo que verifica se é possível executar o pedido no mesmo dia
    public void validaHorario(final int tipo){

        //URL do JSON
        SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
        String auxUrl = urlPref.getString("url_base", "");
        SharedPreferences mercado = getApplication().getSharedPreferences("INFOS_MERCADO", Context.MODE_PRIVATE);
        String id_combo = mercado.getString("id_combo", "");

        Log.d("id_combo==", id_combo+"");
        Log.d("id_mercado==", id_mercado+"");
        Log.d("tipo==", tipo+"");



        String urlLL;
        urlLL = auxUrl + "validaHorario.php?id_mercado=" + id_mercado + "&id_combo=" + id_combo + "&tipo=" + tipo;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlLL,
                new Response.Listener<String>() {
                    //                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        final String[] data = {""};
                        try {
                            if (response.toString().length() > 0 && response.toString() != " ") {
                                data[0] = response.toString();
                                JSONObject consultaArray = new JSONObject(data[0]);

                                JSONObject infoHorario;
                                JSONArray arrayPedidos = consultaArray.getJSONArray("validahorario");
                                infoHorario = (JSONObject) arrayPedidos.get(0);


                                if (tipo==2){ // Retirada
                                    // 0 (Bloqueado) --> Não entrega nesse endereço
                                    if (infoHorario.getString("status").equals("0")) {statusHoraR = 0;}
                                    //  1 (Bloqueado) -->  pedido feito fora das ciadades autorizadas
                                    if (infoHorario.getString("status").equals("1")) {statusHoraR =1;}
                                    horaestimadaR = infoHorario.getString("horaestimada");
                                    diaestimadoR = infoHorario.getString("diaestimado");
                                }
                                if (tipo==1){ // Entrega
                                    // 0 (Bloqueado) --> Não entrega nesse endereço
                                    if (infoHorario.getString("status").equals("0")) {statusHoraE = 0;}
                                    //  1 (Bloqueado) -->  pedido feito fora das ciadades autorizadas
                                    if (infoHorario.getString("status").equals("1")) {statusHoraE =1;}
                                    horaestimadaE = infoHorario.getString("horaestimada");
                                    diaestimadoE = infoHorario.getString("diaestimado");
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        int timeout= 20000; // 20 segundos
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyProvider.getInstance(this).addRequest(stringRequest);


    }




    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


        //Limpar shared

        SharedPreferences settings = getApplicationContext().getSharedPreferences("produtos", Context.MODE_PRIVATE);
        settings.edit().clear().commit();

        SharedPreferences settings2 = getApplicationContext().getSharedPreferences("AUX_COMPRA", Context.MODE_PRIVATE);
        settings2.edit().clear().commit();

        //limpar banco
        db.deleteAll();

        //limpar lista
        ListaPD.clear();

    }



}


