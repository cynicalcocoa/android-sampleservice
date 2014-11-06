package com.hatebyte.cynicalandroid.sampleservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by scott on 11/5/14.
 */
public class SampleService extends Service {
    private static final String TAG = "SampleService";
    private NotificationManager notificationManager;
    private int NOTIFICATION_SERVICE_STARTED = R.string.service_notification_title_start;
    private int NOTIFICATION_SERVICE_STOPPED = R.string.service_notification_title_stop;
    public int connectionStatus = R.string.service_toast_disconnected;

    public class LocalBinder extends Binder {
        SampleService getService() {
            return SampleService.this;
        }
   }

    private final LocalBinder binder = new LocalBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        connectionStatus= R.string.service_toast_connected;
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showCreateNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Received startId "+startId + " : with flags " + flags);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        connectionStatus = R.string.service_toast_disconnected;
        showDestroyNotification();
   }

    private void showDestroyNotification() {
        notificationManager.cancel(NOTIFICATION_SERVICE_STARTED);

        Notification notify = getNotificatiom(
                getText(R.string.service_notification_title_stop),
                getText(R.string.service_ondestroy_fired),
                getText(R.string.service_toast_stopped)
        );
        notificationManager.notify(NOTIFICATION_SERVICE_STOPPED, notify);

        Toast.makeText(getApplicationContext(), R.string.service_toast_stopped, Toast.LENGTH_SHORT).show();
        // SHOULD SAY 'Local Service Stopped'
    }

    private void showCreateNotification() {
        notificationManager.cancel(NOTIFICATION_SERVICE_STOPPED);

        Notification notify = getNotificatiom(
                getText(R.string.service_notification_title_start),
                getText(R.string.service_oncreate_fired),
                getText(R.string.service_toast_started)
        );
        notificationManager.notify(NOTIFICATION_SERVICE_STARTED, notify);

        Toast.makeText(getApplicationContext(), R.string.service_toast_started, Toast.LENGTH_SHORT).show();
        // SHOULD SAY 'Local Service Started'
    }

    private Notification getNotificatiom(CharSequence ticker, CharSequence title, CharSequence text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setAutoCancel(true);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), 0);

        builder.setContentIntent(contentIntent);
        return builder.build();
    }

}
