package ca.cmput301f21t22.nabu.ui.my_day;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.databinding.CardHabitBinding;
import ca.cmput301f21t22.nabu.databinding.CardSocialHabitBinding;
import ca.cmput301f21t22.nabu.databinding.SocialFeedBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;
import ca.cmput301f21t22.nabu.ui.ItemMenuClickListener;
import ca.cmput301f21t22.nabu.ui.habits.EventCardAdapter;

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.ViewHolder> {
    @NonNull
    private List<User> users;

    public SocialFeedAdapter() {
        this.users = new ArrayList<>();
    }

    @NonNull
    @Override
    public SocialFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SocialFeedBinding binding = SocialFeedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.feedView.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        return new SocialFeedAdapter.ViewHolder(binding, new SocialHabitCardAdapter());
    }

    @Override
    public void onBindViewHolder(@NonNull SocialFeedAdapter.ViewHolder holder, int position) {
        User user = this.users.get(position);
        List<Habit> habits = habits;
        holder.adapter.setCards();
        holder.onBindView(user);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<User> users) {
        users = users != null ? users : new ArrayList<>();
        this.users = users;
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final SocialFeedBinding binding;
        @NonNull
        private final SocialHabitCardAdapter adapter;

        public ViewHolder(@NonNull SocialFeedBinding binding, @NonNull SocialHabitCardAdapter adapter) {
            super(binding.getRoot());
            this.binding = binding;
            this.adapter = adapter;
        }

        public void onBindView(@NonNull User user) {
            this.binding.feedNameText.setText(user.getEmail());
        }
    }
}

/*
public class HabitsFragment extends ExtendedToolbarFragment {
    @NonNull
    public final static String TAG = "HabitsFragment";

    @Nullable
    private HabitsViewModel viewModel;
    @Nullable
    private FragmentHabitsBinding binding;
    @Nullable
    private ca.cmput301f21t22.nabu.ui.habits.HabitCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.viewModel = new ViewModelProvider(this).get(HabitsViewModel.class);
        this.binding = FragmentHabitsBinding.inflate(inflater, container, false);
        this.adapter = new ca.cmput301f21t22.nabu.ui.habits.HabitCardAdapter();

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
*/