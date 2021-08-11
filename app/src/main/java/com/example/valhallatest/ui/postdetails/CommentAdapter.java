package com.example.valhallatest.ui.postdetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.valhallatest.R;
import com.example.valhallatest.models.Comment;

import java.util.Objects;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CommentAdapter extends RealmRecyclerViewAdapter<Comment, CommentAdapter.CommentViewHolder> {

    public CommentAdapter(@Nullable OrderedRealmCollection<Comment> comments, boolean autoUpdate) {
        super(comments, autoUpdate);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ConstraintLayout mLayout;
        private TextView mBody;
        private TextView mOwner;

        public CommentViewHolder(ConstraintLayout v) {
            super(v);
            mLayout = v;
            mOwner = v.findViewById(R.id.owner);
            mBody = v.findViewById(R.id.body);
        }

        public void setBody(String body) {
            mBody.setText(body);
        }

        public void setOwner(String owner) {
            mOwner.setText(owner);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.CommentViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {
        Comment comment = Objects.requireNonNull(getItem(position));
        holder.setBody(comment.getBody());
        holder.setOwner(comment.getName());
    }
}
