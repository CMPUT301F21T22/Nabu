package ca.cmput301f21t22.nabu.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentSocialBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class SocialFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "SocialFragment";
    @Nullable
    private SocialViewModel viewModel;
    @Nullable
    private FragmentSocialBinding binding;
    @Nullable
    private RequestFollowCardAdapter requestAdapter;
    @Nullable
    private FollowingCardAdapter followingAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(SocialViewModel.class);
        this.binding = FragmentSocialBinding.inflate(inflater, container, false);
        this.requestAdapter = new RequestFollowCardAdapter();
        this.followingAdapter = new FollowingCardAdapter();

        UserRepository.getInstance()
                .getCurrentUser()
                .observe(this.getViewLifecycleOwner(), user -> this.viewModel.setCurrentUser(user));

        this.requestAdapter.setAcceptButtonListener((adapter, item) -> this.viewModel.onAcceptClicked(item));
        this.requestAdapter.setDenyButtonListener((adapter, item) -> this.viewModel.onDenyClicked(item));

        this.followingAdapter.setUnfollowButtonListener((adapter, item) -> this.viewModel.onUnfollowClicked(item));

        this.viewModel.getRequestCards()
                .observe(this.getViewLifecycleOwner(), cards -> this.requestAdapter.setCards(cards));
        this.viewModel.getFollowingCards()
                .observe(this.getViewLifecycleOwner(), cards -> this.followingAdapter.setCards(cards));

        this.binding.pendingRequestList.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.pendingRequestList.setAdapter(this.requestAdapter);

        this.binding.followerList.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.followerList.setAdapter(this.followingAdapter);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @NonNull
    @Override
    public View getToolbarView() {
        HeaderDefaultBinding header = HeaderDefaultBinding.inflate(LayoutInflater.from(this.getContext()));
        header.title.setText(R.string.fragment_social_name);
        return header.getRoot();
    }
}