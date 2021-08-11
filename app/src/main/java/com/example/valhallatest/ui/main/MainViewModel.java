package com.example.valhallatest.ui.main;

import static java.util.Objects.requireNonNull;

import androidx.databinding.Bindable;
import androidx.annotation.NonNull;

import com.example.valhallatest.BR;
import com.example.valhallatest.models.Comment;
import com.example.valhallatest.models.Post;
import com.example.valhallatest.models.User;
import com.example.valhallatest.services.JsonPlaceholderService;
import com.example.valhallatest.ui.common.ObservableViewModel;
import com.example.valhallatest.ui.common.RealmTaskManager;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends ObservableViewModel {
    private RealmTaskManager t = new RealmTaskManager();
    private Realm mRealm;

    public void initRealmIfNeeded() {
        if(mRealm != null) {
            return;
        }

        String realmName = "ValhallaTestProject";
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .name(realmName)
                .build();

        mRealm = Realm.getInstance(config);
        initializeData();
    }

    private void initializeData() {
        try {
            if(mRealm.where(Post.class).count() == 0) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://jsonplaceholder.typicode.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                JsonPlaceholderService service = retrofit.create(JsonPlaceholderService.class);
                service.getUsers().enqueue(new SaveUsersCallback(service));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private class SaveUsersCallback implements Callback<List<User>> {

        private final JsonPlaceholderService mService;

        public SaveUsersCallback(JsonPlaceholderService service) {
            mService = service;
        }

        @Override
        public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
            System.out.println("Response: " + response.toString());
            List<User> users = Objects.requireNonNull(response.body());

            mRealm.executeTransaction(r -> r.copyToRealmOrUpdate(users));
            mService.getPosts().enqueue(new SavePostsCallback(mService));
        }

        @Override
        public void onFailure(Call<List<User>> call, Throwable t) {

        }
    }

    private class SavePostsCallback implements Callback<List<Post>> {

        private final JsonPlaceholderService mService;

        public SavePostsCallback(JsonPlaceholderService service) {
            mService = service;
        }

        @Override
        public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
            List<Post> posts = Objects.requireNonNull(response.body());

            mRealm.executeTransaction(r -> {
                User user = null;
                for (Post post : posts) {
                    if(user == null || user.getId() != post.getUserId()) {
                        user = mRealm
                                .where(User.class)
                                .equalTo("mId", post.getUserId())
                                .findFirst();
                    }

                    post.setOwner(user);
                    r.insertOrUpdate(post);
                }
            });

            mService.getComments().enqueue(new SaveCommentsCallback());
        }

        @Override
        public void onFailure(Call<List<Post>> call, Throwable t) {

        }
    }

    private class SaveCommentsCallback implements Callback<List<Comment>> {

        @Override
        public void onResponse(@NonNull Call<List<Comment>> call, @NonNull Response<List<Comment>> response) {
            List<Comment> comments = Objects.requireNonNull(response.body());

            mRealm.executeTransaction(r -> {
                Post post = null;
                for (Comment comment : comments) {
                    if(post == null || post.getId() != comment.getPostId()) {
                        post = mRealm
                                .where(Post.class)
                                .equalTo("mId", comment.getPostId())
                                .findFirst();
                    }

                    comment.setPost(post);
                    r.insertOrUpdate(comment);
                }
            });
        }

        @Override
        public void onFailure(Call<List<Comment>> call, Throwable t) {

        }
    }

    public void close() {
        if(mRealm != null) {
            mRealm.close();
        }

        t.cancelAllRunning();
    }

    public Realm getRealm() {
        return mRealm;
    }
}
