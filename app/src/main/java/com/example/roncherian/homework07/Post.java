package com.example.roncherian.homework07;

/**
 * Created by roncherian on 17/11/17.
 */

public class Post {

    String userName;
    String time;
    String post;
    String userId;
    String postId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object obj) {
        Post post = (Post)obj;
        return this.postId.equals(post.getPostId());
    }
}
