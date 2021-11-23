package ca.cmput301f21t22.nabu.ui.my_day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.databinding.FragmentMydayBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderCalendarBinding;
import ca.cmput301f21t22.nabu.dialogs.edit_event.EditEventFragment;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class MyDayFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "MyDayFragment";

    @Nullable
    private MyDayViewModel viewModel;
    @Nullable
    private FragmentMydayBinding binding;
    @Nullable
    private IncompleteCardAdapter incompleteAdapter;
    @Nullable
    private CompleteCardAdapter completeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(MyDayViewModel.class);
        this.binding = FragmentMydayBinding.inflate(inflater, container, false);
        this.incompleteAdapter = new IncompleteCardAdapter();
        this.completeAdapter = new CompleteCardAdapter();

        UserRepository.getInstance()
                .getCurrentUser()
                .observe(this.getViewLifecycleOwner(), user -> this.viewModel.setCurrentUser(user));
        HabitRepository.getInstance()
                .getHabits()
                .observe(this.getViewLifecycleOwner(), habits -> this.viewModel.setCurrentHabits(habits));
        EventRepository.getInstance()
                .getEvents()
                .observe(this.getViewLifecycleOwner(), events -> this.viewModel.setCurrentEvents(events));

        this.incompleteAdapter.setClickListener((adapter, item, position) -> this.viewModel.onCardClicked(item));
        this.completeAdapter.setClickListener((adapter, item, position) -> this.viewModel.onCardClicked(item));

        this.viewModel.getIncompleteCards()
                .observe(this.getViewLifecycleOwner(), cards -> this.incompleteAdapter.setCards(cards));
        this.viewModel.getCompleteCards()
                .observe(this.getViewLifecycleOwner(), cards -> this.completeAdapter.setCards(cards));
        this.viewModel.getInstantShowEdit().observe(this.getViewLifecycleOwner(), show -> {
            if (show != null && show) {
                Snackbar.make(this.requireActivity().findViewById(android.R.id.content),
                              R.string.prompt_habit_completed, BaseTransientBottomBar.LENGTH_LONG)
                        .setAnchorView(this.requireActivity().findViewById(R.id.main_nav_view))
                        .setAction(R.string.button_edit_event,
                                   view -> EditEventFragment.newInstance(this.viewModel.getMostRecentEvent())
                                           .show(this.getChildFragmentManager(), "EditEvent"))
                        .show();
            }
        });

        this.binding.listCard.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.listCard.setAdapter(new ConcatAdapter(this.incompleteAdapter, this.completeAdapter));

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View getToolbarView() {
        HeaderCalendarBinding toolbar = HeaderCalendarBinding.inflate(LayoutInflater.from(this.getContext()));

        // Set title.
        toolbar.title.setText(R.string.fragment_myday_name);

        // Set calendar.
        TextView[] daysOfWeek = {
                toolbar.labelDayOfWeek0,
                toolbar.labelDayOfWeek1,
                toolbar.labelDayOfWeek2,
                toolbar.labelDayOfWeek3,
                toolbar.labelDayOfWeek4,
                toolbar.labelDayOfWeek5,
                toolbar.labelDayOfWeek6,
                };

        TextView[] dates = {
                toolbar.labelDate0,
                toolbar.labelDate1,
                toolbar.labelDate2,
                toolbar.labelDate3,
                toolbar.labelDate4,
                toolbar.labelDate5,
                toolbar.labelDate6,
                };

        LocalDate day = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            daysOfWeek[i].setText(day.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            dates[i].setText(String.format(Locale.getDefault(), "%d", day.getDayOfMonth()));
            day = day.minusDays(1);
        }

        return toolbar.getRoot();
    }
}