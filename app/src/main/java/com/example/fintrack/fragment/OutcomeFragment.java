package com.example.fintrack.fragment;

import com.example.fintrack.R;
import com.example.fintrack.db.DBManager;
import com.example.fintrack.db.TypeItem;

import java.util.ArrayList;
import java.util.List;

public class OutcomeFragment extends BaseRecordFragment
{

    protected void loadDataToGV(){
        super.loadDataToGV();
        List<TypeItem> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("Other");
        typeIv.setImageResource(R.mipmap.ic_other_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountItem.setKind(0);
        DBManager.insertItemToAccounttb(accountItem);
    }
}
