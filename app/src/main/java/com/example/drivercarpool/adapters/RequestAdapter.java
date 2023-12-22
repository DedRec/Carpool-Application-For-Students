package com.example.drivercarpool.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercarpool.R;
import com.example.drivercarpool.items.RequestItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ItemViewHolder>{
    private ArrayList<RequestItem> itemList;
    private RequestAdapter.onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position);
        void onConfirmClick(int position);
    }

    public void setOnItemListener(RequestAdapter.onItemClickListener listener){
        mListener = listener;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public Button confirmBtn;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;
        public TextView mTextView7;
        public TextView mTextView8;


        public ItemViewHolder(@NonNull View itemView, RequestAdapter.onItemClickListener listener) {
            super(itemView);
            confirmBtn = itemView.findViewById(R.id.confirmButton);
            mTextView1 = itemView.findViewById(R.id.textViewOrderId);
            mTextView2 = itemView.findViewById(R.id.textViewSource);
            mTextView3 = itemView.findViewById(R.id.textViewDestination);
            mTextView4 = itemView.findViewById(R.id.textViewOrderPrice);
            mTextView5 = itemView.findViewById(R.id.textViewOrderTime);
            mTextView6 = itemView.findViewById(R.id.textViewClientName);
            mTextView7 = itemView.findViewById(R.id.textViewCarPlate);
            mTextView8 = itemView.findViewById(R.id.textViewState);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                            //itemView.setVisibility(View.GONE);
                        }
                    }
                }
            });
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getBindingAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onConfirmClick(position);
                        }
                    }
                }
            });
        }
    }

    public RequestAdapter(ArrayList<RequestItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item, parent,false);
        ItemViewHolder ivh = new ItemViewHolder(v, mListener);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        RequestItem currentItem = itemList.get(position);


        holder.mTextView1.setText(currentItem.getOrderid());
        holder.mTextView2.setText(currentItem.getSource());
        holder.mTextView3.setText(currentItem.getDestination());
        holder.mTextView4.setText(currentItem.getOrderPrice());
        holder.mTextView5.setText(currentItem.getOrderTime());
        holder.mTextView7.setText(currentItem.getCarPlate());
        holder.mTextView8.setText(currentItem.getOrderState());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("userid").equalTo(currentItem.getUserid());
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String clientName =  snapshot.child(currentItem.getUserid()).child("name").getValue(String.class);
                    holder.mTextView6.setText(clientName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
