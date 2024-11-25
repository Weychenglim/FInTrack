package com.example.fintrack.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.fintrack.R;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.TypeItem;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends BaseRecordFragment {

    @Override
    protected void loadDataToGV(){
        super.loadDataToGV();
        List<TypeItem> outlist = DBManager.getTypeList(1);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Other");
        typeIv.setImageResource(R.mipmap.in_other2_fs);
    }

    public void saveAccountToDB() {
        accountItem.setKind(1);
        DBManager.insertItemToAccounttb(accountItem);
    }


}