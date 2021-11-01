package ca.cmput301f21t22.nabu.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentSettingsBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class SettingsFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "SettingsFragment";

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

        this.viewModel.getCurrentUser().observe(this.getViewLifecycleOwner(), (user) -> {
            if (user != null) {
                this.binding.labelCurrentUserEmail.setText(user.getEmail());
            }
        });

        this.binding.cardLogout.setOnClickListener(
                view -> new MaterialAlertDialogBuilder(this.requireContext()).setMessage(
                        R.string.dialog_sign_out_message)
                        .setNegativeButton(R.string.button_cancel, (dialogInterface, i) -> {
                        })
                        .setPositiveButton(R.string.button_sign_out, (dialogInterface, i) -> this.viewModel.doSignOut())
                        .show());

        this.binding.cardReset.setOnClickListener(
                view -> new MaterialAlertDialogBuilder(this.requireContext()).setMessage(R.string.dialog_reset_message)
                        .setNegativeButton(R.string.button_cancel, (dialogInterface, i) -> {
                        })
                        .setPositiveButton(R.string.button_reset, (dialogInterface, i) -> this.viewModel.doReset())
                        .show());

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
        view.title.setText(R.string.fragment_settings_name);
        return view.getRoot();
    }
}