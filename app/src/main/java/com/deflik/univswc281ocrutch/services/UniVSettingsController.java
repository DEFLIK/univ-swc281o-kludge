package com.deflik.univswc281ocrutch.services;

import android.car.CarNotConnectedException;
import android.car.VehiclePropertyIds;
import android.car.hardware.cabin.CarCabinManager;
import android.car.hardware.property.CarPropertyManager;
import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.deflik.univswc281ocrutch.models.UniVMCUOptionIds;
import com.deflik.univswc281ocrutch.models.UniVSettingKeys;
import com.deflik.univswc281ocrutch.services.decompiled.CollectionService;
import com.deflik.univswc281ocrutch.services.decompiled.PersonalCenterManager;

import java.util.Arrays;

public class UniVSettingsController {
    private final static int PropertiesGlobalAreaId = 1048576;
    private final static String SharedPrefAppName = "univkludge_settings";
    private final Context appContext;
    private final CarCabinManager uniVCabinManager;
    private final CarPropertyManager uniVPropertyManager;
    private UniVEngineStateListener engineStateListener;
    private IBinder collectBinder;
    private IBinder personalBinder;

    public UniVSettingsController(
            Context appContext,
            CarCabinManager uniVCabinManager,
            CarPropertyManager uniVPropertyManager) {
        this.appContext = appContext;
        this.uniVCabinManager = uniVCabinManager;
        this.uniVPropertyManager = uniVPropertyManager;

        try {
            engineStateListener = new UniVEngineStateListener(
                    this::activateMcuPropsOnStart,
                    this::saveMcuPropsStateOnShutdown);
            uniVPropertyManager.registerListener(
                    engineStateListener,
                    VehiclePropertyIds.IGNITION_STATE,
                    0.0f);
        } catch (Exception | Error e) {
            AppLog.e("failed to register engine state listener " + e);
        }
    }

    public void dispose() {
        AppLog.i("disposing univ settings controller");
        uniVPropertyManager.unregisterListener(engineStateListener);
    }
    public void setCollectBinder(IBinder collectBinder)
    {
        this.collectBinder = collectBinder;
    }

    public void setPersonalBinder(IBinder personalBinder) {
        this.personalBinder = personalBinder;
    }

    public void onKeyEvent(int keyCode) {
        if (keyCode == 1005)
            tryChangeExhaustState();
    }

    public void saveAppSettingState(UniVSettingKeys settingKey, boolean value) {
        try {
            AppLog.i("saving setting value " + settingKey.getKeyString() + "=" + value);
            var sharedPref = appContext.getSharedPreferences(SharedPrefAppName, Context.MODE_PRIVATE);
            sharedPref.edit().putBoolean(settingKey.getKeyString(), value).apply();
        } catch (Exception | Error e) {
            AppLog.e("failed to set app setting value " + settingKey + " " + e);
        }
    }

    public boolean getAppSettingState(UniVSettingKeys settingKey) {
        try {
            var sharedPref = appContext.getSharedPreferences(SharedPrefAppName, Context.MODE_PRIVATE);
            var value = sharedPref.getBoolean(settingKey.getKeyString(), false);
            AppLog.i("obtained setting value " + settingKey.getKeyString() + "=" + value);

            return value;
        } catch (Exception | Error e) {
            AppLog.e("failed obtaining app setting value " + settingKey + " " + e);
            return false;
        }
    }

