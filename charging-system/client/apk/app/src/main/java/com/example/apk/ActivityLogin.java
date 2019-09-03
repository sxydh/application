package com.example.apk;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.K;
import cn.net.bhe.utils.Load;
import cn.net.bhe.utils.M;

public class ActivityLogin extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        button = findViewById(R.id.bhe_login);
        findViewById(R.id.bhe_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Load, String, HttpUtils.Rt>() {
                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);

                        button.setText(values[0]);
                    }

                    @Override
                    protected HttpUtils.Rt doInBackground(Load... loads) {
                        publishProgress("loading");
                        return HttpUtils.post(
                                HttpUtils.HOST_MAIN,
                                loads[0].getPath(),
                                JacksonUtils.objToJsonStr(loads[0].getMap()));
                    }

                    @Override
                    protected void onPostExecute(HttpUtils.Rt rt) {
                        super.onPostExecute(rt);

                        Map<String, Object> data = (Map<String, Object>) rt.getData();
                        Map<String, Object> user = (Map<String, Object>) data.get("user");
                        HttpUtils.credential.put(HttpUtils.HOST_MAIN, M.g().put(K.COOKIE, "SESSION=" + user.get("session_id")).getMap());

                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(ActivityLogin.this, ActivityFrame.class);
                        startActivity(intent);
                    }
                }.execute(Load.instance("/user/login").put("phone", "15186942525").put("password", "670b14728ad9902aecba32e22fa4f6bd").put("type", 2));
            }
        });
    }
}
