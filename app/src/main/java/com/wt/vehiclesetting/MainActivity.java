package com.wt.vehiclesetting;

import android.car.Car;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.deflik.univswc281ocrutch.infrastructure.Constants;
import com.deflik.univswc281ocrutch.services.UniVServiceConnection;
import com.deflik.univswc281ocrutch.ui.MainViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(Constants.LOG_TAG, "univcrutch started");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var uniVServiceConnection = new UniVServiceConnection(this);
        var uniV = Car.createCar(getApplicationContext(), uniVServiceConnection);
        uniVServiceConnection.bindCarBeforeConnection(uniV);

        try {
            Log.i(Constants.LOG_TAG, "univcrutch car created");
            uniV.connect();
            Log.i(Constants.LOG_TAG, "univcrutch car connection started");
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "univcrutch exception" + e);
        }

        TabLayout tabLayout = findViewById(R.id.sectionsTabLayout);
        ViewPager2 viewPager = findViewById(R.id.mainViewPager);

        // 1. Создаем и устанавливаем адаптер
        var adapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // 2. Настраиваем TabLayoutMediator для синхронизации вкладок
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Настройки");
                    break;
                case 1:
                    tab.setText("Подробнее");
                    break;
                case 2:
                    tab.setText("Лог");
                    break;
            }
        }).attach();
    }
}