package com.example.fintrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.example.fintrack.adapter.AccountAdapter;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.history.HistoryFragment;
import com.example.fintrack.history.ListHistoryFragment;
import com.example.fintrack.history.ListHistoryFragmentExpend;
import com.example.fintrack.history.ListHistoryFragmentIncome;
import com.example.fintrack.history.SelectDateHistory;
import com.example.fintrack.utils.BudgetDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity  {

    public interface OnSearchQueryListener {
        void onSearchQueryChanged(String query);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BottomNavigationView bottomNavView = findViewById(R.id.bottomNavigationView);
        // Set the background of BottomNavigationView to null
        // Get NavHostFragment directly and then get its NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.NHFMain);

        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavView, navController);
        Toolbar toolbar = findViewById(R.id.TBMainAct);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);   // the text on the tool bar is determined by the label in nav_graph
        setupBottomNavMenu(navController);
    }

    private void setupBottomNavMenu(NavController navController){
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNav,navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        NavController navController = Navigation.findNavController(this, R.id.NHFMain);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        MenuItem calendarItem = menu.findItem(R.id.action_calendar);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Check if the current destination is the Home fragment
            if (destination.getId() == R.id.home) { // Replace with the actual ID of the Home fragment
                searchItem.setVisible(true);
                calendarItem.setVisible(false);// Show search button
            } else if (destination.getId() == R.id.history) {
                searchItem.setVisible(true);
                calendarItem.setVisible(true);
            }else if (destination.getId() == R.id.overview) {
                    searchItem.setVisible(false);
                    calendarItem.setVisible(true);
            } else {
                searchItem.setVisible(false);
                calendarItem.setVisible(false);// Hide search button
            }
        });
        SharedViewModel viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        calendarItem.setOnMenuItemClickListener(item -> {
            SelectDateHistory dialog = new SelectDateHistory(this);
            dialog.show();

            dialog.setOnEnsureListener((time, year, month, day) -> {
                viewModel.setDateData(new SharedViewModel.DateData(time, year, month, day));
            });

            return true;
        });

        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setQueryHint("Type a category");
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // No changes needed when expanded
                calendarItem.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                    // Check if the current destination is the Home fragment
                    if (destination.getId() == R.id.history) { // Replace with the actual ID of the Home fragment
                        calendarItem.setVisible(true);// Show search button
                    }});
                // When collapsed, clear the search query and reset the list
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.NHFMain);
                if (currentFragment instanceof NavHostFragment) {
                    Fragment navFragment = ((NavHostFragment) currentFragment).getChildFragmentManager().getPrimaryNavigationFragment();
                    if (navFragment instanceof Main_Fragment) {
                        ((Main_Fragment) navFragment).onSearchQueryChanged(""); // Reset the list
                    }
                }
                if (currentFragment instanceof NavHostFragment) {
                    Fragment navFragment = ((NavHostFragment) currentFragment).getChildFragmentManager().getPrimaryNavigationFragment();

                    if (navFragment instanceof HistoryFragment) {
                        HistoryFragment historyFragment = (HistoryFragment) navFragment;
                        FragmentManager childFragmentManager = historyFragment.getChildFragmentManager();

                        // Get the current tab index
                        ViewPager viewPager = historyFragment.getView().findViewById(R.id.record_vp_history);
                        int currentTabIndex = viewPager.getCurrentItem();
                        Log.d("currenttab", String.valueOf(currentTabIndex));

                        // Retrieve the correct fragment using the adapter's tag
                        String fragmentTag = historyFragment.getAdapter().getFragmentTag(currentTabIndex);
                        Log.d("currenttab", fragmentTag);
                        Fragment currentTabFragment = childFragmentManager.findFragmentByTag(fragmentTag);
                        Log.d("CurrentTab", "Current Tab: " + (currentTabFragment != null ? currentTabFragment.getClass().getSimpleName() : "null"));
                        if (currentTabFragment == null) {
                            currentTabFragment = historyFragment.getAdapter().getItem(currentTabIndex);
                        }

                        if (currentTabFragment instanceof ListHistoryFragmentExpend) {
                            Log.d("Fragment Check", "This is ListHistoryFragmentExpend");
                            ((ListHistoryFragmentExpend) currentTabFragment).onSearchQueryChanged("");
                            currentTabFragment = historyFragment.getAdapter().getItem(currentTabIndex ==0 ? 1 :0);
                            ((ListHistoryFragmentIncome) currentTabFragment).onSearchQueryChanged("");
                        } else if (currentTabFragment instanceof ListHistoryFragmentIncome) {
                            Log.d("Fragment Check", "This is ListHistoryFragmentIncome");
                            ((ListHistoryFragmentIncome) currentTabFragment).onSearchQueryChanged("");
                            currentTabFragment = historyFragment.getAdapter().getItem(currentTabIndex ==0 ? 1 :0);
                            ((ListHistoryFragmentExpend) currentTabFragment).onSearchQueryChanged("");
                        }
                    }
                }

                return true;
            }
        });
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Get the current fragment from the NavHostFragment
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.NHFMain);

                if (currentFragment instanceof NavHostFragment) {
                    Fragment navFragment = ((NavHostFragment) currentFragment).getChildFragmentManager().getPrimaryNavigationFragment();

                    if (navFragment instanceof Main_Fragment) {
                        ((Main_Fragment) navFragment).onSearchQueryChanged(newText);
                    }
                }
                if (currentFragment instanceof NavHostFragment) {
                    Fragment navFragment = ((NavHostFragment) currentFragment).getChildFragmentManager().getPrimaryNavigationFragment();

                    if (navFragment instanceof HistoryFragment) {
                        HistoryFragment historyFragment = (HistoryFragment) navFragment;
                        FragmentManager childFragmentManager = historyFragment.getChildFragmentManager();

                        // Get the current tab index
                        ViewPager viewPager = historyFragment.getView().findViewById(R.id.record_vp_history);
                        int currentTabIndex = viewPager.getCurrentItem();
                        Log.d("currenttab", String.valueOf(currentTabIndex));

                        // Retrieve the correct fragment using the adapter's tag
                        String fragmentTag = historyFragment.getAdapter().getFragmentTag(currentTabIndex);
                        Log.d("currenttab", fragmentTag);
                        Fragment currentTabFragment = childFragmentManager.findFragmentByTag(fragmentTag);
                        Log.d("CurrentTab", "Current Tab: " + (currentTabFragment != null ? currentTabFragment.getClass().getSimpleName() : "null"));
                        if (currentTabFragment == null) {
                            currentTabFragment = historyFragment.getAdapter().getItem(currentTabIndex);
                            /*The fragment might not yet be attached to the FragmentManager when you're attempting to retrieve it. This can happen if:

The fragment is still in the process of being created.
The ViewPager hasn't fully initialized the fragment.*/
                        }

                        if (currentTabFragment instanceof ListHistoryFragmentExpend) {
                            Log.d("Fragment Check", "This is ListHistoryFragmentExpend");
                            ((ListHistoryFragmentExpend) currentTabFragment).onSearchQueryChanged(newText);
                        } else if (currentTabFragment instanceof ListHistoryFragmentIncome) {
                            Log.d("Fragment Check", "This is ListHistoryFragmentIncome");
                            ((ListHistoryFragmentIncome) currentTabFragment).onSearchQueryChanged(newText);
                        }
                    }
                }

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try{
            Navigation.findNavController(this,R.id.NHFMain).navigate(item.getItemId());
            return true;
        }
        catch (Exception ex){
            return super.onOptionsItemSelected(item);}
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this,R.id.NHFMain).navigateUp();
    }


}