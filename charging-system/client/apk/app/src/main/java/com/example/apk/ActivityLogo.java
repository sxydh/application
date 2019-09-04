package com.example.apk;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;

import cn.net.bhe.utils.Credential;
import cn.net.bhe.utils.Host;

public class ActivityLogo extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (StringUtils.isNotEmpty(Credential.getCookie(Host.MAIN))) {
                        intent.setClass(ActivityLogo.this, ActivityFrame.class);
                    } else {
                        intent.setClass(ActivityLogo.this, ActivityLogin.class);
                    }
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
