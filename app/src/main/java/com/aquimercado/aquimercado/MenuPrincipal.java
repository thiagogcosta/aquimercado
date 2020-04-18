package com.aquimercado.aquimercado;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aquimercado.aquimercado.indicar.IndicarNavigation;
import com.aquimercado.aquimercado.login.AlterarNavigation;
import com.aquimercado.aquimercado.login.CadastroNavigation;
import com.aquimercado.aquimercado.login.LoginNavigation;
import com.aquimercado.aquimercado.pedidos.PedidosListNavigation;
import com.aquimercado.aquimercado.sobre.ContatoNavigation;

/**
 * Created by thiago on 09/02/2017.
 */

public class MenuPrincipal implements NavigationView.OnNavigationItemSelectedListener {
    Context ctx;
    Activity act;

    public MenuPrincipal(Context applicationContext, Activity ln){
        ctx= applicationContext;
        act = ln;

        //***********************SETANDO O NOME NO NAV ***************************************
        NavigationView navigationView = (NavigationView) act.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        //verificar logado
        SharedPreferences auxName = ctx.getSharedPreferences("SessaoUserNOME", Context.MODE_PRIVATE);
        String auxName2 = auxName.getString("nome", "");

        SharedPreferences produtosPref2 = ctx.getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxLogado = produtosPref2.getString("logado", "");
        TextView tx = (TextView) header.findViewById(R.id.user_name);

        if (auxLogado.length() > 0) {

            String aux[] = auxName2.split(" ");
            String aux2 = "";
            if(aux[0].length() > 10){
                for(int i=0; i<aux[0].length(); i++){
                    aux2 += aux[0].charAt(i);
                }
                aux2 += "...";
                tx.setText(aux2);
            }else{
                tx.setText(aux[0]);
            }
        }else {
            tx.setText("Visitante");
        }
        //************************************************************************************

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences produtosPref = ctx.getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
        String auxiliando = produtosPref.getString("logado", "");
        SharedPreferences settings2 = ctx.getSharedPreferences("SessaoUserFULL", Context.MODE_PRIVATE);
        SharedPreferences settings3 = ctx.getSharedPreferences("SessaoUserNOME", Context.MODE_PRIVATE);

        if (id == R.id.menu_home) {
            Intent intent = new Intent(ctx, SplashscreenFIRST.class);
            act.finish();
            act.startActivity(intent);
        } else if (id == R.id.menu_conectar) {
            if(auxiliando.length() > 0){
                produtosPref.edit().clear().apply();
                settings2.edit().clear().apply();
                settings3.edit().clear().apply();
                Toast.makeText(ctx,"Você foi desconectado!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, SplashscreenFIRST.class);
                act.finish();
                act.startActivity(intent);
            }else{
                Intent intent = new Intent(ctx, LoginNavigation.class);
                act.finish();
                act.startActivity(intent);
            }
        } else if (id == R.id.menu_registrar) {
            if(auxiliando.length() > 0) {
                Toast.makeText(ctx,"Por favor, desconecte da sua conta para registrar outra!",Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(ctx, CadastroNavigation.class);
                act.finish();
                act.startActivity(intent);
            }
        } else if (id == R.id.menu_alterar) {
            if(auxiliando.length() > 0){
                Intent intent = new Intent(ctx, AlterarNavigation.class);
                act.finish();
                act.startActivity(intent);
            }else {
                Toast.makeText(ctx, "Acesso restrito, por favor conecte-se a uma conta!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, LoginNavigation.class);
                act.finish();
                act.startActivity(intent);
            }
        }else if(id == R.id.menu_pedidos) {
            if(auxiliando.length() > 0){
                Intent intent = new Intent(ctx, PedidosListNavigation.class);
                intent.putExtra("tipo_pedido", "ambos");
                act.finish();
                act.startActivity(intent);
            }else {
                Toast.makeText(ctx, "Acesso restrito, por favor conecte-se a uma conta!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, LoginNavigation.class);
                act.finish();
                act.startActivity(intent);
            }
        } else if (id == R.id.menu_contato) {
            Intent intent = new Intent(ctx, ContatoNavigation.class);
            act.finish();
            act.startActivity(intent);
        }else if (id == R.id.menu_indicar) {
            Intent intent = new Intent(ctx, IndicarNavigation.class);
            act.finish();
            act.startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) act.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
