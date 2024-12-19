package com.example.fintrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.db.AccountItem;
import com.example.fintrack.db.SavingItem;
import com.example.fintrack.db.SavingTransactionItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SavingTransactionAdapter extends BaseAdapter {
    Context context; // Activity context
    List<SavingTransactionItem> mDatas; // List of AccountItem data to display
    LayoutInflater inflater; // LayoutInflater to create item views

    public SavingTransactionAdapter(Context context, List<SavingTransactionItem> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
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
        SavingTransactionAdapter.ViewHolder holder;

        // Inflate a new view if one is not being reused
        // Check if there is an existing reusable view (convertView) for this item
        if (convertView == null) {
            // If convertView is null, it means there is no reusable view available,
            // so inflate a new view from the layout resource (item_mainlv)
            convertView = inflater.inflate(R.layout.item_saving_transaction_lv, parent, false);

            // Create a new ViewHolder object, passing in the new view to hold references
            // to the view's child elements (like ImageView and TextViews) for quick access
            holder = new SavingTransactionAdapter.ViewHolder(convertView);

            // Associate the ViewHolder with the new view, so it can be reused when
            // convertView is recycled. This helps improve performance by avoiding
            // repeated calls to findViewById
            convertView.setTag(holder);
        } else {
            // If convertView is not null, it means we can reuse it, so we retrieve
            // the associated ViewHolder that was previously set as a tag
            holder = (SavingTransactionAdapter.ViewHolder) convertView.getTag();
        }


        SavingTransactionItem savingTransactionItem = mDatas.get(position);
        if (savingTransactionItem.getDate() != null) {
            try {
                // Parse the transactionDate string into a Date object
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date transactionDate = sdf.parse(savingTransactionItem.getDate());

                // Get today's date
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                // Get the start of the transaction day
                Calendar transactionDay = Calendar.getInstance();
                transactionDay.setTime(transactionDate);
                transactionDay.set(Calendar.HOUR_OF_DAY, 0);
                transactionDay.set(Calendar.MINUTE, 0);
                transactionDay.set(Calendar.SECOND, 0);
                transactionDay.set(Calendar.MILLISECOND, 0);

                // Format the time
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                if (transactionDay.equals(today)) {
                    // If the transaction is today, display the time only
                    holder.date.setText("Today " + timeFormat.format(transactionDate));
                } else {
                    // Otherwise, display the full transaction date
                    holder.date.setText(savingTransactionItem.getDate());
                }
            } catch (ParseException e) {
                e.printStackTrace();
                // Fallback: Display the raw transactionDate if parsing fails
                holder.date.setText(savingTransactionItem.getDate());
            }
        } else {
            holder.date.setText(""); // Handle null case
        }

        holder.amount.setText("+ RM " + String.format("%.2f",savingTransactionItem.getAmount()));
        return convertView; // Return the view for the current list item
    }



    public void updateData(List<SavingTransactionItem> newData) {
        mDatas.clear();
        mDatas.addAll(newData);
        notifyDataSetChanged();
    }

    // ViewHolder pattern to cache views for each item, improving performance
    class ViewHolder {

        TextView amount, date;

        public ViewHolder(View view) {
            date = view.findViewById(R.id.saving_transaction_time);
            amount = view.findViewById(R.id.saving_transaction_amount);
        }
    }
}
