package com.example.valhallatest.ui.postdetails;

import androidx.lifecycle.ViewModel;

import com.example.valhallatest.models.Comment;
import com.example.valhallatest.models.Post;
import com.example.valhallatest.ui.main.MainViewModel;

import org.bson.types.ObjectId;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class PostDetailsViewModel extends ViewModel {
    private MainViewModel mMainViewModel;
    private Post mPost;

    public void setMainViewModel(MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
    }

    public void initPostDetails(long postId) {
        Objects.requireNonNull(mMainViewModel, "Set MainViewModel first before calling this method.");
        mPost = mMainViewModel.getRealm().where(Post.class)
                .equalTo("mId", postId)
                .findFirst();
    }

    public Post getPost() {
        return mPost;
    }
}
