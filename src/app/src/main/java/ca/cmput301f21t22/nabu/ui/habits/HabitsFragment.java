package ca.cmput301f21t22.nabu.ui.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.model.EventList;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.HabitList;
import ca.cmput301f21t22.nabu.model.HabitListAdapter;
import ca.cmput301f21t22.nabu.model.Occurrence;

public class HabitsFragment extends Fragment {

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;

    //Variables for the listView of habits
    private ListView habitsListView;
    private HabitListAdapter habitsAdapter;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        habitDataList = new ArrayList<Habit>();
        habitDataList.add(new Habit("Work", "Making money", new Date(),
                new Occurrence(true, false, true, false,
                        true, false, true), new EventList()));

        habitsListView = this.binding.habitsList;
        habitsAdapter = new HabitListAdapter(this.requireContext(), habitDataList);
        habitsListView.setAdapter(habitsAdapter);

        final FloatingActionButton addHabit = this.binding.addHabitButton;
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Habit inputHabit = new Habit();
                //TODO: Call to edit/add habit fragment
                habitList.add(inputHabit);
                habitsAdapter.add(inputHabit);
                habitsAdapter.notifyDataSetChanged();
            }
        });

    }

}