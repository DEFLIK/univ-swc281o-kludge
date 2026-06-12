package com.deflik.univswc281ocrutch.services;

import android.car.Car;
import android.car.hardware.cabin.CarCabinManager;
import android.car.hardware.property.CarPropertyManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.deflik.univswc281ocrutch.infrastructure.Constants;

public class UniVServiceConnection implements ServiceConnection {
    private Car uniV;
    private final UniVSettingsController controller;

    public UniVServiceConnection(AppCompatActivity mainActivity) {
        controller = new UniVSettingsController(mainActivity);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            Log.i(Constants.LOG_TAG, "univcrutch connection received");
            var uniVCabinManager = (CarCabinManager) uniV.getCarManager(Car.CABIN_SERVICE);
            var uniVPropertyManager = (CarPropertyManager) uniV.getCarManager(Car.PROPERTY_SERVICE);
            Log.i(Constants.LOG_TAG, "univcrutch manager created");
            controller.RegisterCabinManager(uniVCabinManager, uniVPropertyManager);

//            var propsList = uniVCabinManager.getPropertyList();
//            Log.i("I", "univcrutch props obtained");
//            for (var prop : propsList) {
//                Log.i("univcrutch props", prop.toString());
//            }
        } catch (Exception e) {
            Log.i(Constants.LOG_TAG, "univ connection exception" + e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //todo
    }

    public void bindCarBeforeConnection(Car uniV) {
        this.uniV = uniV;
    }
}
