package ca.cmput301f21t22.nabu.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentSocialBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class SocialFragment extends ExtendedToolbarFragment {

    @Nullable
    private SocialViewModel viewModel;
    @Nullable
    private FragmentSocialBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(SocialViewModel.class);
        this.binding = FragmentSocialBinding.inflate(inflater, container, false);

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