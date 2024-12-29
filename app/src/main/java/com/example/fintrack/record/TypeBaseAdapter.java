package com.example.fintrack.record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.db.TypeItem;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {
    Context context;
    List<TypeItem> mDatas;
    int selectPos = 0;  // Selected position

    public TypeBaseAdapter(Context context, List<TypeItem> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // This adapter does not consider the reuse of views because all items are displayed on the screen
    // and will not disappear due to scrolling, so there are no remaining convertViews, and we don't need to reuse them
    @Override
    //getView creates a new view for each item, which is memory-inefficient for large lists. This approach is typically fine for a grid with a small, fixed number of items displayed simultaneously.
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        // Find controls in the layout
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        // Get the data source for the specified position
        TypeItem typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypename());

        // Check if the current position is the selected position.
        // If it is the selected position, set a colored image, otherwise set a grayscale image
        if (selectPos == position) {
            iv.setImageResource(typeBean.getSimageid());
        } else {
            iv.setImageResource(typeBean.getImageid());
        }
        return convertView;
    }
}
