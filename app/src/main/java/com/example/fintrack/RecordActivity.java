package com.example.fintrack;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fintrack.adapter.RecordPagerAdapter;
import com.example.fintrack.fragment.IncomeFragment;
import com.example.fintrack.fragment.OutcomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);

        initPager();
    }

    private void initPager(){

        List<Fragment>fragmentList = new ArrayList<>();
        OutcomeFragment outFrag = new OutcomeFragment();
        IncomeFragment inFrag = new IncomeFragment();
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
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

    public void onClick(View view) {
            if (view.getId() == R.id.record_iv_close){
                finish();
            }
        }
    }

