package com.example.valhallatest.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Post extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private long mId;
    @Required
    @SerializedName("title")
    private String mTitle;
    @Required
    @SerializedName("body")
    private String mBody;
    private User mOwner;

    @Ignore
    @SerializedName("userId")
    private long mUserId;

    @LinkingObjects("mPost")
    private final RealmResults<Comment> mComments = null;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String mMessage) {
        this.mBody = mMessage;
    }

    public User getOwner() {
        return mOwner;
    }

    public void setOwner(User owner) {
        this.mOwner = owner;
    }

    public RealmResults<Comment> getComments() {
        return mComments;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getUserId() {
        return mUserId;
    }

    public void setUserId(long userId) {
        mUserId = userId;
    }
}
