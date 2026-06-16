package com.deflik.univswc281ocrutch.services;

import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;

public class UniVEngineStateListener implements CarPropertyManager.CarPropertyEventListener {
    private final Runnable onStartAction;
    private final Runnable onShutdownAction;
    private int prevState = -1;

    public UniVEngineStateListener(
        Runnable onStartAction,
        Runnable onShutdownAction) {
        this.onStartAction = onStartAction;
        this.onShutdownAction = onShutdownAction;
    }

    @Override
    public void onChangeEvent(CarPropertyValue carPropertyValue) {
        try {
            var currEngineState = (int)carPropertyValue.getValue();
            AppLog.i("engine state changed" + currEngineState);

            if (prevState == 5 && currEngineState == 4) {
                AppLog.i("engine in RUNNING state");
                onStartAction.run();
            }

            if (prevState == 3 && currEngineState == 2) {
                AppLog.i("engine in SHUTDOWN state");
                onShutdownAction.run();
            }

            prevState = currEngineState;
        } catch (Exception | Error e) {
            AppLog.e("failed to read engine state, resetting prev value " + e);
            prevState = -1;
        }
    }

    @Override
    public void onErrorEvent(int i, int i1) {}
}
