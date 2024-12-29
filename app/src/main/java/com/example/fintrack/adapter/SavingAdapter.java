package com.example.fintrack.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

public class SavingAdapter extends BaseAdapter {
    Context context; // Activity context
    List<SavingItem> mDatas; // List of AccountItem data to display
    LayoutInflater inflater; // LayoutInflater to create item views

    public SavingAdapter(Context context, List<SavingItem> mDatas) {
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
        SavingAdapter.ViewHolder holder;

        // Inflate a new view if one is not being reused
        // Check if there is an existing reusable view (convertView) for this item
        if (convertView == null) {
            // If convertView is null, it means there is no reusable view available,
            // so inflate a new view from the layout resource (item_mainlv)
            convertView = inflater.inflate(R.layout.item_saving_lv, parent, false);

            // Create a new ViewHolder object, passing in the new view to hold references
            // to the view's child elements (like ImageView and TextViews) for quick access
            holder = new SavingAdapter.ViewHolder(convertView);

            // Associate the ViewHolder with the new view, so it can be reused when
            // convertView is recycled. This helps improve performance by avoiding
            // repeated calls to findViewById
            convertView.setTag(holder);
        } else {
            // If convertView is not null, it means we can reuse it, so we retrieve
            // the associated ViewHolder that was previously set as a tag
            holder = (SavingAdapter.ViewHolder) convertView.getTag();
        }


        SavingItem savingItem = mDatas.get(position);
        holder.title.setText(savingItem.getGoalTitle());
        holder.amount.setText("RM " + String.valueOf(String.format("%.2f",savingItem.getAmount())));
        holder.priority.setText(savingItem.getPriority() == 1 ? "High Priority" : savingItem.getPriority() == 2 ? "Normal Priority" : "Low Priority");
            if (savingItem.getStatus().equals("Completed")){
                holder.daysLeft.setText("Completed");
                holder.daysLeft.setTextColor(context.getResources().getColor(R.color.green_bright));
                holder.priority.setText("");
            }else if (savingItem.getStatus().equals("Expired")){
                holder.daysLeft.setText("Expired");
                holder.daysLeft.setTextColor(context.getResources().getColor(R.color.red_bright));
                holder.priority.setText("");
            }else{
                holder.daysLeft.setText(savingItem.getDayLeft() + " Days Left");
            }
        String imageUriString = savingItem.getImageUri();
        if (imageUriString != null) {
            Log.d("ImageURI", "Image URI detected");
            Uri imageUri = Uri.parse(imageUriString);
            try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri)) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Convert 120dp to pixels
                float density = context.getResources().getDisplayMetrics().density;
                int widthPx = (int) (120 * density);
                int heightPx = (int) (120 * density);

                // Resize the bitmap
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, widthPx, heightPx, true);

                // Set the resized image in ImageView
                holder.icon.setImageBitmap(resizedBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            holder.icon.setImageResource(R.drawable.target_arrow_svgrepo_com); // Default image
        }

        int progress = (int) Math.round(savingItem.getPercentage());
        progress = Math.max(0, Math.min(100, progress)); // Clamp between 0 and 100
        holder.progress.setProgress(progress);
        return convertView; // Return the view for the current list item
    }

    public void updateData(List<SavingItem> newData) {
        mDatas.clear();
        mDatas.addAll(newData);
        notifyDataSetChanged();
    }

    // ViewHolder pattern to cache views for each item, improving performance
    class ViewHolder {
        ProgressBar progress;
        TextView title, amount, priority,daysLeft;

        ImageView icon;
        public ViewHolder(View view) {
            progress = view.findViewById(R.id.progress);
            title = view.findViewById(R.id.goal_title);
            amount = view.findViewById(R.id.goal_amount);
            icon = view.findViewById(R.id.target_icon);
            priority = view.findViewById(R.id.goal_priority);
            daysLeft = view.findViewById(R.id.goal_daysleft);
        }
    }
}
