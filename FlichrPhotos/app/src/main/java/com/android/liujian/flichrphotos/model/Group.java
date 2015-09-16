package com.android.liujian.flichrphotos.model;

/**
 * Created by liujian on 15/9/11.
 * A model - Group
 */
public class Group {
    private String mId;
    private String mName;
    private String mMemberCount;
    private String mTopicCount;
    private String mPoolCount;

    public Group(){
        //default constructor
    }

    public Group(String id, String name, String memberCount, String topicCount, String poolCount) {
        mId = id;
        mName = name;
        mMemberCount = memberCount;
        mTopicCount = topicCount;
        mPoolCount = poolCount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMember() {
        return mMemberCount;
    }

    public void setMember(String member) {
        mMemberCount = member;
    }

    public String getTopicCount() {
        return mTopicCount;
    }

    public void setTopicCount(String topicCount) {
        mTopicCount = topicCount;
    }

    public String getPoolCount() {
        return mPoolCount;
    }

    public void setPoolCount(String poolCount) {
        mPoolCount = poolCount;
    }
}
