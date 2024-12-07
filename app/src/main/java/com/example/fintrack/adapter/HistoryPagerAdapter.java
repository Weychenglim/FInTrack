package com.example.fintrack.adapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.fintrack.R;

import java.util.List;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;
    private final String[] titles = {"Expend", "Income"};

    public HistoryPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public String getFragmentTag(int position) {
        return "android:switcher:" + R.id.record_vp_history + ":" + position;
    }
}
