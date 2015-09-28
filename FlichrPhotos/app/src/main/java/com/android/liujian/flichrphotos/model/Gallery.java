package com.android.liujian.flichrphotos.model;

import java.io.Serializable;

/**
 * Created by liujian on 15/9/11.
 * A model - Gallery
 */
public class Gallery implements Serializable{
    private String mId;
    private String mOwnerId;
    private String mOwnerName;
//    private String mUrl;
    private String mUpdatedTime;
    private String mPrimaryPhotoUrl;
    private String mPhotoCount;
    private String mTitle;
    private String mDescription;
    private String mCommentCount;
    private String mViewCount;

    public Gallery(){
        //default constructor
    }

    public Gallery(String id, String ownerId, String primaryPhotoUrl, String photoCount, String title, String description, String commentCount, String viewCount) {
        mId = id;
        mOwnerId = ownerId;
//        mOwnerName = ownerName;
//        mUrl = url;
        mPrimaryPhotoUrl = primaryPhotoUrl;
        mPhotoCount = photoCount;
        mTitle = title;
        mDescription = description;
        mCommentCount = commentCount;
        mViewCount = viewCount;
    }


    public String getOwnerName() {
        return mOwnerName;
    }

    public void setOwnerName(String ownerName) {
        mOwnerName = ownerName;
    }


    public String getUpdatedTime() {
        return mUpdatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        mUpdatedTime = updatedTime;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

//    public String getUrl() {
//        return mUrl;
//    }
//
//    public void setUrl(String url) {
//        mUrl = url;
//    }

    public String getPrimaryPhotoUrl() {
        return mPrimaryPhotoUrl;
    }

    public void setPrimaryPhotoUrl(String primaryPhotoUrl) {
        mPrimaryPhotoUrl = primaryPhotoUrl;
    }

    public String getPhotoCount() {
        return mPhotoCount;
    }

    public void setPhotoCount(String photoCount) {
        mPhotoCount = photoCount;
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

    public String toString(){
        return mTitle;
    }
}
