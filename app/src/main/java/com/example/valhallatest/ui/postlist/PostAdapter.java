package com.example.valhallatest.ui.postlist;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.valhallatest.R;
import com.example.valhallatest.models.Post;
import com.example.valhallatest.models.User;

import java.util.Objects;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class PostAdapter extends RealmRecyclerViewAdapter<Post, PostAdapter.PostViewHolder> {

    private final OnPostClick mClickListener;

    public PostAdapter(@Nullable OrderedRealmCollection<Post> posts, boolean autoUpdate, OnPostClick clickListener) {
        super(posts, autoUpdate);
        mClickListener = clickListener;
    }

    public interface OnPostClick {
        void execute(long postId, View view);
    }

    public static class PostViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final OnPostClick mClickListener;
        private final CardView mView;
        private final TextView mTitle;
        private final TextView mBody;
        private final TextView mOwner;
        private long mPostId;

        public PostViewHolder(CardView v, OnPostClick clickListener) {
            super(v);
            mView = v;
            mClickListener = clickListener;
            v.findViewById(R.id.post_item_layout).setOnClickListener(this);
            mTitle = v.findViewById(R.id.title);
            mOwner = v.findViewById(R.id.owner);
            mBody = v.findViewById(R.id.body);
        }

        public void setTitle(String title) {
            mTitle.setText(title);
        }

        public void setBody(String body) {
            mBody.setText(body);
        }

        public void setOwner(CharSequence owner) {
            mOwner.setText(owner);
        }

        public void addBottomSpace() {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            Resources r = mView.getContext().getResources();
            int horizontalMargins = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    r.getDisplayMetrics()
            );
            int verticalMargins = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    15,
                    r.getDisplayMetrics()
            );
            params.setMargins(horizontalMargins, verticalMargins, horizontalMargins, verticalMargins);
            mView.setLayoutParams(params);
        }

        @Override
        public void onClick(View view) {
            mClickListener.execute(getPostId(), mView);
        }

        public long getPostId() {
            return mPostId;
        }

        public void setPostId(long postId) {
            mPostId = postId;
        }

        public void setTransitionName(String transitionName) {
            mView.setTransitionName(transitionName);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = Objects.requireNonNull(getItem(position));
        holder.setBody(post.getBody());
        User owner = post.getOwner();
        holder.setTitle(post.getTitle());
        if(owner != null) {
            holder.setOwner(owner.getName());
        }
        if(position == getItemCount() - 1) {
            holder.addBottomSpace();
        }
        holder.setPostId(post.getId());
        holder.setTransitionName("post_" + post.getId());
    }
}