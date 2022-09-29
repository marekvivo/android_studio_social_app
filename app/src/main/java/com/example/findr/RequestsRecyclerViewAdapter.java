package com.example.findr;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RequestsRecyclerViewAda";

    private Context context;
    private ArrayList<FriendRequest> friendRequests;
    private String userKey;

    public RequestsRecyclerViewAdapter(Context context, ArrayList<FriendRequest> friendRequests, String userKey) {
        this.context = context;
        this.friendRequests = friendRequests;
        this.userKey = userKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_requests_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.senderUserName.setText(friendRequests.get(position).getSenderUsername());
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.addBtn.setClickable(false);
                holder.addBtn.setBackgroundColor(Color.GRAY);

                FirebaseUserHelper fb = new FirebaseUserHelper();
                FriendRequest friendRequest = friendRequests.get(holder.getAdapterPosition());
                String friendKey = friendRequest.getSenderUid();
                String friendUsername = friendRequest.getSenderUsername();
                fb.updateUserFriendList(friendKey,userKey);
                fb.updateUserFriendList(userKey,friendKey);
                Toast.makeText(context, friendUsername + " is now your friend!",Toast.LENGTH_SHORT).show();

                //TODO remove request
                fb.removeUserFriendRequest(friendRequest,userKey);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView senderUserName;
        //TextView percentage;
        Button addBtn;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            senderUserName = itemView.findViewById(R.id.requests_item_senderUserName);
            addBtn = itemView.findViewById(R.id.btn_requests_item_add);
            parentLayout = itemView.findViewById(R.id.requests_recyclerView);
        }
    }
}
