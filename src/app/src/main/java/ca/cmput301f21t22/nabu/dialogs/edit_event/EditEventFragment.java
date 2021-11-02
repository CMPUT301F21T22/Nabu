package ca.cmput301f21t22.nabu.dialogs.edit_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ca.cmput301f21t22.nabu.databinding.FragmentEditEventBinding;
import ca.cmput301f21t22.nabu.model.Event;
import ca.cmput301f21t22.nabu.model.Habit;

public class EditEventFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_EVENT = Event;
    private static Object event;

    @Nullable
    private EditEventViewModel viewModel;
    @Nullable
    private FragmentEditEventBinding binding;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param event Parameter 1.
     * @return A new instance of fragment EditHabitFragment.
     */
    // TODO: Rename and change types and number of parameters
    @NonNull
    public static EditEventFragment newInstance(String event) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT, event);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditEventFragment newInstance(Event event) {
        EditEventFragment fragment = new EditEventFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT, Event);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            this.event = this.getArguments().getString(ARG_EVENT);

        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(EditEventViewModel.class);
        this.binding = FragmentEditEventBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        this.viewModel = null;
        this.binding = null;
        super.onDestroyView();
    }
}
