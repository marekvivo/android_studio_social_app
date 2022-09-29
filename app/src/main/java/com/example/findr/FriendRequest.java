package com.example.findr;

public class FriendRequest {

    private String senderUid;
    private String senderUsername;

    public FriendRequest(){}

    public FriendRequest(String senderUid, String senderUsername) {
        this.senderUid = senderUid;
        this.senderUsername = senderUsername;
    }


    public String getSenderUid() {
        return senderUid;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

}
