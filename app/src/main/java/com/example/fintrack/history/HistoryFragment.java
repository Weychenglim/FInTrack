package com.example.fintrack.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fintrack.R;
import com.example.fintrack.adapter.HistoryPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.record_tabs_history);
        viewPager = view.findViewById(R.id.record_vp_history);
        initPager();
    }

    public void initPager(){
        List<Fragment> fragmentList = new ArrayList<>();
        ListHistoryFragmentExpend expend = new ListHistoryFragmentExpend();
        ListHistoryFragmentIncome income = new ListHistoryFragmentIncome();
        /* Since these are fragment classes, the lifecycle methods (onCreate, onCreateView, onViewCreated) of the parent class will also be executed unless overridden in the child class.

If ListHistoryFragmentExpend or ListHistoryFragmentIncome overrides any of these lifecycle methods, the overridden methods in the child class will run instead.
If the child class methods call super.methodName(), then the parent class's implementation will also execute.
*/
        fragmentList.add(expend);
        fragmentList.add(income);

        HistoryPagerAdapter pagerAdapter = new HistoryPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public HistoryPagerAdapter getAdapter() {
        return (HistoryPagerAdapter) viewPager.getAdapter();
    }

}
