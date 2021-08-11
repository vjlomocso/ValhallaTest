package com.example.valhallatest.services;

import com.example.valhallatest.models.Comment;
import com.example.valhallatest.models.Post;
import com.example.valhallatest.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceholderService {
    @GET("comments")
    Call<List<Comment>> getComments();

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("users")
    Call<List<User>> getUsers();
}
