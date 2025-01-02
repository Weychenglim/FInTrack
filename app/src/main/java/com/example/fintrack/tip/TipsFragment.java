package com.example.fintrack.tip;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fintrack.R;
import com.example.fintrack.adapter.TipsAdapter;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.TipsItem;

import java.util.ArrayList;
import java.util.List;

public class TipsFragment extends Fragment {

    RecyclerView recyclerView; // RecyclerView to display the list of tips
    List<TipsItem> dataList; // List to store tips data
    TipsAdapter adapter; // Adapter for binding data to the RecyclerView

    SearchView searchView; // SearchView for filtering tips by title

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // Initialize the SearchView
        searchView = view.findViewById(R.id.search);
        searchView.clearFocus(); // Ensure the search bar doesn't have focus initially

        // Set a listener to handle text changes in the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Do nothing on query submission
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list based on the entered text
                searchList(newText);
                return true;
            }
        });

        // Set up the RecyclerView with a GridLayoutManager (1 column)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize the data list and load data from the database
        dataList = new ArrayList<>();
        dataList = DBManager.getTipsList();

        // Initialize the adapter and bind it to the RecyclerView
        adapter = new TipsAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Filters the list of tips based on the entered text.
     * If no matches are found, a toast message is displayed.
     *
     * @param text the text entered in the SearchView
     */
    private void searchList(String text) {
        // Temporary list to store matching items
        List<TipsItem> dataSearchList = new ArrayList<>();

        // Iterate through the original list and find matches
        for (TipsItem data : dataList) {
            if (data.getTitle().toLowerCase().contains(text.toLowerCase())) {
                dataSearchList.add(data); // Add matching items to the temporary list
            }
        }

        // Handle the case where no matches are found
        if (dataSearchList.isEmpty()) {
            Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_SHORT).show();
        } else {
            // Update the adapter with the filtered list
            adapter.setSearchList(dataSearchList);
        }
    }
}
