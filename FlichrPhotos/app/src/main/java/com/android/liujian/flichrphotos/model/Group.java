package com.android.liujian.flichrphotos.model;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by liujian on 15/9/11.
 * A model - Group
 */
public class Group {
    private String mId;
    private String mName;
    private String mBuddyiconUrl;
    private Bitmap mBuddyicon;
    private String mMemberCount;
    private String mPoolCount;

    public Group(){
        //default constructor
    }

    public Group(String id, String name, String memberCount, String poolCount) {
        mId = id;
        mName = name;
        mMemberCount = memberCount;
        mPoolCount = poolCount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Bitmap getBuddyicon() {
        return mBuddyicon;
    }

    public void setBuddyicon(Bitmap buddyicon) {
        mBuddyicon = buddyicon;
    }

    public String getMemberCount() {
        return mMemberCount;
    }

    public void setMemberCount(String memberCount) {
        mMemberCount = memberCount;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public String getPoolCount() {
        return mPoolCount;
    }

    public void setPoolCount(String poolCount) {
        mPoolCount = poolCount;
    }

    /**
     * https://farm{icon-farm}.staticflickr.com/{icon-server}/buddyicons/{nsid}.jpg
     */
    public void setBuddyIconUrl(String iconfarm, String iconserver, String nsid){
        mBuddyiconUrl = "https://farm" + iconfarm + ".staticflickr.com/" + iconserver + "/buddyicons/"
                + nsid + ".jpg";
        Log.d("People icon", "BudddyIcon url: " + mBuddyiconUrl);
    }

    public String getBuddyiconUrl(){
        return mBuddyiconUrl;
    }


}
