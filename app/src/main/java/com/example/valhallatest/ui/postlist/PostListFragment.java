package com.example.valhallatest.ui.postlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.valhallatest.R;
import com.example.valhallatest.databinding.PostListFragmentBinding;
import com.example.valhallatest.models.Post;
import com.example.valhallatest.ui.main.MainViewModel;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmResults;

public class PostListFragment extends Fragment {
    private PostListViewModel mViewModel;
    private RecyclerView mPosts;
    private PostAdapter mAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        postponeEnterTransition();

        setAllowEnterTransitionOverlap(true);
        setAllowReturnTransitionOverlap(true);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mPosts = view.findViewById(R.id.post_list);
        initData(mainViewModel);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        mPosts.setLayoutManager(layoutManager);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mainViewModel.close();
                requireActivity().supportFinishAfterTransition();
            }
        });

        final ViewGroup parentView = (ViewGroup) view.getParent();

        parentView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw(){
                        parentView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(PostListViewModel.class);
        PostListFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.post_list_fragment, container, false);
        binding.setViewModel(mViewModel);
        binding.setHandlers(this);
        return binding.getRoot();
    }

    private void initData(MainViewModel mainViewModel) {
        mViewModel.setMainViewModel(mainViewModel);
        mViewModel.initPosts();
        RealmResults<Post> posts = mViewModel.getPosts();
        mAdapter = new PostAdapter(posts, true, this::viewPostDetails);
        mPosts.setAdapter(mAdapter);
    }

    private void viewPostDetails(long postId, View view) {
        FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
                .addSharedElement(view, view.getTransitionName())
                .build();

        NavController navController = NavHostFragment.findNavController(this);
        PostListFragmentDirections.ListToDetailsAction action =
                PostListFragmentDirections.listToDetailsAction(postId);
        navController.navigate(action, extras);
    }
}
