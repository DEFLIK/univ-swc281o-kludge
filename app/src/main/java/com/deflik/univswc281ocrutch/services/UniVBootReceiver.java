//package com.deflik.univswc281ocrutch.services;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//
//import com.deflik.univswc281ocrutch.infrastructure.AppLog;
//
//public class UniVBootReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        try {
//            var action = intent.getAction();
//
//            AppLog.i("received broadcast intent " + action);
//            if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
//                AppLog.i("initializing boot logic");
////
////                var serviceIntent = new Intent(context, UniVForegroundService.class);
////                context.startForegroundService(serviceIntent);
//            }
//        } catch (Exception e) {
//            AppLog.e("failed to create univ connection from BootReceiver: " + e);
//        }
//    }
//}
