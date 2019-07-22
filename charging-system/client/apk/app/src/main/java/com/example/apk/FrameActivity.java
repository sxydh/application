package com.example.apk;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import cn.net.bhe.utils.HttpUtils;

public class FrameActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        drawerLayout = findViewById(R.id.draw_layout);
        findViewById(R.id.menu_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menu_button:
                        drawerLayout.openDrawer(Gravity.LEFT);
                        break;
                }
            }
        });

        new Thread(() -> {
            try {
                String str = HttpUtils.get("https://www.baidu.com/", null).get("body").toString();
                System.out.println(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
