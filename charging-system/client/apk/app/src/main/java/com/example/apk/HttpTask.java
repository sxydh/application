package com.example.apk;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import java.util.Map;

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.K;

public class HttpTask extends AsyncTask<Map<String, Object>, Object, String> {

    private Activity activity;

    public static HttpTask init(Activity activity) {
        HttpTask httpTask = new HttpTask();
        httpTask.activity = activity;
        return httpTask;
    }

    @Override
    protected String doInBackground(Map<String, Object>... maps) {
        String result = null;
        try {
            Map<String, Object> map = HttpUtils.post(
                    (String) maps[0].get(K.URL),
                    (String) maps[0].get(K.PARAM));
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
