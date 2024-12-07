package com.example.fintrack.history;

import android.util.Log;

import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class ListHistoryFragmentExpend extends ListHistoryFragment{

    @Override
    protected void loadData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        List<AccountItem> list = DBManager.getAccountListOneDayFromAccounttbType(year,month,day,0);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void onSearchQueryChanged(String query) {
        if (adapter != null) {
            Log.d("sdf","esdf");
            if (query.isEmpty()) {
                // Restore the full list if the query is empty
                loadDBData(); // This resets mDatas to the full original list
            } else {
                // Filter starting from the original full data list (mDatas)
                loadDBData();
                List<AccountItem> filteredList = new ArrayList<>();
                for (AccountItem item : mDatas) {  //The underlying behavior here is how mDatas is used for filtering: once it is filtered, it holds only the filtered results unless explicitly reset.
                    if (item.getTypename().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                // Update the adapter with the new filtered list
                adapter.updateData(filteredList);
            }
        }
    }

    private void loadDBData() {
        List<AccountItem> list = DBManager.getAccountListOneDayFromAccounttbType(year, month, day,0);
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
