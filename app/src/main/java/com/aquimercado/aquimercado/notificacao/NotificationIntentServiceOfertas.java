package com.aquimercado.aquimercado.notificacao;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.notificacao.oferta.OfertaNotificacao;


/**
 * Created by rafael on 23/08/16.
 */

public class NotificationIntentServiceOfertas extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    private String cod_pedido = "";
    private String hora_pedido = "";
    private String nome_mercado = "";
    private String cont_pedido = "";


    public static  Context contexto;

    public NotificationIntentServiceOfertas() {
        super(NotificationIntentServiceOfertas.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        contexto = context;
        Intent intent = new Intent(context, NotificationIntentServiceOfertas.class);
        intent.setAction(ACTION_START);
        Log.d("notificationIntent","OK");
        return intent;

    }



    @Override
    protected void onHandleIntent(final Intent intent) {
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                final ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                final boolean isWifiConn = networkInfo.isConnected();

                networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final boolean isMobileConn = networkInfo.isConnected();

                if(isWifiConn == true || isMobileConn == true) {

                    //URL do JSON
                    SharedPreferences urlPref = getApplication().getSharedPreferences("URLS", Context.MODE_PRIVATE);
                    String auxUrl = urlPref.getString("url_base", "");
                    // ID_Cliente
                    SharedPreferences id_cliente = getApplication().getSharedPreferences("SessaoUser", Context.MODE_PRIVATE);
                    String id = id_cliente.getString("id_cliente", "");

                    Log.d("chamoujanelanotifica=","OK");

                    processStartNotification();
                   //NotificationEventReceiverOfertas.cancelAlarm(contexto);


                    // Verifica se tem ofertas (caso o cliente habilitou o recebimento)
                   /* String strUrl = auxUrl + "temofertas.php?id_cliente="+id;


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, strUrl,
                            new Response.Listener<String>() {
//                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onResponse(String response) {
                                    final String[] data = {""};
                                    try {
                                        if(response.toString().length() > 0 && response.toString() != " ") {
                                            data[0] = response.toString();
                                            JSONObject consultaArray = new JSONObject(data[0]);


                                            if (!consultaArray.isNull("pedidos")) {

                                                JSONObject infoPedido;

                                                JSONArray arrayPedidos = consultaArray.getJSONArray("pedidos");

                                                infoPedido = (JSONObject) arrayPedidos.get(0);
                                                processStartNotification();
                                                NotificationEventReceiver.cancelAlarm(contexto);

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
                    */
                }
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processStartNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("Oferta Especiais")
            .setAutoCancel(true)
            .setContentText("aquiMercado.com")
            .setSmallIcon(R.drawable.carrinho2);

        Log.d("crioujanelanotifica","OK");
        //final Intent mainIntent = new Intent(this, NotificacaoAntecedencia.class);
        final Intent mainIntent = new Intent(this, OfertaNotificacao.class);





        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.vibrate = new long[]{100, 250, 100, 500};

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, notification);
    }

}
