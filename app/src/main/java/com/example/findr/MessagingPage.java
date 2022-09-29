package com.example.findr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagingPage extends AppCompatActivity {
    private static final String TAG = "MessagingPage";

    private Button btnSend;
    private EditText messageInput;

    private String userKey;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_page);

        btnSend = findViewById(R.id.btn_message_send);
        messageInput = findViewById(R.id.message_input);
        userKey = getIntent().getStringExtra("fbUserId");
        chatId = getIntent().getStringExtra("chatId");


        Log.d(TAG, "onCreate: " + chatId);
        FirebaseUserHelper fb = new FirebaseUserHelper();
        Query query = fb.getChatDatabaseReference().orderByKey().equalTo(chatId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d : snapshot.getChildren()){
                    Chat chat = d.getValue(Chat.class);
                    ArrayList<Message> messages = new ArrayList<>();

                    try {
                        if(!chat.getMessages().isEmpty()){
                            messages = chat.getMessages();
                        }
                    }
                    catch (NullPointerException e ){
                        Log.d(TAG, "onDataChange: messages empty");
                    }
                    initMessagingRecyclerView(messages);

                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String senderUid = userKey;

                            String messageToSend = messageInput.getText().toString();
                            Message message = new Message(senderUid,messageToSend);
                            fb.updateChat(message,chatId);

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initMessagingRecyclerView (ArrayList<Message> messages){
        Log.d(TAG, "initMessagingRecyclerView: called.");
        RecyclerView recyclerView = findViewById(R.id.messaging_recyclerView);
        MessagingRecyclerViewAdapter adapter = new MessagingRecyclerViewAdapter(this,userKey,messages);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
    }
}