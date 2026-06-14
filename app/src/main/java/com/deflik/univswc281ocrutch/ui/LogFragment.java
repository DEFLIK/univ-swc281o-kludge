package com.deflik.univswc281ocrutch.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.wt.vehiclesetting.R;

public class LogFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        var logOutput = (TextView) view.findViewById(R.id.logOutput);
        AppLog.bindTextView(logOutput);
    }

    @Override
    public void onDestroyView() {
        AppLog.unbindTextView();
        super.onDestroyView();
    }
}