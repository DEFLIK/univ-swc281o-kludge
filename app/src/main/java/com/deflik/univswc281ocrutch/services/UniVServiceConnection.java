package com.deflik.univswc281ocrutch.services;

import android.car.Car;
import android.car.hardware.cabin.CarCabinManager;
import android.car.hardware.property.CarPropertyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class UniVServiceConnection implements ServiceConnection {

    private static final String CollectServiceDescriptor = "com.incall.apps.start.CollectService";
    private static final String PersonalServiceDescriptor = "com.incall.apps.start.PersonalService";
    private static final String ChanganCommonPackageName = "com.incall.apps.commonservice";

    private static UniVServiceConnection singletonInstance;
    private Context appContext;
    private UniVSettingsController controller;
    private Car uniV;
    private HashMap<String, Consumer<UniVSettingsController>> onEstablishedConnectionSubscriptions = new HashMap<>();
    private HashMap<String, Runnable> onConnectionClosedSubscriptions = new HashMap<>();

    private ServiceConnection personalConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller.setPersonalBinder(service);
            AppLog.i("PersonalService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppLog.i("PersonalService disconnected");
            controller.setPersonalBinder(null);
        }
    };

    private ServiceConnection collectConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            controller.setCollectBinder(service);
            AppLog.i("CollectService connected");

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AppLog.i("CollectService disconnected");
            controller.setCollectBinder(null);
        }
    };

    private UniVServiceConnection(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        try {
            AppLog.i("car connection received");
            var uniVCabinManager = (CarCabinManager) uniV.getCarManager(Car.CABIN_SERVICE);
            var uniVPropertyManager = (CarPropertyManager) uniV.getCarManager(Car.PROPERTY_SERVICE);
            AppLog.i("managers created");

            bindServices();

            controller = new UniVSettingsController(appContext, uniVCabinManager, uniVPropertyManager);

            onEstablishedConnectionSubscriptions.values().forEach(sub -> sub.accept(controller));
        } catch (Exception | Error e) {
            AppLog.e("univ connection exc" + e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        AppLog.i("univ connection onServiceDisconnected called");
        dispose();
    }

    public UniVServiceConnection onEstablishedConnection(String subscriberName,  Consumer<UniVSettingsController> action) {
        if (controller != null)
            action.accept(controller);

        onEstablishedConnectionSubscriptions.put(subscriberName, action);
        return this;
    }

    public void onConnectionClosed(String subscriberName, Runnable action) {
        onConnectionClosedSubscriptions.put(subscriberName, action);
    }

    public void unsubscribeConnectionListeners(String subscriberName) {
        onEstablishedConnectionSubscriptions.remove(subscriberName);
        onConnectionClosedSubscriptions.remove(subscriberName);
    }

    public static UniVServiceConnection getSingletonInstance(Context appContext) {
        if (singletonInstance == null)
            singletonInstance = new UniVServiceConnection(appContext);

        return singletonInstance;
    }

    public UniVSettingsController getSettingsController() {
        return controller;
    }

    public UniVServiceConnection connectToUniV(String initiatorName) {
        if (uniV != null) {
            AppLog.i("univ already connected or in connection from " + initiatorName);
            return this;
        }
        AppLog.i("starting univ new connection " + initiatorName);

        var uniV = Car.createCar(appContext, this);
        this.uniV = uniV;
        AppLog.i("univ car class created, performing connection..." );

        try {
            uniV.connect();
            AppLog.i("univ car connection started...");
        } catch (Exception | Error e) {
            AppLog.e("univ car connection exc " + e);
        }

        return this;
    }

    public static void disconnect() {
        AppLog.i("univ connection disconnect called");
        if (singletonInstance == null)
            return;
        singletonInstance.uniV.disconnect();
    }

    private void dispose() {
        AppLog.i("disposing connection singleton instance");
        onConnectionClosedSubscriptions.values().forEach(Runnable::run);
        onConnectionClosedSubscriptions = null;
        onEstablishedConnectionSubscriptions = null;
        controller.dispose();
        unbindServices();
        singletonInstance = null;
        appContext = null;
    }

    private void bindServices() {
        AppLog.i("binding to incall internal services");
        try {
            var collectServiceIntent = new Intent(CollectServiceDescriptor);
            collectServiceIntent.setPackage(ChanganCommonPackageName);
            appContext.bindService(collectServiceIntent, collectConnection, 1);
        } catch (Exception | Error e) {
            AppLog.e("failed to bind CollectService "+ e);
        }

        try {
            var personalServiceIntent = new Intent(PersonalServiceDescriptor);
            personalServiceIntent.setPackage(ChanganCommonPackageName);
            appContext.bindService(personalServiceIntent, personalConnection, 1);
        } catch (Exception | Error e) {
            AppLog.e("failed to bind PersonalService "+ e);
        }
    }

    private void unbindServices() {
        appContext.unbindService(collectConnection);
        appContext.unbindService(personalConnection);
        personalConnection = null;
        collectConnection = null;
    }
}
