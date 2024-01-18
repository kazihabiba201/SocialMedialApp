package com.socialmedia.Model;

public class PostModel {
    private int react;

    public int getReact() {
        return react;
    }

    public void setReact(int react) {
        this.react = react;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    private int commentCount;



    public PostModel(int commentCount) {

        this.commentCount = commentCount;
    }

    public PostModel(){

    }
    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostUpdate() {
        return postUpdate;
    }

    public void setPostUpdate(String postUpdate) {
        this.postUpdate = postUpdate;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public PostModel(String postId, String postImage, String postedBy, String postUpdate, long postedAt) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postUpdate = postUpdate;
        this.postedAt = postedAt;
    }

    private String postId , postImage, postedBy, postUpdate;
private long postedAt;
    }

