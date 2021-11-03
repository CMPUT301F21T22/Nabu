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

import ca.cmput301f21t22.nabu.databinding.FragmentEventsBinding;
import ca.cmput301f21t22.nabu.model.Event;
import ca.cmput301f21t22.nabu.model.EventList;
import ca.cmput301f21t22.nabu.model.EventListAdapter;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.HabitList;
import ca.cmput301f21t22.nabu.model.Occurrence;

public class EventsFragment extends Fragment {

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentEventsBinding binding;

    //Variables for the listView of habits
    private ListView eventsListView;
    private EventListAdapter eventsAdapter;
    private ArrayList<Event> eventDataList;

    //Temporary
    private HabitList habitList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentEventsBinding.inflate(inflater, container, false);

        eventDataList = new ArrayList<Event>();
        eventDataList.add(new Event(new Date()));

        eventsListView = this.binding.eventsList;
        eventsAdapter = new EventListAdapter(this.requireContext(), eventDataList);
        eventsListView.setAdapter(eventsAdapter);

        final FloatingActionButton addHabit = this.binding.addEventButton;
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event inputEvent = new Event(new Date());
                //TODO: Call to edit/add habit fragment
                //habitDataList.add(inputHabit);
                eventsAdapter.add(inputEvent);
                eventsAdapter.notifyDataSetChanged();
            }
        });

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.viewModel = null;
        this.binding = null;
        super.onDestroyView();
    }

}
