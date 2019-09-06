package com.example.apk;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.Date;

public class ListenerNotification extends NotificationListenerService {

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("\r\n" + new Date().toString() + "\r\n" + this + " -> " + new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        System.out.println("\r\n" + new Date().toString() + "\r\n" + this + " -> " + new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        System.out.println("\r\n" + new Date().toString() + "\r\n" + this + " -> " + new Object() {
        }.getClass().getEnclosingMethod().getName());
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();

    }

}
