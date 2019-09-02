package com.example.apk;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Map;

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.Load;

public class HttpTask extends AsyncTask<Load, Object, String> {

    private Activity activity;

    public static HttpTask init(Activity activity) {
        HttpTask httpTask = new HttpTask();
        httpTask.activity = activity;
        return httpTask;
    }

    @Override
    protected String doInBackground(Load... loads) {
        String result = null;
        try {
            Map<String, Object> map = HttpUtils.post(
                    loads[0].getUrl(),
                    JacksonUtils.objToJsonStr(loads[0].getMap()));
            result = JacksonUtils.objToJsonStr(map.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        TextView textView = this.activity.findViewById(R.id.wallet_profile);
        textView.setText(s);
    }
}
