package com.wt.vehiclesetting;

import android.car.Car;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

//import com.deflik.univswc281ocrutch.infrastructure.Constants;
//import com.deflik.univswc281ocrutch.services.KeyInterceptorService;
import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.deflik.univswc281ocrutch.services.UniVServiceConnection;
import com.deflik.univswc281ocrutch.ui.MainViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private UniVServiceConnection uniVServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppLog.i("mainActivity onCreate");

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ((TextView)findViewById(R.id.versionText)).setText(BuildConfig.VERSION_NAME);
        ((TextView)findViewById(R.id.commitHashText)).setText(BuildConfig.GIT_HASH);

//        var serviceIntent = new Intent(this, KeyInterceptorService.class);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(serviceIntent);
//        } else {
//            startService(serviceIntent);
//        }

        uniVServiceConnection = new UniVServiceConnection(this);
        var uniV = Car.createCar(getApplicationContext(), uniVServiceConnection);
        uniVServiceConnection.bindCarBeforeConnection(uniV);
        AppLog.i("univ car class created, performing connection...");

        try {
            uniV.connect();
            AppLog.i("car connection started");
        } catch (Exception e) {
            AppLog.e("car connection exc " + e);
        }

        TabLayout tabLayout = findViewById(R.id.sectionsTabLayout);
        ViewPager2 viewPager = findViewById(R.id.mainViewPager);

        var adapter = new MainViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.i(Constants.LOG_TAG, "keyCodeEvent= " + keyCode);
//        switch (keyCode) {
////            case KeyEvent.KEYCODE_VOLUME_UP:
////                // Запуск отслеживания для возможного долгого нажатия
////                event.startTracking();
////                showToast("Нажата громкость +");
////                return true; // Перехватываем событие (громкость системы не изменится)
////
////            case KeyEvent.KEYCODE_VOLUME_DOWN:
////                showToast("Нажата громкость -");
////                return true;
//            case 1005:
//                Log.i(Constants.LOG_TAG, "catch key 1005");
//                uniVServiceConnection.controller.changeExhaustState();
//                return true;
//
//            default:
//                return super.onKeyDown(keyCode, event);
//        }
//    }
}