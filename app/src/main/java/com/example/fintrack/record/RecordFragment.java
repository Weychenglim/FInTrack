package com.example.fintrack.record;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fintrack.R;
import com.example.fintrack.adapter.RecordPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class RecordFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.record_tabs);
        viewPager = view.findViewById(R.id.record_vp);

        initPager();
    }

    private void initPager(){

        List<Fragment> fragmentList = new ArrayList<>();
        OutcomeFragment outFrag = new OutcomeFragment();
        IncomeFragment inFrag = new IncomeFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        /* an adapter acts as a bridge between a data source and a UI component, allowing dynamic data to be displayed
        in views like lists, grids, or view pagers. In this case, RecordPagerAdapter is a custom adapter that manages the
        fragments for a ViewPager. ViewPager is a layout manager that lets users swipe between fragments, which are often
        used to separate content into distinct screens.
        RecordPagerAdapter takes a FragmentManager as one of its parameters (in this case, getSupportFragmentManager()),
        which allows the adapter to manage fragment transactions. This manager handles adding, removing, and replacing
        fragments within the ViewPager, keeping them properly aligned with user navigation.
         Fragment List: RecordPagerAdapter also takes the fragmentList, which contains OutcomeFragment and IncomeFragment.
         The adapter's role is to bind each fragment to its corresponding position in the ViewPager. When the user swipes
         or navigates between tabs, the adapter knows which fragment to display based on the position index, ensuring smooth
         transitions.*/
    }

}