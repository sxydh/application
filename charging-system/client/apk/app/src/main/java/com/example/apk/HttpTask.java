package com.example.apk;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

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
            HttpUtils.Rt rt = HttpUtils.post(
                    HttpUtils.HOST_MAIN,
                    loads[0].getPath(),
                    JacksonUtils.objToJsonStr(loads[0].getMap()));
            result = JacksonUtils.objToJsonStr(rt);
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
