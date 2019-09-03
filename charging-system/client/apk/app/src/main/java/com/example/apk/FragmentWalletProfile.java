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

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.Load;

public class FragmentWalletProfile extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_profile, container, false);

        Activity activity = this.getActivity();

        new AsyncTask<Load, Object, HttpUtils.Rt>() {
            @Override
            protected HttpUtils.Rt doInBackground(Load... loads) {
                return HttpUtils.get(
                        HttpUtils.HOST_MAIN,
                        loads[0].getPath(),
                        null);
            }

            @Override
            protected void onPostExecute(HttpUtils.Rt rt) {
                super.onPostExecute(rt);

                TextView textView = activity.findViewById(R.id.wallet_profile);
                textView.setText(JacksonUtils.objToJsonStr(rt.getData()));
            }
        }.execute(Load.instance("/wallet/get"));

        return root;
    }
}


