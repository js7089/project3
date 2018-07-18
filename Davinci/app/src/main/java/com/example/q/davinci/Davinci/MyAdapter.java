package com.example.q.davinci.Davinci;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.q.davinci.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public ArrayList<Double> mDataSet;
    public TextView mTitle;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public MyAdapter(ArrayList<Double> mDataSet) {
        this.mDataSet = mDataSet;

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.darow, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitle.setText(Double.toString( mDataSet.get(position).intValue()));
        if(Double.parseDouble(Double.toString( mDataSet.get(position).intValue())+".0") == mDataSet.get(position)) {
            holder.mTitle.setBackgroundColor(Color.WHITE);
        }else {holder.mTitle.setBackgroundColor(Color.BLACK);}
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle=(TextView) itemView.findViewById(R.id.title);
        }
    }
}
