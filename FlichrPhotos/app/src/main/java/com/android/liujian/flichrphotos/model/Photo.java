package com.android.liujian.flichrphotos.model;


/**
 * Created by lj on 2015/8/14.
 * A photo class storing information of photo
 */
public class Photo {
    private String mTitle;
    private String mUrl;
    private String mId;
    private String mOwnerId;
    private String mCommentCount;
    private String mViewCount;
    private String mFavCount;
    private String mTakenTime;

    public Photo(){
        //default constructor
    }

    public String getTakenTime() {
        return mTakenTime;
    }

    public void setTakenTime(String takenTime) {
        mTakenTime = takenTime;
    }

    public String getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(String commentCount) {
        mCommentCount = commentCount;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    public String getFavCount() {
        return mFavCount;
    }

    public void setFavCount(String favrCount) {
        mFavCount = favrCount;
    }

    public String toString(){
        return mTitle;
    }

    public void setTitle(String caption){
        mTitle = caption;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String owner){
        mOwnerId = owner;
    }

}
