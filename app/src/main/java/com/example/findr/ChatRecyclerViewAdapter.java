package com.example.findr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ChatRecyclerView";


    private Context context;
    private String userFbId;
    private ArrayList<String> friendUsernames; //same length as chat size just to show the username
    private ArrayList<Chat> chats;
    private ArrayList<String>chatIds;

    public ChatRecyclerViewAdapter(Context context, String userFbId, ArrayList<String> friendUsernames, ArrayList<Chat> chats, ArrayList<String> chatIds) {
        this.context = context;
        this.userFbId = userFbId;
        this.friendUsernames = friendUsernames;
        this.chats = chats;
        this.chatIds = chatIds;
    }


    @NonNull
    @Override
    public ChatRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_people_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        if(chats.size() == friendUsernames.size()){
            Log.d(TAG, "onBindViewHolder: chats same size");
            Chat chat = chats.get(position);
            holder.username.setText(friendUsernames.get(position));
            Message lastMessage = chat.getMessages().get(chat.getMessages().size()-1);
            if(!lastMessage.getSenderUid().equals(userFbId)){
                holder.youTag.setVisibility(View.INVISIBLE);
            }
            holder.lastMessage.setText(lastMessage.getMessage());


            holder.parentLayout.setBackgroundColor(Color.GRAY);
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO set onclick functionality
                    String chatId = chatIds.get(holder.getAdapterPosition());
                    Intent intent = new Intent(context,MessagingPage.class);
                    intent.putExtra("fbUserId",userFbId);
                    intent.putExtra("chatId",chatId);
                    context.startActivity(intent);


                }
            });
        }
        else{
            Log.d(TAG, "onBindViewHolder: wrong" + friendUsernames.toString());
            Log.d(TAG, "onBindViewHolder: " + friendUsernames.size() + chats.size());
        }



    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView username;
        TextView youTag;
        TextView lastMessage;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.chat_item_username);
            youTag = itemView.findViewById(R.id.chat_item_you_tag);
            lastMessage = itemView.findViewById(R.id.chat_item_message);
            parentLayout = itemView.findViewById(R.id.chat_people_item_layout);
        }
    }
}
