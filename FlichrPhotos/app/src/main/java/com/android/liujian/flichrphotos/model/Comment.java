package com.android.liujian.flichrphotos.model;

/**
 * Created by liujian on 15/9/15.
 * A model - comment
 */
public class Comment {
    private String mPhotoId;
    private String mCommentId;
    private String mContent;
    private String mAuthorId;
    private String mAuthorName;

    public Comment(){
        //default constructor
    }


    public Comment(String photoId, String commentId, String content, String authorId) {
        mPhotoId = photoId;
        mCommentId = commentId;
        mContent = content;
        mAuthorId = authorId;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public void setAuthorName(String authorName) {
        mAuthorName = authorName;
    }

    public String getPhotoId() {
        return mPhotoId;
    }

    public void setPhotoId(String photoId) {
        mPhotoId = photoId;
    }

    public String getCommentId() {
        return mCommentId;
    }

    public void setCommentId(String commentId) {
        mCommentId = commentId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getAuthorId() {
        return mAuthorId;
    }

    public void setAuthorId(String authorId) {
        mAuthorId = authorId;
    }
}
