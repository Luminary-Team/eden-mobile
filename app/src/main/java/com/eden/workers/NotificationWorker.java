package com.eden.workers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.eden.utils.NotificationHelper;

import java.util.Calendar;
import java.util.Random;

public class NotificationWorker {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    NotificationWorker(Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationHelper.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void scheduleRandomAlarm(Context context) {
        // Check if the app can schedule exact alarms
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            if (!alarmMgr.canScheduleExactAlarms()) {
                // Notify the user to allow exact alarms
                Toast.makeText(context, "Please allow exact alarms in app settings.", Toast.LENGTH_LONG).show();
                // Optionally, redirect the user to the settings
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                return;
            }
        }

        Random random = new Random();
        int hour = 10 + random.nextInt(13); // Gera um número entre 10 e 22
        int minute = random.nextInt(60); // Gera um número entre 0 e 59

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Se o horário já passou, agenda para o próximo dia
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Agenda o alarme
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public void cancelAlarm(Context context) {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
    }

}
