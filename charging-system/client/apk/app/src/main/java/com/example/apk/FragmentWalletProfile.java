package com.example.apk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import cn.net.bhe.utils.Load;

public class FragmentWalletProfile extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.wallet_profile, container, false);

        HttpTask.init(this.getActivity()).execute(Load.instance("/wallet/get")
                //
                .put("phone", "15186942525")
                //
                .put("password", "670b14728ad9902aecba32e22fa4f6bd")
                //
                .put("type", 2));

        return root;
    }
}


