package com.aquimercado.aquimercado;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aquimercado.aquimercado.mercado.MercadoItemNavigation;

public class SplashscreenFIRST extends AppCompatActivity implements Runnable, ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int READ_GPS_REQUEST_CODE = 12;
    protected LocationManager locationManager;
    public static final String  MyPREFERENCES = "PermissaoGPS" ;
    public static String provider = null;
    public Integer cont = 0;
    private ProgressBar mProgress;
    private int mProgressStatus = 0;


    public void onCreate(Bundle savedIntance) {
        super.onCreate(savedIntance);
        setContentView(com.aquimercado.aquimercado.R.layout.splashscreen);
        mProgress = (ProgressBar) findViewById(R.id.pbSplash);
        mProgress.setVisibility(View.INVISIBLE);


        Handler splashcreen = new Handler();
        splashcreen.postDelayed(this, 1000);


    }

    @Override
    public void run() {

        mProgress.setVisibility(View.VISIBLE);

        // Adquire a referência ao Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define o listener que responde às atualizações de localização

        final LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                // Chamado quando uma x'''va localização é encontrada
                location.getLatitude();// Latitude coletada
                location.getLongitude();// Longitude coletada

                if (cont == 0) {

                    Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
                    intent.putExtra("latitude", location.getLatitude());
                    intent.putExtra("longitude", location.getLongitude());
                    startActivity(intent);
                    cont++;
                    finish();
                } else
                    cont++;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                switch(status) {
                    case LocationProvider.AVAILABLE:
                        break;
                    case LocationProvider.OUT_OF_SERVICE:
                        break;
                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
                        break;
                }
            }

            @Override
            public void onProviderEnabled(String provider) {
                //Quando desabilita o GPS
                Toast.makeText(getBaseContext(), "Sistema de localização Online !", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                //Quando desabilita o GPS
                Toast.makeText(getBaseContext(), "Sistema de localização Offline !", Toast.LENGTH_LONG).show();
            }
        };

        //***************************************VERIFICAÇÃO DE VERSÃO********************************************************

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (getApplicationContext().checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(SplashscreenFIRST.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        READ_GPS_REQUEST_CODE);

            }
        }else{
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            // Retorna a localização com a data da última localização conhecida
            Location location = locationManager.getLastKnownLocation(provider);


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 	0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 	0, locationListener);

        }

        //*************************************VERIFICAÇÃO DE PERMISSÃO CONCEDIDA***********************************************
        SharedPreferences produtosPref = getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String auxiliando = produtosPref.getString("permissions", "");

        if(auxiliando.equalsIgnoreCase("1")) {
            // Define um critério para selecionar o location provider
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);

            // Retorna a localização com a data da última localização conhecida
            Location location = locationManager.getLastKnownLocation(provider);
            try{
                if (cont == 0) {
                    Intent intent = new Intent(getApplicationContext(), MercadoItemNavigation.class);
                    intent.putExtra("latitude", location.getLatitude());
                    intent.putExtra("longitude", location.getLongitude());
                    startActivity(intent);
                    cont++;
                    mProgress.setProgress(mProgressStatus);
                    mProgress.setVisibility(View.INVISIBLE);
                    finish();
                } else
                    cont++;
            }catch(Exception e){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 	0, locationListener);
            }


        }if (auxiliando.equalsIgnoreCase("0")) {
            Toast.makeText(getApplicationContext(), "Permissão de localização não concedida!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_GPS_REQUEST_CODE: {
                // Se o usuário não deu permissão o array está vazio.
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    //*****************************Se deu permissão gravo no shared*******************
                    SharedPreferences produtosPref = getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = produtosPref.edit();
                    editor.putString("permissions", "1");
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                    finish();
                    startActivity(intent);

                } else {

                    //****************************Se não deu não gravo no shared**********************
                    SharedPreferences produtosPref = getApplication().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = produtosPref.edit();
                    editor.putString("permissions","0");
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), SplashscreenFIRST.class);
                    finish();
                    startActivity(intent);
                }
                return;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
