package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatPage extends AppCompatActivity {
    private static final String TAG = "ChatPage";

    private Button btnCompose;

    private String userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        btnCompose = findViewById(R.id.btn_chat_compose);
        userKey = getIntent().getStringExtra("fbUserId");


        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ChatPage.this, ComposeMessagePage.class);
                intent.putExtra("fbUserId",userKey);
                startActivity(intent);*/

                FirebaseUserHelper fb = new FirebaseUserHelper();
                /*String targetUser = "-MyuGdRP8nEhPaB2YRC9";
                String userkey = "-Mx7Lre_GrCEHM03dAWp";*/

                String userkey = "-MyuGdRP8nEhPaB2YRC9";
                String targetUser = "-Mx7Lre_GrCEHM03dAWp";

                Chat chat = new Chat(userkey,targetUser);
                String chatId = fb.add(chat);
                fb.updateUserChatIds(chatId,userkey);
                fb.updateUserChatIds(chatId,targetUser);
                chat.sendMessage(userkey,"Hi There");
                fb.updateChat(chat,chatId);
            }
        });
        FirebaseUserHelper fb = new FirebaseUserHelper();

        Query query = fb.getUserDatabaseReference().orderByKey().equalTo(userKey);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: query single user");

                    fb.getChatDatabaseReference().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d(TAG, "onDataChange: chats");
                            UserInfo u = d.getValue(UserInfo.class);
                            ArrayList<String> chatIds = new ArrayList<>();

                            try {
                                if(!u.getChatsIds().isEmpty()){
                                    chatIds = u.getChatsIds();
                                }
                            }
                            catch (NullPointerException e){
                                Log.d(TAG, "onDataChange: no Chats");
                                return;
                            }

                            ArrayList<Chat> chats = new ArrayList<>();

                            for(DataSnapshot d1 : snapshot.getChildren()){
                                if(containsKey(chatIds,d1.getKey())){
                                    chats.add(d1.getValue(Chat.class));
                                }
                            }
                            ArrayList<String> uidList = chatsToUidList(chats, userKey);
                            ArrayList<String> finalChatIds = chatIds;
                            fb.getUserDatabaseReference().addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d(TAG, "onDataChange: retrieve users "+chats.get(0).getMessages().get(0));
                                    ArrayList<String> chatUsernames = uidList;
                                    for(DataSnapshot d : snapshot.getChildren()){
                                        if(containsKey(uidList,d.getKey())){
                                            chatUsernames = uidToUsername(chatUsernames,d.getValue(UserInfo.class).getUserName(),d.getKey());
                                        }
                                    }
                                    initChatRecyclerView(chatUsernames,chats, finalChatIds);
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private ArrayList<String> uidToUsername(ArrayList<String> uidList, String username, String uid){
        ArrayList<String> returnList = new ArrayList<>();
        for(String s : uidList){
            if(s.equals(uid)){
                returnList.add(username);
            }
            else{
                returnList.add(s);
            }
        }
        return returnList;
    }


    private ArrayList<String> chatsToUidList(ArrayList<Chat> chats, String userKey){
        ArrayList<String> uidList = new ArrayList<>();
        for(Chat c : chats){
            if(c.getUserId1().equals(userKey)){
                uidList.add(c.getUserId2());
            }
            else{
                uidList.add(c.getUserId1());
            }

        }
        return uidList;
    }

    private boolean containsKey(ArrayList<String> chatIds, String chatId){
        for(String s : chatIds){
            if(chatId.equals(s)){
                return true;
            }
        }
        return false;
    }

    private void initChatRecyclerView(ArrayList<String> chatUserNames ,ArrayList<Chat> chats, ArrayList<String> chatIds){
        Log.d(TAG, "initChatRecyclerView: called");
        RecyclerView recyclerView = findViewById(R.id.chat_recyclerView);
        ChatRecyclerViewAdapter adapter = new ChatRecyclerViewAdapter(this,userKey,chatUserNames,chats, chatIds);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}