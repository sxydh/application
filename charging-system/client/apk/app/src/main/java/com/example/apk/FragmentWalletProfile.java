package com.example.apk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import cn.net.bhe.utils.Load;

public class FragmentWalletProfile extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_profile, container, false);

        Load load = Load.instance().setUrl("/wallet/get");
        Map<String, Object> map = new HashMap<>();
        map.put("url", "/wallet/get");
        map.put("phone", "15186942525");
        map.put("password", "670b14728ad9902aecba32e22fa4f6bd");
        map.put("type", 2);
        HttpTask.init(this.getActivity()).execute(map);

        return root;
    }
}


