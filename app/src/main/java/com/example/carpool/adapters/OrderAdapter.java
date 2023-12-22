package com.example.carpool.adapters;


import static com.example.carpool.items.OrderItem.convertDateFormat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carpool.items.OrderItem;
import com.example.carpool.R;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ItemViewHolder>{
    private ArrayList<OrderItem> itemList;
    private ItemAdapter.onItemClickListener mListener;
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;
        public TextView mTextView7;
        public TextView mTextView8;
        public TextView mTextView9;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textViewOrderId);
            mTextView2 = itemView.findViewById(R.id.textViewSource);
            mTextView3 = itemView.findViewById(R.id.textViewDestination);
            mTextView4 = itemView.findViewById(R.id.textViewOrderPrice);
            mTextView5 = itemView.findViewById(R.id.textViewOrderState);
            mTextView6 = itemView.findViewById(R.id.textViewOrderTime);
            mTextView7 = itemView.findViewById(R.id.textViewDriverName);
            mTextView8 = itemView.findViewById(R.id.textViewCarPlate);
            mTextView9 = itemView.findViewById(R.id.textViewOrderDate);
        }
    }

    public OrderAdapter(ArrayList<OrderItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        OrderItem currentItem = itemList.get(position);

        holder.mTextView1.setText(currentItem.getOrderid());
        holder.mTextView2.setText(currentItem.getSource());
        holder.mTextView3.setText(currentItem.getDestination());
        holder.mTextView4.setText(currentItem.getOrderPrice());
        holder.mTextView5.setText(currentItem.getOrderState());
        holder.mTextView6.setText(currentItem.getOrderTime());
        holder.mTextView7.setText(currentItem.getDriverName());
        holder.mTextView8.setText(currentItem.getCarPlate());
        holder.mTextView9.setText(convertDateFormat(currentItem.getTripDate()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

