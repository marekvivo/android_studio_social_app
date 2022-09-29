package com.example.findr;

public class Message {

    private String senderUid;
    private String message;

    public Message(){}

    public Message(String senderUid, String message){
        this.senderUid = senderUid;
        this.message = message;
    }

    public boolean messageEmpty(){
        return (senderUid.isEmpty()||message.isEmpty());
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
