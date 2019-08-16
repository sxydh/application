package com.example.apk;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Map;

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;

public class FragmentWalletProfile extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_profile, container, false);

        new HttpTask(this.getActivity()).execute();

        return root;
    }
}

class HttpTask extends AsyncTask<String, Object, String> {

    Activity activity;

    public HttpTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            Map<String, Object> map = HttpUtils.post(
                    "/user/login",
                    "{"
                            + "\"phone\": \"15186942525\","
                            + "\"password\": \"670b14728ad9902aecba32e22fa4f6bd\","
                            + "\"type\": 2"
                            + "}");
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
