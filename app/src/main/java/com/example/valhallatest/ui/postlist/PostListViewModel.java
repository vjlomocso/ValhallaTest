package com.example.valhallatest.ui.postlist;

import androidx.lifecycle.ViewModel;

import com.example.valhallatest.models.Post;
import com.example.valhallatest.ui.main.MainViewModel;

import java.util.Objects;

import io.realm.RealmResults;
import io.realm.Sort;

public class PostListViewModel extends ViewModel {
    private MainViewModel mMainViewModel;

    private RealmResults<Post> mPosts;

    public void setMainViewModel(MainViewModel mainViewModel) {
        mMainViewModel = mainViewModel;
    }

    public void initPosts() {
        Objects.requireNonNull(mMainViewModel, "Set MainViewModel first before calling this method.");
        mPosts = mMainViewModel.getRealm().where(Post.class)
                .findAll();
    }

    public RealmResults<Post> getPosts() {
        return mPosts;
    }
}
