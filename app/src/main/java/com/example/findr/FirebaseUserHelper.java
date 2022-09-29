package com.example.findr;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseUserHelper {
    private static final String TAG = "FirebaseUserHelper";
    private DatabaseReference userDatabaseReference;
    private DatabaseReference interestDatabaseReference;
    private DatabaseReference chatDatabaseReference;

    public FirebaseUserHelper(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        userDatabaseReference = db.getReference().child(UserInfo.class.getSimpleName());
        interestDatabaseReference = db.getReference().child(Interest.class.getSimpleName());
        chatDatabaseReference = db.getReference().child(Chat.class.getSimpleName());
    }

    public String add(Chat chat){
        //Adds chat to Firebase and chatId to respective users
        String key = chatDatabaseReference.push().getKey();
        chatDatabaseReference.child(key).setValue(chat);

        return key;
    }

    public String add(UserInfo u){
       String key =  userDatabaseReference.push().getKey();
       userDatabaseReference.child(key).setValue(u);
       return key;
    }

    public String add(Interest i){
        String key = interestDatabaseReference.push().getKey();
        interestDatabaseReference.child(key).setValue(i);
        return key;
    }

    public void updateUser (UserInfo u, String key){
        userDatabaseReference.child(key).setValue(u);
    }

    public void updateChat(Chat chat, String key){
        chatDatabaseReference.child(key).setValue(chat);
    }

    public void updateChat (Message message, String key){
        Log.d(TAG, "updateChat: called.");
        Query chatQuery = chatDatabaseReference.orderByKey().equalTo(key);

        chatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d : snapshot.getChildren()){
                        Chat chat = d.getValue(Chat.class);
                        ArrayList<Message> messages = new ArrayList<>();

                        try{
                            if(!chat.getMessages().isEmpty()){
                                messages = chat.getMessages();
                            }
                        }
                        catch (NullPointerException e){
                            //chat.setMessages(messages)
                            //First message


                        }
                        messages.add(message);
                        chat.setMessages(messages);
                        updateChat(chat,key);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void removeUserFriendRequest(FriendRequest friendRequest, String userKey){
        Log.d(TAG, "removeUserFriendRequest: called.");
        Query userQuery = userDatabaseReference.orderByKey().equalTo(userKey);
        
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    UserInfo u = d.getValue(UserInfo.class);
                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    
                    try {
                        if(!u.getFriendRequests().isEmpty()){
                            friendRequests = u.getFriendRequests();
                        }
                    }
                    catch (NullPointerException e){
                        Log.d(TAG, "onDataChange: friendRequests is empty");
                        break;
                    }
                    for(int i = 0; i<friendRequests.size();i++){
                        if(friendRequests.get(i).getSenderUid().equals(friendRequest.getSenderUid())){
                            friendRequests.remove(i);
                            break;
                        }
                    }
                    u.setFriendRequests(friendRequests);
                    updateUser(u,userKey);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void updateUserFriendList(String friendKey, String userKey){
        Log.d(TAG, "updateUserFriendList: called.");
        Query userQuery = userDatabaseReference.orderByKey().equalTo(userKey);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    ArrayList<String> friendList = new ArrayList<>();
                    UserInfo u = d.getValue(UserInfo.class);

                    try{
                        if(!u.getFriendList().isEmpty()){
                            friendList = u.getFriendList();
                        }
                    }
                    catch (NullPointerException e){
                        Log.d(TAG, "onDataChange: No friends Sadge...");
                    }

                    if(!containsFriend(friendKey,friendList)){
                        friendList.add(friendKey);
                        u.setFriendList(friendList);
                        updateUser(u,userKey);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateUserFriendRequests(FriendRequest friendRequest, String targetUserKey){
        Log.d(TAG, "updateUserFriendRequests: called.");
        Query userQuery = userDatabaseReference.orderByKey().equalTo(targetUserKey);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    ArrayList<FriendRequest> friendRequests = new ArrayList<>();
                    UserInfo u = d.getValue(UserInfo.class);

                    try{
                        if(!u.getFriendRequests().isEmpty()){
                            friendRequests = u.getFriendRequests();
                        }
                    }
                    catch (NullPointerException e){
                        Log.d(TAG, "onDataChange: NullPointerException");
                    }

                    if(!containsFriendRequest(friendRequest,friendRequests)){
                        friendRequests.add(friendRequest);
                        u.setFriendRequests(friendRequests);
                        updateUser(u,targetUserKey);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void updateUserChatIds(String chatId, String userKey){
        Log.d(TAG, "updateUserChatIds: called.");
        Query userQuery = userDatabaseReference.orderByKey().equalTo(userKey);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot d : snapshot.getChildren()){
                       ArrayList<String> chatIds = new ArrayList<>();
                       UserInfo u = d.getValue(UserInfo.class);
                        Log.d(TAG, "onDataChange: "+ u.toString());

                       try{
                           if(!u.getChatsIds().isEmpty()){
                               chatIds = u.getChatsIds();
                           }
                       }
                       catch (NullPointerException e ){
                           Log.d(TAG, "onDataChange: NullPointerException");
                       }

                       if(!containsChatId(chatIds,chatId)){
                           chatIds.add(chatId);
                           u.setChatsIds(chatIds);
                           updateUser(u,userKey);
                       }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: ");

            }
        });
    }


    public void updateUserInterest(String interestId, String key) {
        Log.d("DEBUG", "Running updateUserInterest");
        Query userQuery = userDatabaseReference.orderByKey().equalTo(key);

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    Log.d("WHAT","WHAT");
                }
                for(DataSnapshot d: snapshot.getChildren()){

                    ArrayList<String> updateInterestIdArr = new ArrayList<>();

                    UserInfo u = d.getValue(UserInfo.class);
                    Log.d("DEBUG",u.toString());
                    try{
                        if(!u.getInterestIdArr().isEmpty()){
                            updateInterestIdArr = u.getInterestIdArr();
                        }

                    }catch (NullPointerException e){
                        u.setInterestIdArr(updateInterestIdArr);
                    }

                    if(!containsInterestId(updateInterestIdArr,interestId)){
                        updateInterestIdArr.add(interestId);
                        u.setInterestIdArr(updateInterestIdArr);
                        updateUser(u,key);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean containsFriend(String friendUid, ArrayList<String> friendList){
        for(String s : friendList){
            if(s.equals(friendUid)){
                return true;
            }
        }
        return false;
    }

    public boolean containsFriendRequest (FriendRequest friendRequest , ArrayList<FriendRequest> friendRequests){
        String senderUid = friendRequest.getSenderUid();

        for(FriendRequest fr : friendRequests){
            if(fr.getSenderUid().equals(senderUid)){
                return true;
            }
        }
        return false;
    }

    public boolean containsInterestId(ArrayList<String> interestIdArr, String strIn){
        for(String s : interestIdArr){
            if(strIn.equals(s)){
                return true;
            }
        }
        return false;
    }

    public boolean containsChatId(ArrayList<String> chatIds, String chatId){
        for(String s : chatIds){
            if(chatId.equals(s)){
                return true;
            }
        }
        return false;
    }

    public void logUserKeys(){
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    Log.d("KEY",d.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

   /* public ArrayList<Interest> returnUserInterests(UserInfo userInfo){

        ArrayList<String> userInterestIdList = userInfo.getInterestIdArr();
        ArrayList<Interest> returnInterestList = new ArrayList<>();

        interestDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    //Contains interest key
                    if(userInterestIdList.contains(d.getKey())){
                        Interest i = d.getValue(Interest.class);
                        returnInterestList.add(i);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return returnInterestList;
    }*/


    public void readUserData(FirebaseUserCallback firebaseUserCallback){
        ArrayList<UserInfo> userInfoArrayList = new ArrayList<>();
        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    UserInfo u = d.getValue(UserInfo.class);
                    userInfoArrayList.add(u);
                }
                firebaseUserCallback.onCallBack(userInfoArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "Failed to read value.", error.toException());

            }
        });
    }



    private interface FirebaseInterestCallback{
        void onCallBack(ArrayList<Interest> interestArrayList);
    }

    public interface FirebaseUserCallback{
        void onCallBack(ArrayList<UserInfo> userInfoArrayList);
    }


    public DatabaseReference getUserDatabaseReference() {
        return userDatabaseReference;
    }

    public void setUserDatabaseReference(DatabaseReference userDatabaseReference) {
        this.userDatabaseReference = userDatabaseReference;
    }

    public DatabaseReference getInterestDatabaseReference() {
        return interestDatabaseReference;
    }

    public void setInterestDatabaseReference(DatabaseReference interestDatabaseReference) {
        this.interestDatabaseReference = interestDatabaseReference;
    }

    public DatabaseReference getChatDatabaseReference() {
        return chatDatabaseReference;
    }

    public void setChatDatabaseReference(DatabaseReference chatDatabaseReference) {
        this.chatDatabaseReference = chatDatabaseReference;
    }
}
