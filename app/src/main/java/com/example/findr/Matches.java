package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Matches extends AppCompatActivity {
    private static final String TAG = "Matches";

    private FirebaseUserHelper fb;

    private String userName;

    private UserInfo currentUser;
    private UserInfo userInfo;
    private Score userScore;
    private String userKey;

    private ArrayList<UserInfo> userInfoList = new ArrayList<>();
    private ArrayList<Interest> interestArrayList = new ArrayList<>();

    private ArrayList<Score>    scoreList = new ArrayList<>();
    private ArrayList<Double>   percentageMatchList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        userName = getIntent().getStringExtra("username");
        userKey = getIntent().getStringExtra("fbUserId");

        fb = new FirebaseUserHelper();
        Log.d(TAG, "onCreate: here");
        loadUserData(new FirebaseUserCallback() {
            @Override
            public void onCallBack(ArrayList<UserInfo> userInfoArrayList) {
                Log.d(TAG, "onCallBack: HERE");

                for(UserInfo u : userInfoArrayList){
                    if(u.getUserName().equalsIgnoreCase(userName)){
                        userInfo = u;
                        userInfoArrayList.remove(u);
                        break;
                    }
                }
                currentUser = userInfo;
                //if current user is not null
                if (!(userInfo == null)){
                    Log.d(TAG, "onCallBack: InterestList" + interestArrayList.size());
                    loadInterestData(new FirebaseInterestsCallback(){
                        @Override
                        public void onCallBack(ArrayList<Interest> interestArrayList) {
                            userScore = new Score();
                            //Add score of user
                            for(Interest i : interestArrayList){
                                Log.d(TAG, "onCallBack: USERSCORE ADD" + i.getGenreIdList().toString());
                                userScore.addScores(i.getGenreIdList());
                            }
                            //Adding scores for all other users

                            //Compare user's scores with targets scoreList (Each score in list is an individual user score)
                            loadScores(new FirebaseScoreCallback() {
                                @Override
                                public void onCallBack(ArrayList<Score> scoreArrayList) {
                                    for(Score s : scoreList){
                                        Double percentage = userScore.percentageMatched(s);
                                        percentageMatchList.add(percentage);
                                    }
                                    //Access percentageMatchList and add to recyclerview
                                    Log.d(TAG, "onCallBack: userListSize" + userInfoList.get(0).getUserName());
                                    userScore.printIdMap();
                                    Log.d(TAG, "onCallBack: scoreListSize" + scoreList.size());

                                    Log.d(TAG, "onCallBack: percentageListsize" + percentageMatchList.get(0).toString());
                                    Log.d(TAG, "onCallBack: userScore" + userScore.calculateScore(userScore,scoreList.get(0)));
                                    initRecyclerView();
                                }
                            });
                        }
                    });

                }

            }
        });


    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerView");
        RecyclerView recyclerView = findViewById(R.id.match_recyclerview);
        MatchesRecyclerViewAdapter adapter = new MatchesRecyclerViewAdapter(this,userInfoList,percentageMatchList,userKey,userName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "initRecyclerView: " + adapter.getItemCount());


    }

    private void loadUserData(FirebaseUserCallback firebaseUserCallback) {

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    //String s = d.getValue().toString();
                    UserInfo u = d.getValue(UserInfo.class);
                    userInfoList.add(u);
                    //Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                    //Toast.makeText(MainActivity.this,u.toString(),Toast.LENGTH_LONG).show();
                }
                firebaseUserCallback.onCallBack(userInfoList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        fb.getUserDatabaseReference().addValueEventListener(userListener);


    }

    private void loadScores(FirebaseScoreCallback firebaseScoreCallback){
        fb.getInterestDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(UserInfo u : userInfoList){
                    Score s = new Score();
                    for(DataSnapshot d : snapshot.getChildren()){
                        try {
                            if(!u.getInterestIdArr().isEmpty()){
                                if(u.getInterestIdArr().contains(d.getKey())){
                                    Log.d(TAG, "onDataChange: SCORELOAD");
                                    s.addScores(d.getValue(Interest.class).getGenreIdList());
                                }
                            }
                        }catch (NullPointerException e){

                        }
                    }
                    scoreList.add(s);
                }
                Log.d(TAG, "onDataChange: loadScores" + scoreList.size());
                firebaseScoreCallback.onCallBack(scoreList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadInterestData(FirebaseInterestsCallback firebaseInterestsCallback){

        fb.getInterestDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: called");
                for(DataSnapshot d : snapshot.getChildren()){
                    if(currentUser.getInterestIdArr().contains(d.getKey())) {
                        Interest i = d.getValue(Interest.class);
                        interestArrayList.add(i);
                    }
                }
                firebaseInterestsCallback.onCallBack(interestArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private interface FirebaseScoreCallback{
        void onCallBack(ArrayList<Score> scoreArrayList);
    }



    private interface FirebaseInterestsCallback{
        void onCallBack(ArrayList<Interest> interestArrayList);
    }

    private interface FirebaseUserCallback{
        void onCallBack(ArrayList<UserInfo> userInfoArrayList);
    }


}