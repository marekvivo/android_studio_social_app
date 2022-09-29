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

public class ProfileInterestRecyclerViewAdapter extends RecyclerView.Adapter<ProfileInterestRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ProfileInterestRecycler";

    private Context context;
    private ArrayList<Interest> interestArrayList;

    public ProfileInterestRecyclerViewAdapter(Context context, ArrayList<Interest> interestArrayList) {
        this.context = context;
        this.interestArrayList = interestArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_interest_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileInterestRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        holder.description.setText(interestArrayList.get(position).getInterest());
        holder.genreIdList.setText(String.valueOf(interestArrayList.get(position).toDatabaseGenreId()));
    }

    @Override
    public int getItemCount() {
        return interestArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView genreIdList;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.profile_interest_description_textview);
            genreIdList = itemView.findViewById(R.id.profile_interest_genreId_textview);
            parentLayout = itemView.findViewById(R.id.profile_interest_item_layout);

        }
    }
}
