package ca.cmput301f21t22.nabu.ui.habits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.databinding.FragmentHabitsBinding;
import ca.cmput301f21t22.nabu.databinding.HeaderDefaultBinding;
import ca.cmput301f21t22.nabu.dialogs.edit_event.EditEventFragment;
import ca.cmput301f21t22.nabu.dialogs.edit_habit.EditHabitFragment;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;
import ca.cmput301f21t22.nabu.ui.ExtendedToolbarFragment;

public class HabitsFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "HabitsFragment";

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;
    @Nullable
    private HabitCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentHabitsBinding.inflate(inflater, container, false);
        this.adapter = new HabitCardAdapter();

        UserRepository.getInstance()
                .getCurrentUser()
                .observe(this.getViewLifecycleOwner(), user -> this.viewModel.setCurrentUser(user));
        HabitRepository.getInstance()
                .getHabits()
                .observe(this.getViewLifecycleOwner(), habits -> this.viewModel.setCurrentHabits(habits));
        EventRepository.getInstance()
                .getEvents()
                .observe(this.getViewLifecycleOwner(), events -> this.viewModel.setCurrentEvents(events));

        this.adapter.setHabitCardClickListener((adapter, card) -> this.viewModel.onCardClicked(card));
        this.adapter.setHabitCardMenuClickListener((view, card) -> {
            PopupMenu menu = new PopupMenu(this.getContext(), view);
            if (!card.isExpanded()) {
                menu.getMenuInflater().inflate(R.menu.overflow_menu_habit_card_unexpanded, menu.getMenu());
            } else {
                menu.getMenuInflater().inflate(R.menu.overflow_menu_habit_card_expanded, menu.getMenu());
            }
            menu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == menu.getMenu().getItem(0).getItemId()) {
                    this.viewModel.onCardClicked(card);
                    return true;
                } else if (menuItem.getItemId() == menu.getMenu().getItem(1).getItemId()) {
                    EditHabitFragment.newInstance(card.getHabit()).show(this.getChildFragmentManager(), "EditHabit");
                    return true;
                } else if (menuItem.getItemId() == menu.getMenu().getItem(2).getItemId()) {
                    new MaterialAlertDialogBuilder(this.requireContext()).setMessage(
                            R.string.dialog_delete_habit_message)
                            .setNegativeButton(R.string.button_cancel, (dialogInterface, i) -> {
                            })
                            .setPositiveButton(R.string.button_delete,
                                               (dialogInterface, i) -> this.viewModel.deleteHabit(card.getHabit()))
                            .show();
                    return true;
                } else {
                    return false;
                }
            });
            menu.show();
        });

        this.adapter.setEventCardClickListener((adapter, event) -> EditEventFragment.newInstance(event)
                .show(this.getChildFragmentManager(), "EditEvent"));
        this.adapter.setEventCardMenuClickListener((view, event) -> {
            PopupMenu menu = new PopupMenu(this.getContext(), view);
            menu.getMenuInflater().inflate(R.menu.overflow_menu_event_card, menu.getMenu());
            menu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == menu.getMenu().getItem(0).getItemId()) {
                    EditEventFragment.newInstance(event).show(this.getChildFragmentManager(), "EditEvent");
                    return true;
                } else if (menuItem.getItemId() == menu.getMenu().getItem(1).getItemId()) {
                    new MaterialAlertDialogBuilder(this.requireContext()).setMessage(
                            R.string.dialog_delete_event_message)
                            .setNegativeButton(R.string.button_cancel, (dialogInterface, i) -> {
                            })
                            .setPositiveButton(R.string.button_delete,
                                               (dialogInterface, i) -> this.viewModel.deleteEvent(event))
                            .show();
                    return true;
                } else {
                    return false;
                }
            });
            menu.show();
        });

        this.viewModel.getCards().observe(this.getViewLifecycleOwner(), cards -> this.adapter.setCards(cards));

        this.binding.listHabits.setLayoutManager(new LinearLayoutManager(this.requireContext()));
        this.binding.listHabits.setAdapter(this.adapter);

        this.binding.buttonAddHabit.setOnClickListener(view -> {
            User currentUser = this.viewModel.getCurrentUser();
            if (currentUser != null) {
                EditHabitFragment.newInstance(currentUser).show(this.getChildFragmentManager(), "AddHabit");
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