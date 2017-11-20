package com.example.roncherian.homework07;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

/**
 * Created by roncherian on 17/11/17.
 */

public class User {
    String name;
    String userId;

    @Exclude
    boolean isGoogleUser = false;

    public boolean isGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public boolean getiRequested() {
        return iRequested;
    }

    public void setiRequested(boolean iRequested) {
        this.iRequested = iRequested;
    }

    @Exclude
    public boolean iRequested = false;

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    String dob;

    ArrayList<String>friends = new ArrayList<>();
    ArrayList<String>pendingFriendRequests = new ArrayList<>();
    ArrayList<String>requestedFriends = new ArrayList<>();

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getPendingFriendRequests() {
        return pendingFriendRequests;
    }

    public void setPendingFriendRequests(ArrayList<String> pendingFriendRequests) {
        this.pendingFriendRequests = pendingFriendRequests;
    }

    public ArrayList<String> getRequestedFriends() {
        return requestedFriends;
    }

    public void setRequestedFriends(ArrayList<String> requestedFriends) {
        this.requestedFriends = requestedFriends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User)obj;
        return this.getUserId().equals(user.getUserId());
    }
}
