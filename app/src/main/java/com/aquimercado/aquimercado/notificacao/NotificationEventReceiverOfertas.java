package com.aquimercado.aquimercado.notificacao;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by rafael on 23/08/16.
 */

public class NotificationEventReceiverOfertas extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);

        long milisegundos_alarme = 0; //variavel auxiliar que leva os milisegundos do calendário

        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 17);
        c.set(Calendar.SECOND, 15);

        //solução aqui:
        if(c.getTimeInMillis() <= now.getTimeInMillis())
            milisegundos_alarme = c.getTimeInMillis() + (AlarmManager.INTERVAL_DAY+1);
        else
            milisegundos_alarme = c.getTimeInMillis();
        //fim solução
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, milisegundos_alarme, 1000*60*60*24, alarmIntent); //repete em 24 horas depois

        //notificacao
        /*alarmManager.setInexactRepeating (AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+60000,
                60000,
                alarmIntent);*/

        Log.d("setupalerm","OK");
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.cancel(alarmIntent);
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, NotificationEventReceiverOfertas.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        Log.d("pendent=","ok");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;

        Log.d("serviceStart","OK");
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            serviceIntent = NotificationIntentServiceOfertas.createIntentStartNotificationService(context);
            Log.d("serviceStart","OK");
        }

        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, serviceIntent);
        }
    }
}
