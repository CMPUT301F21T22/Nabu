package ca.cmput301f21t22.nabu.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301f21t22.nabu.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    @Nullable
    private SettingsViewModel viewModel;
    @Nullable
    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        this.binding = FragmentSettingsBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.viewModel = null;
        this.binding = null;
        super.onDestroyView();
    }
}