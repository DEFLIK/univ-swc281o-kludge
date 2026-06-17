package com.deflik.univswc281ocrutch.services;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;

import java.util.Arrays;

public class UniVAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AppLog.i("accessibility service connected");


        UniVServiceConnection
            .getSingletonInstance(getApplicationContext())
            .connectToUniV(UniVAccessibilityService.class.getName());

//        var prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isFirstRun = prefs.getBoolean(FirstRunPreferenceKey, true);
//        if (isFirstRun) {
//            prefs.edit().putBoolean(FirstRunPreferenceKey, false).apply();
//
//            Intent intent = new Intent(this, MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
    }

    public static boolean isAccessibilityEnabled(Context context) {
        String expectedComponentName = context.getPackageName() + "/" + UniVAccessibilityService.class.getName();

        String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);

        if (enabledServicesSetting == null)
            return false;

        return Arrays
            .asList(enabledServicesSetting
                .replace(" ", "")
                .toLowerCase()
                .split(":"))
            .contains(expectedComponentName.toLowerCase());
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        try {
            int keyCode = event.getKeyCode();
            AppLog.i("keycode event catch" + keyCode);
            if (keyCode == 1005) {
                AppLog.i("keycode observable event catch" + keyCode);
                var ctrl = UniVServiceConnection.getSingletonInstance(getApplicationContext()).getSettingsController();
                if (ctrl != null)
                    ctrl.onKeyEvent(keyCode);
                return super.onKeyEvent(event);
            }
        } catch (Exception | Error e) {
            AppLog.e("error on accessibilityService keyEvent");
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        AppLog.i("accessibility service onDestroy");
        try {
            UniVServiceConnection.disconnect();
        } catch (Exception | Error e) {
            AppLog.i("failed disconnecting uni v from accessibilityService " + e);
        }
        super.onDestroy();
    }
}
