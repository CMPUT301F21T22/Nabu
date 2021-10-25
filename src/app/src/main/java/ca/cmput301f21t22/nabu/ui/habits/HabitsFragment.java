package ca.cmput301f21t22.nabu.ui.habits;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.FragmentSettingsBinding;
import ca.cmput301f21t22.nabu.dialogs.edit_habit.EditHabitFragment;
import ca.cmput301f21t22.nabu.ui.settings.SettingsViewModel;

public class HabitsFragment extends Fragment {

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
        this.viewModel = null;
        this.binding = null;
        super.onDestroyView();
    }

    // TODO: Fix R and findViewById errors
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FloatingActionButton addHabit = findViewById(R.id.add_habit_Button);
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add a way to create a edit_habit fragment
            }
        });

    }

    /**
     * Shows a popup menu when the button on a habit card is clicked
     * @param view
     *  View of the button pressed
     */
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
    }
}