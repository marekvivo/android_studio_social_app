package com.example.findr;

import java.util.ArrayList;

public class UserInfo {

    private String firstName, lastName, email, userName, salt, password;
    //private Genre genreObj;
    private ArrayList<String> interestIdArr;
    private ArrayList<String> chatsIds;
    private ArrayList<FriendRequest> friendRequests;
    private ArrayList<String> friendList;

    public UserInfo(){
        //Firebase default constructor needed
    }

    public UserInfo(String firstName, String lastName, String email, String userName, String salt, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.interestIdArr = new ArrayList<>();
        this.chatsIds = new ArrayList<>();
        this.friendRequests = new ArrayList<>();
        this.friendList = new ArrayList<>();
        //this.genreObj = new Genre("default");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public Genre getGenreObj() {
        return genreObj;
    }

    public void setGenreObj(Genre genreObj) {
        this.genreObj = genreObj;
    }*/

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public ArrayList<String> getInterestIdArr() {
        return interestIdArr;
    }

    public void setInterestIdArr(ArrayList<String> interestIdArr) {
        this.interestIdArr = interestIdArr;
    }

    public ArrayList<String> getChatsIds() {
        return chatsIds;
    }

    public void setChatsIds(ArrayList<String> chats) {
        this.chatsIds = chats;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(ArrayList<FriendRequest> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
