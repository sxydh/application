package com.example.apk;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URLEncoder;
import java.util.Map;

import cn.net.bhe.utils.Credential;
import cn.net.bhe.utils.Host;
import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.Load;

public class ActivityLogin extends AppCompatActivity {

    private static final int SHOW_NOTIFICATION_SERVICE_ALERT_DIALOG = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SHOW_NOTIFICATION_SERVICE_ALERT_DIALOG:
                    AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
                    enableNotificationListenerAlertDialog.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button button = findViewById(R.id.bhe_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<Load, String, HttpUtils.Rt>() {

                    @Override
                    protected HttpUtils.Rt doInBackground(Load... loads) {
                        if (!isNotificationServicePermissionEnabled()) {
                            Message message = new Message();
                            message.what = SHOW_NOTIFICATION_SERVICE_ALERT_DIALOG;
                            handler.sendMessage(message);
                            return null;
                        }

                        try {
                            Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + URLEncoder.encode("https://qr.alipay.com/fkx08338aku9ygrjcwlw157", "utf-8") + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (1 > 0) {
                            return null;
                        }

                        publishProgress("loading");
                        return HttpUtils.post(
                                Host.MAIN,
                                loads[0].getPath(),
                                JacksonUtils.objToJsonStr(loads[0].getMap()));
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        super.onProgressUpdate(values);

                        button.setText(values[0]);
                    }

                    @Override
                    protected void onPostExecute(HttpUtils.Rt rt) {
                        super.onPostExecute(rt);

                        if (rt == null) {
                            return;
                        }
                        Map<String, Object> data = (Map<String, Object>) rt.getData();
                        Map<String, Object> user = (Map<String, Object>) data.get("user");
                        Credential.setCookie(Host.MAIN, "SESSION=" + user.get("sessionid"));

                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(ActivityLogin.this, ActivityFrame.class);
                        startActivity(intent);
                    }
                }.execute(Load.instance("/user/login").put("phone", "15186942525").put("password", "670b14728ad9902aecba32e22fa4f6bd").put("type", 2));
            }
        });

        if (!isNotificationServicePermissionEnabled()) {
            AlertDialog enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNotificationServicePermissionEnabled()) {
            Message message = new Message();
            message.what = SHOW_NOTIFICATION_SERVICE_ALERT_DIALOG;
            handler.sendMessage(message);
        }
    }

    public boolean isNotificationServicePermissionEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public AlertDialog buildNotificationServiceAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Notification Listener Service");
        alertDialogBuilder.setMessage("For the the app. to work you need to enable the Notification Listener Service. Enable it now?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return (alertDialogBuilder.create());
    }

}
