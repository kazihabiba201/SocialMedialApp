package com.socialmedia.Model;

public class CommentModel {
    private  String commentRv;
    private int commentCount;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public CommentModel(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCommentRv() {
        return commentRv;
    }

    public void setCommentRv(String commentRv) {
        this.commentRv = commentRv;
    }

    public long getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(long commentedAt) {
        this.commentedAt = commentedAt;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public CommentModel() {
        this.commentRv = commentRv;
        this.commentedAt = commentedAt;
        this.commentedBy = commentedBy;
    }

    private  long commentedAt;
    private  String commentedBy;



}
