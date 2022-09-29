package com.example.findr;

import java.util.ArrayList;

public class Chat {

    //sets a chat up between 2 users and records the messages between them
    //Message obj consists of the actual message and the sender (used when displaying the chat history)

    private String userId1;//firebase uid
    private String userId2;

    private ArrayList<Message> messages;

    public Chat(){}

    public Chat(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.messages = new ArrayList<>();
    }

    public Chat(String userId1, String userId2, ArrayList<Message> messages) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.messages = messages;
    }

    //Returns true if message was added successfully to messages
    public boolean sendMessage (String senderUid, String message){
        if(message.isEmpty()||senderUid.isEmpty()){
            return false;
        }
        else{
            Message message1 = new Message(senderUid,message);
            messages.add(message1);
            return true;
        }

    }

    public boolean sendMessage (Message message){
        if(message.messageEmpty()){
            return false;
        }
        else{
            messages.add(message);
            return true;
        }
    }

    public String getUserId1() {
        return userId1;
    }

    public void setUserId1(String userId1) {
        this.userId1 = userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public void setUserId2(String userId2) {
        this.userId2 = userId2;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}

