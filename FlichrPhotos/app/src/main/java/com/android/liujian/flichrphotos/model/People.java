package com.android.liujian.flichrphotos.model;

/**
 * Created by liujian on 15/9/11.
 * Model - people
 */
public class People {
    private String mId;
    private String mBuddyiconsUrl;
    private String mUserName;
    private String mRealName;
    private String mLocation;

    public People(){
        //default constructor
    }

    public People(String id, String userName, String realName, String location) {
        mId = id;
        mUserName = userName;
        mRealName = realName;
        mLocation = location;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }


    /**
     * http://farm{icon-farm}.staticflickr.com/{icon-server}/buddyicons/{nsid}.jpg
     */
    public void setBuddyIconsUrl(String iconfarm, String iconserver, String nsid){
        mBuddyiconsUrl = "http://farm" + iconfarm + ".staticflickr.com/" + iconserver + "/buddyicons/"
                + nsid + ".jpg";
    }

    public String getBuddyiconsUrl(){
        return mBuddyiconsUrl;
    }

}
