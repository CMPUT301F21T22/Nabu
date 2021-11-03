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

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.databinding.FragmentEventsBinding;

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
    //private HabitList habitList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentEventsBinding.inflate(inflater, container, false);

        this.eventDataList = new ArrayList<Event>();
        this.eventDataList.add(new Event(new Date()));

        this.eventsListView = this.binding.eventsList;
        this.eventsAdapter = new EventListAdapter(this.requireContext(), this.eventDataList);
        this.eventsListView.setAdapter(this.eventsAdapter);

        final FloatingActionButton addHabit = this.binding.addEventButton;
        addHabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event inputEvent = new Event(new Date());
                //TODO: Call to edit/add habit fragment
                //habitDataList.add(inputHabit);
                EventsFragment.this.eventsAdapter.add(inputEvent);
                EventsFragment.this.eventsAdapter.notifyDataSetChanged();
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
