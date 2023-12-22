package com.example.drivercarpool.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercarpool.R;
import com.example.drivercarpool.helpers.HelperTrip;


import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ItemViewHolder>{
    private ArrayList<HelperTrip> itemList;
    private onItemClickListener mListener;
    public interface onItemClickListener {
        void onStartClick(int position);
        void onCompleteClick(int position);
        void onCancelClick(int position);

    }

    public void setOnItemListener(onItemClickListener listener){
        mListener = listener;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public Button startBtn, completeBtn, cancelBtn;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView tripInfo;
        public LinearLayout layout;

        public ItemViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            startBtn = itemView.findViewById(R.id.start_btn);
            completeBtn = itemView.findViewById(R.id.complete_btn);
            cancelBtn = itemView.findViewById(R.id.cancel_btn);
            mTextView1 = itemView.findViewById(R.id.trip_item_from);
            mTextView2 = itemView.findViewById(R.id.trip_item_to);
            mTextView3 = itemView.findViewById(R.id.time);
            tripInfo = itemView.findViewById(R.id.trip_details);
            layout = itemView.findViewById(R.id.trip_details_layout);

            startBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onStartClick(position);
                        }
                    }
                }
            });
            completeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCompleteClick(position);
                        }
                    }
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCancelClick(position);
                        }
                    }
                }
            });
        }
    }

    public StatusAdapter(ArrayList<HelperTrip> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_status_item, parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        HelperTrip currentItem = itemList.get(position);

        holder.mTextView1.setText(currentItem.getFrom());
        holder.mTextView2.setText(currentItem.getTo());
        holder.mTextView3.setText(currentItem.getTime());
        if (TextUtils.equals(currentItem.getTripState(),"Not Started")){
            holder.startBtn.setVisibility(View.VISIBLE);
            holder.completeBtn.setVisibility(View.GONE);
        }else if (TextUtils.equals(currentItem.getTripState(),"Started")){
            holder.startBtn.setVisibility(View.GONE);
            holder.completeBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
