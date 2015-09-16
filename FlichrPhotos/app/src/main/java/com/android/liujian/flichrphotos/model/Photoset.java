package com.android.liujian.flichrphotos.model;

/**
 * Created by liujian on 15/9/14.
 * A model - photoset
 */
public class Photoset {
    private String mId;
    private String mTitle;
    private String mDescription;
    private String mPhotoCount;
    private String mViewCount;
    private String mCommentCount;
    private String mPrimaryPhotoUrl;

    public Photoset(){
        //default constructor
    }

    public Photoset(String id, String title, String description,
                    String photoCount, String viewCount, String commentCount, String primaryPhotoUrl) {
        mId = id;
        mTitle = title;
        mDescription = description;
        mPhotoCount = photoCount;
        mViewCount = viewCount;
        mCommentCount = commentCount;
        mPrimaryPhotoUrl = primaryPhotoUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPhotoCount() {
        return mPhotoCount;
    }

    public void setPhotoCount(String photoCount) {
        mPhotoCount = photoCount;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    public String getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(String commentCount) {
        mCommentCount = commentCount;
    }

    public String getPrimaryPhotoUrl() {
        return mPrimaryPhotoUrl;
    }

    public void setPrimaryPhotoUrl(String primaryPhotoUrl) {
        mPrimaryPhotoUrl = primaryPhotoUrl;
    }
}
