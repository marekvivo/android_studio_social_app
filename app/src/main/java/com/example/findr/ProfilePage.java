package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    private static final String TAG = "ProfilePage";

    private TextView usernameTxtView;
    private Button btnAddInterest;
    private Button btnMatch;
    private Button btnChat;
    private Button btnRequests;

    //private ArrayList<UserInfo> userInfoList;
    private UserDataBaseHelper dbHelper;

    private String userkey;
    private String username;
    //private UserInfo userInfo;

    private FirebaseUserHelper fb = new FirebaseUserHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);



        /*dbHelper = new UserDataBaseHelper(ProfilePage.this, "user.db", null, 1);
        userInfoList = dbHelper.getAll();*/
        userkey = getIntent().getStringExtra("fbUserId");

        usernameTxtView = findViewById(R.id.profile_username);
        btnAddInterest = findViewById(R.id.btn_profile_addInterest);
        btnMatch = findViewById(R.id.btn_profile_match);
        btnChat = findViewById(R.id.btn_profile_chat);
        btnRequests = findViewById(R.id.btn_profile_requests);

        Query query = fb.getUserDatabaseReference().orderByKey().equalTo(userkey);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: called");
                if(!snapshot.exists()){
                    //No user found in database
                    Log.d(TAG, "onDataChange: error loading user");
                }
                for(DataSnapshot d : snapshot.getChildren()){
                    if(d.getKey().equals(userkey)){
                        username = d.getValue(UserInfo.class).getUserName();
                        usernameTxtView.setText(username);
                        //Load interests
                        //Load friends
                        //TODO Load friends
                        fb.getInterestDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<String> interestIdList = new ArrayList<>();
                                try {
                                    if(!d.getValue(UserInfo.class).getInterestIdArr().isEmpty()){
                                        interestIdList = d.getValue(UserInfo.class).getInterestIdArr();
                                    }
                                }
                                catch (NullPointerException e ){
                                    Log.d(TAG, "onDataChange: user interestIdArr is empty");
                                }
                                ArrayList<Interest> outputInterestList = new ArrayList<>();
                                for(DataSnapshot d1 : snapshot.getChildren()){
                                    for(String s : interestIdList){
                                        if(d1.getKey().equals(s)){
                                            //is correct interest
                                            Interest i = d1.getValue(Interest.class);
                                            outputInterestList.add(i);
                                        }
                                    }
                                }
                                initInterestRecyclerView(outputInterestList);

                                fb.getUserDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ArrayList<String> friendUserNames = new ArrayList<>();
                                        ArrayList<String> friendUids = new ArrayList<>();

                                        try {
                                            if(!d.getValue(UserInfo.class).getFriendList().isEmpty()){
                                                friendUids = d.getValue(UserInfo.class).getFriendList();
                                            }
                                        }
                                        catch (NullPointerException e ){
                                            Log.d(TAG, "onDataChange: user friendlist is empty");
                                        }

                                        if(!friendUids.isEmpty()){
                                            for(DataSnapshot d2 : snapshot.getChildren()){
                                                if(friendUids.contains(d2.getKey())){
                                                    friendUserNames.add(d2.getValue(UserInfo.class).getUserName());
                                                }
                                            }
                                            initFriendRecyclerView(friendUserNames);

                                        }
                                        else{
                                            //Friendlist is empty


                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }


                        });




                        btnAddInterest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProfilePage.this, InterestAddPage.class);
                                intent.putExtra("fbUserId", userkey);
                                startActivity(intent);
                            }
                        });

                        btnMatch.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProfilePage.this, Matches.class);
                                intent.putExtra("username",username);
                                intent.putExtra("fbUserId",userkey);
                                startActivity(intent);
                            }
                        });

                        btnChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                //Test Message
                                /*String targetUser = "-Mx7Lre_GrCEHM03dAWp";
                                Chat chat = new Chat(userkey,targetUser);
                                String chatId = fb.add(chat);
                                fb.updateUserChatIds(chatId,userkey);
                                fb.updateUserChatIds(chatId,targetUser);
                                chat.sendMessage(userkey,"Hi There");
                                fb.updateChat(chat,chatId);
*/
                                Intent intent = new Intent(ProfilePage.this, ChatPage.class);
                                intent.putExtra("fbUserId", userkey);
                                startActivity(intent);

                            }
                        });

                        btnRequests.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ProfilePage.this, RequestsPage.class);
                                intent.putExtra("username", username);
                                intent.putExtra("fbUserId", userkey);
                                startActivity(intent);

                            }
                        });





                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }


    private void initInterestRecyclerView(ArrayList<Interest> interestArrayList) {
        Log.d(TAG, "initInterestRecyclerView: called");
        RecyclerView recyclerView = findViewById(R.id.profile_page_interest_recyclerview);
        ProfileInterestRecyclerViewAdapter adapter = new ProfileInterestRecyclerViewAdapter(this, interestArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initFriendRecyclerView(ArrayList<String> friendUserNames){
        Log.d(TAG, "initFriendRecyclerView: called.");
        RecyclerView recyclerView = findViewById(R.id.profile_friend_recyclerView);
        ProfileFriendRecyclerViewAdapter adapter = new ProfileFriendRecyclerViewAdapter(this, friendUserNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}