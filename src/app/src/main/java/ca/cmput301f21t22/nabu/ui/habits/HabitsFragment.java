package ca.cmput301f21t22.nabu.ui.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class HabitsFragment extends ExtendedToolbarFragment {

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentHabitsBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @NonNull
    @Override
    public View getToolbarView() {
        HeaderDefaultBinding view = HeaderDefaultBinding.inflate(LayoutInflater.from(this.getContext()));
        view.title.setText(R.string.fragment_habits_name);
        return view.getRoot();
    }
}