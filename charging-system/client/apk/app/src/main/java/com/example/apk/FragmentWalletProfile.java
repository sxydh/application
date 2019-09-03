package com.example.apk;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.HashMap;
import java.util.Map;

import cn.net.bhe.utils.HttpUtils;
import cn.net.bhe.utils.JacksonUtils;
import cn.net.bhe.utils.Load;

public class FragmentWalletProfile extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textView;
    private Map<String, Object> cache = new HashMap<>();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_profile, container, false);

        textView = root.findViewById(R.id.wallet_profile_text);

        swipeRefreshLayout = root.findViewById(R.id.wallet_profile);
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
                        HttpUtils.HOST_MAIN,
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
        }.execute(Load.instance("/wallet/get"));
    }
}


