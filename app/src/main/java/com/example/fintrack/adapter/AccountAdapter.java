package com.example.fintrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.db.AccountItem;
import java.util.Calendar;
import java.util.List;

// Adapter class to bind account data to the ListView in the layout
public class AccountAdapter extends BaseAdapter {
    Context context; // Activity context
    List<AccountItem> mDatas; // List of AccountItem data to display
    LayoutInflater inflater; // LayoutInflater to create item views
    int year, month, day; // Current year, month, and day

    // Constructor to initialize context, data, and the inflater
    public AccountAdapter(Context context, List<AccountItem> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);

        // Get the current date (year, month, day)
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based, add 1
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // Returns the number of items in the data list
    @Override
    public int getCount() {
        return mDatas.size();
    }

    // Returns an item at a specific position
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    // Returns the item ID, which is the same as the position in this case
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Creates or reuses a view for each item in the list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Inflate a new view if one is not being reused
        // Check if there is an existing reusable view (convertView) for this item
        if (convertView == null) {
            // If convertView is null, it means there is no reusable view available,
            // so inflate a new view from the layout resource (item_mainlv)
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);

            // Create a new ViewHolder object, passing in the new view to hold references
            // to the view's child elements (like ImageView and TextViews) for quick access
            holder = new ViewHolder(convertView);

            // Associate the ViewHolder with the new view, so it can be reused when
            // convertView is recycled. This helps improve performance by avoiding
            // repeated calls to findViewById
            convertView.setTag(holder);
        } else {
            // If convertView is not null, it means we can reuse it, so we retrieve
            // the associated ViewHolder that was previously set as a tag
            holder = (ViewHolder) convertView.getTag();
        }


        // Get the AccountItem for the current position
        AccountItem accountItem = mDatas.get(position);

        // Set item views based on AccountItem properties
        holder.typeIv.setImageResource(accountItem.getSimageId()); // Set the item icon
        holder.typeTv.setText(accountItem.getTypename()); // Set the type name
        holder.remarkTv.setText(accountItem.getRemark()); // Set any remarks

        // Format the money value to two decimal places
        String formattedMoney = String.format("RM %.2f", accountItem.getMoney());
        holder.moneyTv.setText(formattedMoney);

        if (accountItem.getKind() == 0) {
            holder.moneyTv.setTextColor(context.getResources().getColor(R.color.red)); // Use red color for kind == 0
        } else if (accountItem.getKind() == 1) {
            holder.moneyTv.setTextColor(context.getResources().getColor(R.color.green_5cd65c)); // Use green color for kind == 1
        }

        // If the date matches today, display "Today" and the time; otherwise, display full date and time
        if (accountItem.getYear() == year && accountItem.getMonth() == month && accountItem.getDay() == day) {
            String time = accountItem.getTime().split("\\s+")[1]; // Extract time part from the timestamp
            holder.timeTv.setText("Today " + time);
        } else {
            holder.timeTv.setText(accountItem.getTime()); // Display full timestamp if it's not today
        }

        return convertView; // Return the view for the current list item
    }

    public void updateData(List<AccountItem> newData) {
        mDatas.clear();
        mDatas.addAll(newData);
        notifyDataSetChanged();
    }

    // ViewHolder pattern to cache views for each item, improving performance
    class ViewHolder {
        ImageView typeIv; // Icon representing the type
        TextView typeTv, remarkTv, timeTv, moneyTv; // TextViews for type name, remark, time, and money

        // Constructor initializes the view references from layout resource
        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            remarkTv = view.findViewById(R.id.item_mainlv_tv_remark);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}
