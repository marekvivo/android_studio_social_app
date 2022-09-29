package com.example.findr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

public class MatchesRecyclerViewAdapter extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "MatchesRecyclerViewAdap";

    private Context context;
    private ArrayList<UserInfo> userNameList = new ArrayList<>();
    private ArrayList<Double> percentageList = new ArrayList<>();
    private String userKey;
    private String username;

    public MatchesRecyclerViewAdapter(Context context, ArrayList<UserInfo> userNameList, ArrayList<Double> percentageList, String userKey, String username) {
        this.context = context;
        this.userNameList = userNameList;
        this.percentageList = percentageList;
        this.userKey = userKey;
        this.username = username;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_matchlistitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.userName.setText(userNameList.get(position).getUserName());
        Log.d(TAG, "onBindViewHolder: " + holder.userName.getText());
        holder.percentage.setText(Math.round(percentageList.get(position))+"% Matched");

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btnAdd.setClickable(false);
                holder.btnAdd.setBackgroundColor(Color.GRAY);

                FirebaseUserHelper fb = new FirebaseUserHelper();
                fb.getUserDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String targetUserName = userNameList.get(holder.getAdapterPosition()).getUserName();
                        String targetUid = "";
                        for(DataSnapshot d : snapshot.getChildren()){
                            if(d.getValue(UserInfo.class).getUserName().equals(targetUserName)){
                                targetUid = d.getKey();
                                break;
                            }
                        }

                        if(!targetUid.isEmpty()){
                            FriendRequest friendRequest = new FriendRequest(userKey,username);
                            fb.updateUserFriendRequests(friendRequest,targetUid);
                            Toast.makeText(context,userNameList.get(holder.getAdapterPosition()).getUserName()+" Added",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return userNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        TextView percentage;
        Button btnAdd;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.match_name_textview);
            percentage = itemView.findViewById(R.id.match_percentage_textview);
            btnAdd = itemView.findViewById(R.id.btn_match_add);
            parentLayout = itemView.findViewById(R.id.match_recycler_layout);


        }
    }
}
