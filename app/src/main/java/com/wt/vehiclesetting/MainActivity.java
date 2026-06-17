package com.wt.vehiclesetting;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.deflik.univswc281ocrutch.infrastructure.AppLog;
import com.deflik.univswc281ocrutch.services.UniVServiceConnection;
import com.deflik.univswc281ocrutch.ui.MainViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

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

        UniVServiceConnection.getSingletonInstance(getApplicationContext())
            .onEstablishedConnection(ctrl -> (findViewById(R.id.disconnectedText)).setVisibility(INVISIBLE));
//            .onConnectionClosed(() -> (findViewById(R.id.disconnectedText)).setVisibility(VISIBLE)); todo fix memory leakage

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
}