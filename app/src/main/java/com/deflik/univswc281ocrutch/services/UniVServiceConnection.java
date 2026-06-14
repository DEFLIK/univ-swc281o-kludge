package com.deflik.univswc281ocrutch.services;

import static android.car.Car.CAR_INPUTKEY_SERVICE;

import android.car.Car;
import android.car.hardware.cabin.CarCabinManager;
import android.car.hardware.property.CarPropertyManager;
import android.car.input.CarInputManager;
import android.car.input.InputFilter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

//import com.deflik.univswc281ocrutch.infrastructure.Constants;
import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.deflik.univswc281ocrutch.services.decompiled.CollectionService;
import com.deflik.univswc281ocrutch.services.decompiled.PersonalCenterManager;
import com.google.gson.JsonObject;
import com.wt.vehiclesetting.R;

public class UniVServiceConnection implements ServiceConnection {

    private static final String CollectServiceDescriptor = "com.incall.apps.start.CollectService";
    private static final String PersonalServiceDescriptor = "com.incall.apps.start.PersonalService";
    private static final String ChanganCommonPackageName = "com.incall.apps.commonservice";

    public final UniVSettingsController controller;
    private Car uniV;
    private final AppCompatActivity mainActivity;
    private CarInputManager.CarInputEventCallback callback;
    private IBinder personalBinder;
    private IBinder collectBinder;

    private final ServiceConnection personalConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            personalBinder = service;
            AppLog.i("PersonalService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            personalBinder = null;
            AppLog.i( "PersonalService disconnected");
        }
    };

    private final ServiceConnection collectConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            collectBinder = service;
            AppLog.i("CollectService connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            collectBinder = null;
            AppLog.i("CollectService disconnected");
        }
    };

    public UniVServiceConnection(AppCompatActivity mainActivity) {
        controller = new UniVSettingsController(mainActivity);
        this.mainActivity = mainActivity;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        try {
            AppLog.i("car connection received");
            var uniVCabinManager = (CarCabinManager) uniV.getCarManager(Car.CABIN_SERVICE);
            var uniVPropertyManager = (CarPropertyManager) uniV.getCarManager(Car.PROPERTY_SERVICE);
            var inputManager = (CarInputManager) uniV.getCarManager(CAR_INPUTKEY_SERVICE);
            AppLog.i("managers created");

            callback = (event, in) -> {
                AppLog.i("keymanager event " + event + " " + in);
                if (event.getKeyCode() == 1005) {
                    AppLog.i("keypressed in manager 1005");
                }
            };
            var filters = new InputFilter[]{
                new InputFilter(1005, Display.DEFAULT_DISPLAY)
            };
            try {
                inputManager.registerCallback(callback, filters);
            } catch (NoSuchMethodError e) {
                AppLog.e("failed to register keyListener. running on emulator? " + e);
            }

            bindServices();

            //todo rm
            mainActivity.findViewById(R.id.setUnk).setOnClickListener(view -> {
//                Log.i(Constants.LOG_TAG, "click setUnknown");
                try {
                    PersonalCenterManager.saveOtherAppInfoByOther(personalBinder, 3, "589824", "", "0x1009");
                } catch (RemoteException e) {
//                    Log.i(Constants.LOG_TAG, "saveOtherApp UNK exc" + e);
                }
                try {
                    CollectionService.uploadData(collectBinder, "1406", 9);
                } catch (RemoteException e) {
//                    Log.i(Constants.LOG_TAG, "upload UNK exc" + e);
                }
            });

            // todo rm
            mainActivity.findViewById(R.id.setMute).setOnClickListener(view -> {
//                Log.i(Constants.LOG_TAG, "click setMute");
                try {
                    PersonalCenterManager.saveOtherAppInfoByOther(personalBinder, 3, "589824", "", "0x1003");
                } catch (RemoteException e) {
//                    Log.i(Constants.LOG_TAG, "saveOtherApp MUTE exc" + e);
                }
                try {
                    CollectionService.uploadData(collectBinder, "1406", 4);
                } catch (RemoteException e) {
//                    Log.i(Constants.LOG_TAG, "upload MUTE exc" + e);
                }
            });


            controller.RegisterManagers(uniVCabinManager, uniVPropertyManager);
        } catch (Exception e) {
            AppLog.e("univ connection exc" + e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //todo
    }

    public void bindCarBeforeConnection(Car uniV) {
        this.uniV = uniV;
    }

    private void bindServices() {
        try {
            var collectServiceIntent = new Intent(CollectServiceDescriptor);
            collectServiceIntent.setPackage(ChanganCommonPackageName);
            mainActivity.getApplicationContext().bindService(collectServiceIntent, collectConnection, 1);
        } catch (Exception e) {
            AppLog.e("failed to bind CollectService "+ e);
        }

        try {
            var personalServiceIntent = new Intent(PersonalServiceDescriptor);
            personalServiceIntent.setPackage(ChanganCommonPackageName);
            mainActivity.getApplicationContext().bindService(personalServiceIntent, personalConnection, 1);
        } catch (Exception e) {
            AppLog.e("failed to bind PersonalService "+ e);
        }
    }
}
