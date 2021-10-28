package ca.cmput301f21t22.nabu.ui.habits;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.FragmentSettingsBinding;
import ca.cmput301f21t22.nabu.dialogs.edit_habit.EditHabitFragment;
import ca.cmput301f21t22.nabu.model.EventList;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.HabitList;
import ca.cmput301f21t22.nabu.model.HabitListAdapter;
import ca.cmput301f21t22.nabu.model.HabitListView;
import ca.cmput301f21t22.nabu.model.Occurrence;
import ca.cmput301f21t22.nabu.ui.settings.SettingsViewModel;

public class HabitsFragment extends Fragment {

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;

    //Variables for the listView of habits
    private ListView habitsListView;
    private HabitListAdapter habitsAdapter;
    private HabitListView habitListView;
    private ArrayList<Habit> habitDataList;

    //Temporary
    private HabitList habitList;



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

        habitDataList.add(new Habit("Work", "Making money", new Date(),
                new Occurrence(true, false, true, false,
                        true, false, true), new EventList()));

        habitsListView = this.binding.habitsList;
        habitsAdapter = new HabitListAdapter(this.requireContext(), habitDataList);
        habitsListView.setAdapter(habitsAdapter);
        habitListView = new HabitListView(habitList, habitsAdapter);

        final FloatingActionButton addHabit = this.binding.addHabitButton;
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add a way to create a edit_habit fragment
                habitsAdapter.notifyDataSetChanged();

            }
        });

    }

}