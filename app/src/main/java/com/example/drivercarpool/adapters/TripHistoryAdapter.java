package com.example.drivercarpool.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercarpool.R;
import com.example.drivercarpool.helpers.HelperTrip;

import java.util.ArrayList;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ItemViewHolder>{
    private ArrayList<HelperTrip> itemList;

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewTripId);
            mTextView2 = itemView.findViewById(R.id.textViewSource);
            mTextView3 = itemView.findViewById(R.id.textViewDestination);
            mTextView4 = itemView.findViewById(R.id.textViewTripTime);
            mTextView5 = itemView.findViewById(R.id.textViewCarPlate);
            mTextView6 = itemView.findViewById(R.id.textViewTripState);

        }
    }

    public TripHistoryAdapter(ArrayList<HelperTrip> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_history_item, parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        HelperTrip currentItem = itemList.get(position);

        holder.mTextView1.setText(currentItem.getTripid());
        holder.mTextView2.setText(currentItem.getFrom());
        holder.mTextView3.setText(currentItem.getTo());
        holder.mTextView4.setText(currentItem.getTime());
        holder.mTextView5.setText(currentItem.getCarPlate());
        holder.mTextView6.setText(currentItem.getTripState());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
