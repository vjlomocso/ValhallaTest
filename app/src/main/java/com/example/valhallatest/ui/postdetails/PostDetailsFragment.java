package com.example.valhallatest.ui.postdetails;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingConversion;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.valhallatest.R;
import com.example.valhallatest.databinding.PostDetailsFragmentBinding;
import com.example.valhallatest.models.Comment;
import com.example.valhallatest.ui.main.MainViewModel;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

public class PostDetailsFragment extends Fragment {
    private PostDetailsViewModel mViewModel;
    private RecyclerView mComments;
    private CommentAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();

        setAllowEnterTransitionOverlap(true);
        setAllowReturnTransitionOverlap(true);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mComments = view.findViewById(R.id.comment_list);
        long postId = PostDetailsFragmentArgs.fromBundle(getArguments()).getPostId();
        initData(mainViewModel, postId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mComments.setLayoutManager(layoutManager);
        mComments.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainViewModel.close();
                requireActivity().supportFinishAfterTransition();
            }
        });

        view.findViewById(R.id.post).setTransitionName("post_" + postId);

        ChangeBounds enterTransition = new ChangeBounds();
        enterTransition.setDuration(750);
        setSharedElementEnterTransition(enterTransition);

        ChangeBounds returnTransition = new ChangeBounds();
        returnTransition.setDuration(750);
        setSharedElementReturnTransition(returnTransition);

        startPostponedEnterTransition();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PostDetailsViewModel.class);
        PostDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.post_details_fragment, container, false);
        binding.setViewModel(mViewModel);
        binding.setHandlers(this);
        return binding.getRoot();
    }

    @BindingConversion
    public static CharSequence convertDateToString(Date date) {
        return DateUtils.getRelativeTimeSpanString(
                date.getTime(),
                Calendar.getInstance().getTimeInMillis(),
                DateUtils.MINUTE_IN_MILLIS);
    }

    private void initData(MainViewModel mainViewModel, long postId) {
        mViewModel.setMainViewModel(mainViewModel);
        mViewModel.initPostDetails(postId);
        RealmResults<Comment> comments = mViewModel.getPost()
                .getComments();
        mAdapter = new CommentAdapter(comments, true);
        mComments.setAdapter(mAdapter);
    }
}
