package com.example.carpool;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private ArrayList<TripItem> itemList;
    private ItemAdapter.onItemClickListener mListener;
    public interface onItemClickListener {
        void onItemClick(int position);
        void onBookClick(int position);
    }

    public void setOnItemListener(ItemAdapter.onItemClickListener listener){
        mListener = listener;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public Button btn;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView tripInfo;
        public LinearLayout layout;

        public ItemViewHolder(@NonNull View itemView, ItemAdapter.onItemClickListener listener) {
            super(itemView);
            btn = itemView.findViewById(R.id.trip_btn);
            mTextView1 = itemView.findViewById(R.id.trip_item_from);
            mTextView2 = itemView.findViewById(R.id.trip_item_to);
            mTextView3 = itemView.findViewById(R.id.time);
            tripInfo = itemView.findViewById(R.id.trip_details);
            layout = itemView.findViewById(R.id.trip_details_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            int x = (tripInfo.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                            TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                            tripInfo.setVisibility(x);
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onBookClick(position);
                        }
                    }
                }
            });
        }
    }

    public ItemAdapter(ArrayList<TripItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TripItem currentItem = itemList.get(position);

        holder.mTextView1.setText(currentItem.getFrom());
        holder.mTextView2.setText(currentItem.getTo());
        holder.mTextView3.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
