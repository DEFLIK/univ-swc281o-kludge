package com.deflik.univswc281ocrutch.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wt.vehiclesetting.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Привязываем созданный XML-файл
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Вся логика, которая была в Activity, переносится сюда.
        // Пример поиска кнопки:
        // Button myButton = view.findViewById(R.id.myButton);
    }
}