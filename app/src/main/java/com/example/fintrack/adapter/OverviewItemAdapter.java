package com.example.fintrack.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fintrack.R;
import com.example.fintrack.doubleUtils;
import com.example.fintrack.overview.OverviewItemType;

import java.util.List;

public class OverviewItemAdapter extends BaseAdapter {
    Context context;

    List <OverviewItemType> mDatas;

    LayoutInflater inflater;

    public OverviewItemAdapter(Context context, List<OverviewItemType> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_overview_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        OverviewItemType type = mDatas.get(position);
        holder.iv.setImageResource(type.getsImageId());
        holder.typeTv.setText(type.getType());
        double percentage = type.getPercentage();
        String pert = doubleUtils.ratioToPercentage(percentage);
        holder.percentageTv.setText(pert);
        holder.sumTv.setText("RM " + String.format("%.2f",type.getSum()));
        holder.sumTv.setTextColor(type.getKind() == 0 ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.green_5cd65c));
        return convertView;
    }

    class ViewHolder{
        TextView typeTv, percentageTv, sumTv;
        ImageView iv;

        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_overview_title);
            percentageTv = view.findViewById(R.id.item_overview_percentage);
            sumTv = view.findViewById(R.id.item_overview_money);
            iv = view.findViewById(R.id.item_overview_iv);
        }
    }
}
