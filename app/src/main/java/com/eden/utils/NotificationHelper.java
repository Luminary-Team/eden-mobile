package com.eden.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.eden.R;
import com.eden.ui.activities.MainActivity;

import java.util.Calendar;

public class NotificationHelper {

    public static final String CHANNEL_ID = "my_channel_id";
    public static final String CHANNEL_NAME = "My Channel";

    public static void createNotificationChannel(Context context) {
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    public static void sendRandomNotification(Context context) {
        int notificationId = (int) System.currentTimeMillis();

        String[] actions = {
                "Encontre eletrônicos como novos, com preços incríveis!",
                "Economize com produtos de qualidade e garantia, sem pesar no bolso!",
                "Precisa de um upgrade? Veja nossos eletrônicos mais vendidos e encontre o seu próximo gadget com desconto!",
                "Descubra as últimas novidades em tecnologia!",
                "Encontre o gadget perfeito para você!",
                "A tecnologia que você merece, agora ao seu alcance!",
                "Saiba tudo sobre o futuro da tecnologia!",
                "Notícias em tempo real! Fique por dentro do mundo tech.",
                "Análises exclusivas! Entenda o impacto da tecnologia em sua vida.",
                "Tendências em tecnologia! O que você precisa saber."
        };

        String action = actions[(int) (Math.random() * actions.length)];

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Builds the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.eden_logotipo_2)
                .setContentTitle("Eden")
                .setContentText(action)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        // Sends the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(notificationId, builder.build());
        }
    }

    public void agendarNotificacaoDiaria(Context context) {
        // Defina a hora para exibir a notificação
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Se a hora definida já passou no dia atual, agendar para o próximo dia
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Intent para o BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agendar o alarme
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Repetir o alarme uma vez por dia
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void agendarNotificacaoHoraEmHora(Context context) {
        // Defina o horário inicial para o próximo início de hora
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Se a hora já passou, configurar para o próximo início de hora
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        }

        // Intent para o BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Agendar o alarme
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Agendar alarme exato para o próximo início de hora
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}