package com.example.apk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

public class ActivityFrame extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private AdapterViewPagerWater adapterViewPagerWater;
    private AdapterViewPagerWallet adapterViewPagerWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        drawerLayout = findViewById(R.id.bhe_draw_layout);
        findViewById(R.id.bhe_menu_button).setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.bhe_menu_button:
                    drawerLayout.openDrawer(Gravity.LEFT);
                    break;
            }
        });

        adapterViewPagerWater = new AdapterViewPagerWater(this, getSupportFragmentManager());
        adapterViewPagerWallet = new AdapterViewPagerWallet(this, getSupportFragmentManager());

        viewPager = findViewById(R.id.bhe_view_pager);
        viewPager.setAdapter(adapterViewPagerWater);
    }

    public void change(View view) {
        switch (view.getId()) {
            case R.id.bhe_water:
                viewPager.setAdapter(adapterViewPagerWater);
                break;
            case R.id.bhe_wallet:
                viewPager.setAdapter(adapterViewPagerWallet);
                break;
        }
    }
}
