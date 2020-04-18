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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.aquimercado.aquimercado.R;
import com.aquimercado.aquimercado.VolleyProvider;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Created by rafael on 23/08/16.
 */

public class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    private String cod_pedido = "";
    private String hora_pedido = "";
    private String nome_mercado = "";
    private String cont_pedido = "";


    public static  Context contexto;

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        contexto = context;
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
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

                    String strUrl = auxUrl + "notificacaopedido.php?id_cliente="+id;




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
                                                int cont_pronto = 0;
                                                int cont_processamento = 0;
                                                int cont_cancelado=0;



                                                JSONArray arrayPedidos =  consultaArray.getJSONArray("pedidos");

                                                Log.d("AlarmeNotificacao","NADA");
                                                //percorrer a lista de pedidos em andamento (processamento ou pronto)
                                                for (int i = 0; i < arrayPedidos.length(); i++) {
                                                    infoPedido = (JSONObject) arrayPedidos.get(i);
                                                    if (infoPedido.getString("status_pedido").equals("pronto")) {
                                                        cont_pronto++;
                                                    }
                                                    if (infoPedido.getString("status_pedido").equals("processamento")) {
                                                        cont_processamento++;
                                                    }
                                                    if (infoPedido.getString("status_pedido").equals("cancelado")) {
                                                        cont_cancelado++;
                                                    }
                                                }

                                                int cont_total = cont_processamento + cont_pronto;

                                                // tem mais de um pedido em andamento
                                                if (cont_total > 1) {
                                                    if (cont_pronto >= 1) { // tem um ou mais pedidos prontos

                                                        cod_pedido = "";
                                                        hora_pedido = "";
                                                        nome_mercado = "Você tem "+cont_pronto+" pedido(s) pronto(s)";
                                                        processStartNotification();
                                                        // nao desliga o alarm porque tem outros pedidos em procesamento
                                                    }
                                                } else if (cont_pronto == 1) {
                                                    Log.d("AlarmeNotificacao","OKO");
                                                    infoPedido = (JSONObject) consultaArray.getJSONArray("pedidos").get(0);
                                                    cod_pedido = "Código: " + infoPedido.getString("cod_pedido").toString();
                                                    hora_pedido = infoPedido.getString("hora_pedido");
                                                    nome_mercado = infoPedido.getString("nome_mercado");
                                                    processStartNotification();
                                                    NotificationEventReceiver.cancelAlarm(contexto);
                                                }

                                                if (cont_cancelado>0){
                                                    int i=0;
                                                    do {
                                                        infoPedido = (JSONObject) consultaArray.getJSONArray("pedidos").get(i);
                                                        if (infoPedido.getString("status_pedido").equals("cancelado"))
                                                            break;
                                                        i++;
                                                    }while(true);

                                                    infoPedido = (JSONObject) consultaArray.getJSONArray("pedidos").get(i);
                                                    cod_pedido = infoPedido.getString("cod_pedido");
                                                    hora_pedido = infoPedido.getString("hora_pedido");
                                                    nome_mercado = infoPedido.getString("nome_mercado");
                                                    processStartCancelNotification();
                                                }

                                                cont_processamento =0;
                                                cont_pronto =0;
                                                cont_total=0;




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
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("PEDIDO PRONTO!")
            .setAutoCancel(true)
            .setContentText(nome_mercado)
            .setSmallIcon(R.drawable.carrinho2);

        final Intent mainIntent = new Intent(this, NotificacaoAntecedencia.class);
        mainIntent.putExtra("cod_pedido", cod_pedido);
        mainIntent.putExtra("hora_pedido", hora_pedido);
        mainIntent.putExtra("nome_mercado", nome_mercado);



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

    private void processStartCancelNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("PEDIDO CANCELADO!")
                .setAutoCancel(true)
                .setContentText(nome_mercado)
                .setSmallIcon(R.drawable.carrinho2);

        final Intent mainIntent = new Intent(this, NotificacaoCancelamento.class);
        mainIntent.putExtra("cod_pedido", cod_pedido);
        mainIntent.putExtra("hora_pedido", hora_pedido);
        mainIntent.putExtra("nome_mercado", nome_mercado);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID+1,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.vibrate = new long[]{100, 250, 100, 500};

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID+1, notification);
    }
}
