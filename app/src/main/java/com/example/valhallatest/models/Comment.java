package com.example.valhallatest.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Comment extends RealmObject {
    @PrimaryKey
    @SerializedName("id")
    private long mId;
    @Required
    @SerializedName("name")
    private String mName;
    @Required
    @SerializedName("body")
    private String mBody;
    private Post mPost;

    @Ignore
    @SerializedName("postId")
    private long mPostId;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }

    public Post getPost() {
        return mPost;
    }

    public void setPost(Post post) {
        this.mPost = post;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public long getPostId() {
        return mPostId;
    }

    public void setPostId(long postId) {
        mPostId = postId;
    }
}
