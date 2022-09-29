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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileFriendRecyclerViewAdapter extends RecyclerView.Adapter<ProfileFriendRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ProfileFriendRecyclerVi";
    private Context context;
    private ArrayList<String> friendList;

    public ProfileFriendRecyclerViewAdapter(Context context, ArrayList<String> friendList) {
        this.context = context;
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public ProfileFriendRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_interest_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFriendRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.order.setText(String.valueOf(position+1));
        holder.username.setText(friendList.get(position));

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView order;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            order = itemView.findViewById(R.id.profile_interest_description_textview);
            username = itemView.findViewById(R.id.profile_interest_genreId_textview);
            parentLayout = itemView.findViewById(R.id.profile_page_interest_recyclerview);
        }
    }
}
