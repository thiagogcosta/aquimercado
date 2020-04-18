package com.aquimercado.aquimercado.login;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.aquimercado.aquimercado.Mask;
import com.aquimercado.aquimercado.MenuPrincipal;
import com.aquimercado.aquimercado.R;

public class CadastroCompletoNavigation extends AppCompatActivity {

    public static final String nome = "nome";
    public static final String senha = "senha";
    public static final String email = "email";
    public static final String login = "login";

    private EditText editNas;
    private EditText editTel;
    private EditText editCPF;
    private EditText editCEP;
    private EditText editEstado;
    private EditText editCidade;
    private EditText editRua;
    private EditText editNumero;
    private Cliente usuario;



    private Button btRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_completo_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.rgb(15, 77, 135));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        new MenuPrincipal(this.getApplicationContext(), this);

        //Setando a máscara no EditText

        editNas = (EditText) findViewById(R.id.editNasc);
        editNas.addTextChangedListener(Mask.insert("##/##/####", editNas));

        editTel = (EditText) findViewById(R.id.editTel);
        editTel.addTextChangedListener(Mask.insert("(##)####-####", editTel));

        editCPF= (EditText) findViewById(R.id.editCPF);
        editCPF.addTextChangedListener(Mask.insert("###.###.###-##", editCPF));

        editCEP= (EditText) findViewById(R.id.editCEP);
        editCEP.addTextChangedListener(Mask.insert("#####-###", editCEP));

        editEstado= (EditText) findViewById(R.id.editEstado);
        editCidade= (EditText) findViewById(R.id.editCidade);
        editRua= (EditText) findViewById(R.id.editRua);
        editNumero= (EditText) findViewById(R.id.editNum);


        //Limpando focus
        editNas.clearFocus();
        editTel.clearFocus();
        editCPF.clearFocus();
        editCEP.clearFocus();
        editEstado.clearFocus();
        editCidade.clearFocus();
        editRua.clearFocus();
        editNumero.clearFocus();


        btRegistrar = (Button) findViewById(R.id.btRegistrar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
