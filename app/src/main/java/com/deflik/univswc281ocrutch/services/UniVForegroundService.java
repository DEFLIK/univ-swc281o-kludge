//package com.deflik.univswc281ocrutch.services;
//
//import static android.app.NotificationManager.IMPORTANCE_LOW;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.ServiceInfo;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//
//import com.deflik.univswc281ocrutch.infrastructure.AppLog;
//import com.wt.vehiclesetting.R;
//
//public class UniVForegroundService extends Service {
//    private static final String ChannelId = "univkludge_foreground_service_channel";
//    private static final int NotificationId = 1499;
//    private UniVServiceConnection uniVServiceConnection;
//
//    public UniVForegroundService() {
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        AppLog.i("onCreate foreground");
//
//        try {
//            if (Build.VERSION.SDK_INT >= 26) {
//                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
//                        .createNotificationChannel(new NotificationChannel(ChannelId, "UniVKludge foreground service", IMPORTANCE_LOW));
//                startForeground(1, new Notification.Builder(this, ChannelId).build());
//            }
//        } catch (Exception e) {
//            Log.i("UniVKludge", "err foreground " + e); // todo
//        }
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent == null)
//            return flags;
//
//        AppLog.i("onStartCommand foreground");
//        return START_STICKY;
////        var appContext = getApplicationContext();
////        UniVServiceConnection
////            .getSingletonInstance(appContext)
////            .connectToUniV(UniVForegroundService.class.getName())
////            .onEstablishedConnection(UniVSettingsController::refreshSettings);
////
////        return START_STICKY;
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    private void createNotificationChannel() {
//        NotificationChannel channel = new NotificationChannel(
//                ChannelId, "UniVKludge foreground service", IMPORTANCE_LOW);
//        NotificationManager manager = getSystemService(NotificationManager.class);
//        if (manager != null) {
//            manager.createNotificationChannel(channel);
//        }
//    }
//}