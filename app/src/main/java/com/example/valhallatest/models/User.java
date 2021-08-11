package com.example.valhallatest.models;

import com.google.gson.annotations.SerializedName;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private long mId;
    @Required
    @SerializedName("name")
    private String mName;

    @LinkingObjects("mOwner")
    private final RealmResults<Post> mPosts = null;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public RealmResults<Post> getPosts() {
        return mPosts;
    }
}
