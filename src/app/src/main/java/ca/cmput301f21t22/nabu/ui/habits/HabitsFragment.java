package ca.cmput301f21t22.nabu.ui.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class HabitsFragment extends ExtendedToolbarFragment {

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;

    //Variables for the listView of habits
    private ListView habitsListView;
    private HabitListAdapter habitsAdapter;
    private ArrayList<Habit> habitDataList;

    //Temporary
    //private HabitList habitList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentHabitsBinding.inflate(inflater, container, false);

        this.habitDataList = new ArrayList<Habit>();
        this.habitDataList.add(new Habit("Work", "Making money", new Date(),
                                         new Occurrence(true, false, true, false, true, false, true), new ArrayList<>(),
                                         true));

        this.habitsListView = this.binding.habitsList;
        this.habitsAdapter = new HabitListAdapter(this.requireContext(), this.habitDataList);
        this.habitsListView.setAdapter(this.habitsAdapter);

        final FloatingActionButton addHabit = this.binding.addHabitButton;
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Call to edit/add habit fragment
                //habitDataList.add(inputHabit);
                HabitsFragment.this.habitsAdapter.notifyDataSetChanged();
            }
        });

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