package com.example.apk;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdapterViewPagerWallet extends FragmentStatePagerAdapter {

    private Context context;
    private List<Fragment> fragments = new ArrayList<>();

    public AdapterViewPagerWallet(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;

        fragments.add(new FragmentWalletProfile());
        fragments.add(new FragmentWalletLog());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }

}
