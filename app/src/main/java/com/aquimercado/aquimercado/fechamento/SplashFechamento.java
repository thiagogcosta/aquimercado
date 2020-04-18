package com.aquimercado.aquimercado.fechamento;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.SplashscreenFIRST;

public class SplashFechamento extends AppCompatActivity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_fechamento);

        Handler splashcreen = new Handler();
        splashcreen.postDelayed(this, 3500);

    }

    @Override
    public void run() {
        Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
        startActivity(intent);
        finish();
    }
}
