package com.deflik.univswc281ocrutch.services;

import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.cabin.CarCabinManager;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.deflik.univswc281ocrutch.infrastructure.Constants;
import com.deflik.univswc281ocrutch.models.UniVMCUOptionIds;
import com.wt.vehiclesetting.R;

public class UniVSettingsController {

    private final AppCompatActivity activity;

    public UniVSettingsController(AppCompatActivity mainActivity) {
        this.activity = mainActivity;
    }

    public void RegisterCabinManager(CarCabinManager uniVCabinManager, CarPropertyManager uniVPropertyManager) {
//        TextView textView = activity.findViewById(R.id.textView);
//        activity.runOnUiThread(() -> textView.append("started "));
//
//
//        Button buttonOpen = activity.findViewById(R.id.exhaustOpenButton);
//        buttonOpen.setOnClickListener(view -> {
//            activity.runOnUiThread(() -> textView.append("open "));
//            try {
//                var props = uniVCabinManager.getPropertyList();
////                for (var prop : props)
////                    Log.i(Constants.LOG_TAG, "p: " + prop);
//
//                var exProp = props.stream().filter(x -> x.getPropertyId() == UniVMCUOptionIds.EXHAUST_NOISE).findFirst().orElse(null);
//                if (exProp != null) {
//                    Log.i(Constants.LOG_TAG, "found EXHAUST prop: " + exProp);
//                    Log.i(Constants.LOG_TAG, "get ex int area 0" + uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 0));
//                    Log.i(Constants.LOG_TAG, "get ex int area 1048576" + uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 1048576));
////                    Log.i(Constants.LOG_TAG, "get ex string" + uniVCabinManager.getStringProperty(UniVMCUOptionIds.EXHAUST_NOISE, 0));
//                }
//
////                var spProp = props.stream().filter(x -> x.getPropertyId() == UniVMCUOptionIds.SPOILER).findFirst().orElse(null);
////                if (spProp != null) {
////                    Log.i(Constants.LOG_TAG, "found SPOILER prop: " + spProp);
////                    Log.i(Constants.LOG_TAG, "get sp int" + uniVCabinManager.getIntProperty(UniVMCUOptionIds.SPOILER, 0));
////                    Log.i(Constants.LOG_TAG, "get sp string" + uniVCabinManager.getStringProperty(UniVMCUOptionIds.SPOILER, 0));
////                }
//
//
//                Log.i(Constants.LOG_TAG, "univcrutch set open");
//                uniVCabinManager.setIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 1048576, 2);
//                Log.i(Constants.LOG_TAG, "get ex int after open area 0" + uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 0));
//                Log.i(Constants.LOG_TAG, "get ex int after open area 1048576" + uniVCabinManager.getIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 1048576));
//            } catch (Exception e) {
//                Log.i(Constants.LOG_TAG, "univcrutch set open FAILED" + e);
//                throw new RuntimeException(e);
//            }
//
//        });
//
//        Button buttonClose = activity.findViewById(R.id.exhaustCloseButton);
//        buttonClose.setOnClickListener(view -> {
//            activity.runOnUiThread(() -> textView.append("close "));
//            try {
//                Log.i(Constants.LOG_TAG, "univcrutch set close");
//                uniVCabinManager.setIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 1048576, 1);
//            } catch (CarNotConnectedException e) {
//                Log.i(Constants.LOG_TAG, "univcrutch set close FAILED" + e);
//                throw new RuntimeException(e);
//            }
//        });
//
//        Button buttonOpen2 = activity.findViewById(R.id.exhaustOpenButton2);
//        buttonOpen2.setOnClickListener(view -> {
//            activity.runOnUiThread(() -> textView.append("open "));
//            try {
//                Log.i(Constants.LOG_TAG, "univcrutch set open");
//                uniVPropertyManager.setIntProperty(557846434, 1048576, 1);
//            } catch (CarNotConnectedException e) {
//                Log.i(Constants.LOG_TAG, "univcrutch set open FAILED" + e);
//                throw new RuntimeException(e);
//            }
//        });
////        Button buttonClose2 = activity.findViewById(R.id.exhaustCloseButton2);
////        buttonClose2.setOnClickListener(view -> {
////            activity.runOnUiThread(() -> textView.append("close prop "));
////            try {
////                Log.i(Constants.LOG_TAG, "univcrutch set close prop");
////                uniVPropertyManager.setIntProperty(UniVMCUOptionIds.EXHAUST_NOISE, 0, 1);
////            } catch (CarNotConnectedException e) {
////                Log.i(Constants.LOG_TAG, "univcrutch set close prop FAILED" + e);
////                throw new RuntimeException(e);
////            }
////        });
    }

    public void UnregisterCabinManager() {
        // todo
    }
}
