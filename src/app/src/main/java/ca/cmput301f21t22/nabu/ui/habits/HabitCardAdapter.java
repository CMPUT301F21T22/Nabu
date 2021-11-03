package ca.cmput301f21t22.nabu.ui.habits;

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
import java.util.List;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.databinding.CardHabitBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;
import ca.cmput301f21t22.nabu.ui.ItemMenuClickListener;

public class HabitCardAdapter extends RecyclerView.Adapter<HabitCardAdapter.ViewHolder> {
    @NonNull
    private List<HabitCard> cards;
    @Nullable
    private ItemClickListener<HabitCardAdapter, HabitCard> habitCardClickListener;
    @Nullable
    private ItemMenuClickListener<HabitCard> habitCardMenuClickListener;
    @Nullable
    private ItemClickListener<EventCardAdapter, Event> eventCardClickListener;
    @Nullable
    private ItemMenuClickListener<Event> eventCardMenuClickListener;

    public HabitCardAdapter() {
        this.cards = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardHabitBinding binding = CardHabitBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        binding.listEvents.setLayoutManager(new LinearLayoutManager(parent.getContext()));
        return new ViewHolder(binding, new EventCardAdapter());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HabitCard card = this.cards.get(position);

        holder.adapter.setEvents(card.getEvents());
        holder.adapter.setClickListener(this.eventCardClickListener);
        holder.adapter.setMenuClickListener(this.eventCardMenuClickListener);

        holder.onBindView(card);
        holder.binding.card.setOnClickListener((view) -> {
            if (this.habitCardClickListener != null) {
                this.habitCardClickListener.onItemClicked(this, card);
            }
        });
        holder.binding.buttonOverflowMenu.setOnClickListener((view) -> {
            if (this.habitCardMenuClickListener != null) {
                this.habitCardMenuClickListener.onItemMenuClicked(view, card);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCards(@Nullable List<HabitCard> cards) {
        cards = cards != null ? cards : new ArrayList<>();
        this.cards = cards;
        this.notifyDataSetChanged();
    }

    public void setHabitCardClickListener(@Nullable ItemClickListener<HabitCardAdapter, HabitCard> habitCardClickListener) {
        this.habitCardClickListener = habitCardClickListener;
    }

    public void setHabitCardMenuClickListener(@Nullable ItemMenuClickListener<HabitCard> habitCardMenuClickListener) {
        this.habitCardMenuClickListener = habitCardMenuClickListener;
    }

    public void setEventCardClickListener(@Nullable ItemClickListener<EventCardAdapter, Event> eventCardClickListener) {
        this.eventCardClickListener = eventCardClickListener;
    }

    public void setEventCardMenuClickListener(@Nullable ItemMenuClickListener<Event> eventCardMenuClickListener) {
        this.eventCardMenuClickListener = eventCardMenuClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final DateFormat dateFormat;
        @NonNull
        private final CardHabitBinding binding;
        @NonNull
        private final EventCardAdapter adapter;

        public ViewHolder(@NonNull CardHabitBinding binding, @NonNull EventCardAdapter adapter) {
            super(binding.getRoot());
            this.dateFormat = DateFormat.getDateInstance();
            this.binding = binding;
            this.adapter = adapter;
        }

        public void onBindView(@NonNull HabitCard card) {
            Habit habit = card.getHabit();
            this.binding.labelHabitTitle.setText(habit.getTitle());
            this.binding.labelOccurrence.setText(habit.getOccurrence().toString());
            this.binding.labelReason.setText(habit.getReason());
            this.binding.labelStartDate.setText(this.dateFormat.format(habit.getStartDate()));

            if (card.isExpanded() && card.getEvents().size() > 0) {
                this.binding.listEvents.setAdapter(this.adapter);
                this.binding.layoutEvents.setVisibility(View.VISIBLE);
            } else {
                this.binding.layoutEvents.setVisibility(View.GONE);
            }
        }
    }
}