    public boolean tryChangeExhaustState() {
        if (!isInitialized())
            return false;

        try {
            var props = uniVCabinManager.getPropertyList();
            var exProp = props.stream().filter(x -> x.getPropertyId() == UniVMCUOptionIds.EXHAUST_NOISE).findFirst().orElse(null);
            if (exProp == null) {
                AppLog.e("EXHAUST prop not found");
                return false;
            }
            AppLog.i("found EXHAUST prop: " + exProp);

            var currVal = uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, PropertiesGlobalAreaId);
            AppLog.i("EXHAUST prop value BEFORE set: " + currVal);

            if (currVal == 0)
                uniVCabinManager.setIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, PropertiesGlobalAreaId, 2);
            else
                uniVCabinManager.setIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, PropertiesGlobalAreaId, 1);

            AppLog.i("EXHAUST prop value AFTER set: " + uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, PropertiesGlobalAreaId));
        } catch (Exception | Error e) {
            AppLog.e("EXHAUST value set failed" + e);
            return false;
        }

        return true;
    }

    public boolean tryDisableCustomButtonLauncherIteration() {
        return trySaveInternalUniVData(
                "589824",
                "0x1009",
                "1406",
                9,
                "CSTM DISABLE");
    }

    public boolean tryRestoreCustomButtonLauncherIteration() {
        return trySaveInternalUniVData(
            "589824",
            "0x1003",
            "1406",
            4,
            "CSTM MUTE");
    }

    public boolean tryDisableStartStop() {
        try {
            var props = uniVCabinManager.getPropertyList();
            var ssProp = props.stream().filter(x -> x.getPropertyId() == UniVMCUOptionIds.START_STOP).findFirst().orElse(null);
            if (ssProp == null) {
                AppLog.e("STARTSTOP prop not found");
                return false;
            }
            AppLog.i("found STARTSTOP prop: " + ssProp);

            var currVal = uniVCabinManager.getIntProperty(UniVMCUOptionIds.START_STOP, PropertiesGlobalAreaId);
            AppLog.i("STARTSTOP prop value BEFORE set: " + currVal);

            uniVCabinManager.setIntProperty(UniVMCUOptionIds.START_STOP, PropertiesGlobalAreaId, 1);
            AppLog.i("STARTSTOP prop value AFTER set: " + uniVCabinManager.getIntProperty(UniVMCUOptionIds.START_STOP, PropertiesGlobalAreaId));
            return true;
        } catch (Exception e) {
            AppLog.i("failed to disable StartStop: " + e);
            return false;
        }
    }

    public boolean trySaveInternalUniVData(
            String personalServiceDataLocationId,
            String personalServiceDataValue,
            String collectServiceDataLocationId,
            int collectServiceDataValue,
            String DataNameLog
            ) {
        if (!isInitialized())
            return false;

        try {
            PersonalCenterManager.saveOtherAppInfoByOther(personalBinder, 3, personalServiceDataLocationId, "", personalServiceDataValue);
            AppLog.i("set other app info SUCCESS " + DataNameLog);
        } catch (RemoteException e) {
            AppLog.e("set other app info FAILED " + DataNameLog + ": " + e);
            return false;
        }
        try {
            CollectionService.uploadData(collectBinder, collectServiceDataLocationId, collectServiceDataValue);
            AppLog.i("upload data SUCCESS: " + DataNameLog);
        } catch (RemoteException e) {
            AppLog.e("upload data FAILED " + DataNameLog + ": " + e);
            return false;
        }

        return true;
    }

    private void activateMcuPropsOnStart() {
        if (getAppSettingState(UniVSettingKeys.START_STOP_OPTION_KEY))
            tryDisableStartStop();
    }

    private void saveMcuPropsStateOnShutdown() {
        try { // todo
            var propValue = uniVCabinManager.getIntProperty(UniVMCUOptionIds.AUTO_HOLD, PropertiesGlobalAreaId);
            var propValue2 = uniVCabinManager.getIntProperty(UniVMCUOptionIds.AUTO_HOLD, 524288);
            AppLog.i("autohold prop value on shut " + propValue);
            AppLog.i("autohold prop value on shut 2 " + propValue2);

            var arrPropVal = uniVCabinManager.getIntArrayProperty(UniVMCUOptionIds.AUTO_HOLD, 16777216);
            AppLog.i("autohold ARR prop value on shut " + Arrays.toString(arrPropVal));
        } catch (CarNotConnectedException e) {
            AppLog.e("failed to save autohold value on shut " + e);
        }
    }

    private boolean isInitialized() {
        var isBindersCreated = collectBinder != null && personalBinder != null;
        if (!isBindersCreated)
            AppLog.e("some of binders are not created yet:"
                    + "\n\tcollect binder is null: " + (collectBinder == null)
                    + "\n\tpersonal binder is null: " + (personalBinder == null)
            );

        return isBindersCreated;
    }
}
