package com.example.fintrack.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fintrack.R;
import com.example.fintrack.db.TipsItem;

public class TipsAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<TipsItem> dataList;

    public void setSearchList(List<TipsItem> dataSearchList){
        this.dataList = dataSearchList;
        notifyDataSetChanged();
    }

    public TipsAdapter(Context context, List<TipsItem> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.recImage.setImageResource(dataList.get(position).getImageId());
        holder.recTitle.setText(dataList.get(position).getTitle());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webLink = dataList.get(position).getWebLink();

                if (webLink != null && !webLink.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(webLink));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "No link available for this tip", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recTitle;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        recCard = itemView.findViewById(R.id.recCard);
    }
}