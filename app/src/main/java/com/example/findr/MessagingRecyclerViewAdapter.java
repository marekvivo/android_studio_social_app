package com.example.findr;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagingRecyclerViewAdapter extends RecyclerView.Adapter<MessagingRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MessagingRecyclerViewAd";
    Context context;
    String userKey;
    ArrayList<Message> messages;

    public MessagingRecyclerViewAdapter(Context context, String userKey, ArrayList<Message> messages) {
        this.context = context;
        this.userKey = userKey;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_messaging_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        Log.d(TAG, "onBindViewHolder: " + message.getMessage());

        if(message.getSenderUid().equals(userKey)){
            holder.rightMessage.setText(message.getMessage());
            holder.leftMessage.setVisibility(View.INVISIBLE);
        }
        else{
            holder.leftMessage.setText(message.getMessage());
            holder.rightMessage.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView leftMessage;
        TextView rightMessage;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftMessage = itemView.findViewById(R.id.message_left);
            rightMessage = itemView.findViewById(R.id.message_right);
            parentLayout = itemView.findViewById(R.id.messaging_item_layout);

        }
    }
}
