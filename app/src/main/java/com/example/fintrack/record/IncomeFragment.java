package com.example.fintrack.record;

import androidx.fragment.app.Fragment;

import com.example.fintrack.R;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.TypeItem;

import java.util.List;


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