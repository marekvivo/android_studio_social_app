package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsPage extends AppCompatActivity {

    private static final String TAG = "RequestsPage";

    private String userKey;
    private String username;
    private FirebaseUserHelper fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        userKey = getIntent().getStringExtra("fbUserId");
        username = getIntent().getStringExtra("username");
        fb = new FirebaseUserHelper();

        Query userQuery = fb.getUserDatabaseReference().orderByKey().equalTo(userKey);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) Log.d(TAG, "onDataChange: User not found");

                for(DataSnapshot d : snapshot.getChildren()){
                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    UserInfo u = d.getValue(UserInfo.class);

                    try {
                        if(!u.getFriendRequests().isEmpty()){
                            friendRequests = u.getFriendRequests();
                        }
                    }
                    catch (NullPointerException e){
                        Log.d(TAG, "onDataChange: FriendRequests empty");
                    }

                    if(friendRequests.isEmpty()){
                        //TODO show text that requests are empty
                    }
                    else{
                        initRequestsRecyclerView(friendRequests);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initRequestsRecyclerView(ArrayList<FriendRequest> friendRequests){
        Log.d(TAG, "initRequestsRecyclerView: ");
        RecyclerView recyclerView = findViewById(R.id.requests_recyclerView);
        RequestsRecyclerViewAdapter adapter = new RequestsRecyclerViewAdapter(this,friendRequests,userKey);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}