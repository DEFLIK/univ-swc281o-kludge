package com.deflik.univswc281ocrutch.ui;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.deflik.univswc281ocrutch.models.UniVSettingKeys;
import com.deflik.univswc281ocrutch.services.UniVServiceConnection;
import com.deflik.univswc281ocrutch.services.UniVSettingsController;
import com.wt.vehiclesetting.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLog.i("onViewCreated SettingsFragment");

        UniVServiceConnection
            .getSingletonInstance(requireContext().getApplicationContext())
            .onEstablishedConnection(settingsController -> {
                bindSettingViewToController(view, settingsController);
                view.findViewById(R.id.connection_loader).setVisibility(GONE);
            }).onConnectionClosed(() -> view.findViewById(R.id.connection_loader).setVisibility(VISIBLE));
    }

    public void bindSettingViewToController(View view, UniVSettingsController settingsController) {
        var startStopSettingSavedState = settingsController.getAppSettingState(UniVSettingKeys.START_STOP_OPTION_KEY);
        var exhaustControlSettingSavedState = settingsController.getAppSettingState(UniVSettingKeys.EXHAUST_CONTROL_OPTION_KEY);

        var startStopSwitch = (SwitchCompat) view.findViewById(R.id.startStopOption).findViewById(R.id.card_switch);
        startStopSwitch.setChecked(startStopSettingSavedState);
        startStopSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            AppLog.i("start stop option state = " + startStopSwitch.isChecked());
            settingsController.saveAppSettingState(UniVSettingKeys.START_STOP_OPTION_KEY, isChecked);
        });

        var exhaustSwitch = (SwitchCompat) view.findViewById(R.id.exhaustOption).findViewById(R.id.card_switch);
        exhaustSwitch.setChecked(exhaustControlSettingSavedState);
        exhaustSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            AppLog.i("exhaust option state = " + exhaustSwitch.isChecked());
            if (isChecked) {
                settingsController.saveAppSettingState(UniVSettingKeys.EXHAUST_CONTROL_OPTION_KEY, true);
                settingsController.tryDisableCustomButtonLauncherIteration();
            } else {
                settingsController.saveAppSettingState(UniVSettingKeys.EXHAUST_CONTROL_OPTION_KEY, false);
                settingsController.tryRestoreCustomButtonLauncherIteration();
            }
        });
    }
}