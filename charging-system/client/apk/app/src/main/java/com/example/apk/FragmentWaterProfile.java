package com.example.apk;

import android.content.ComponentName;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import cn.net.bhe.utils.Host;
import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.Load;


public class FragmentWaterProfile extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private static Map<String, Object> cache = new HashMap<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.water_profile, container, false);

        textView = root.findViewById(R.id.bhe_water_profile_text);

        swipeRefreshLayout = root.findViewById(R.id.bhe_water_profile);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        });

        if (cache.isEmpty()) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    refresh();
                }
            });
        } else {
            textView.setText(JacksonUtils.objToJsonStr(cache));
        }

        return root;
    }

    public void refresh() {
        new AsyncTask<Load, Object, HttpUtils.Rt>() {
            @Override
            protected HttpUtils.Rt doInBackground(Load... loads) {
                return HttpUtils.get(
                        Host.MAIN,
                        loads[0].getPath(),
                        null);
            }

            @Override
            protected void onPostExecute(HttpUtils.Rt rt) {
                super.onPostExecute(rt);

                cache = (Map<String, Object>) rt.getData();
                textView.setText(JacksonUtils.objToJsonStr(rt.getData()));

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }.execute(Load.instance("/water/get"));
    }

}
