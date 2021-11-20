package ca.cmput301f21t22.nabu.ui.habits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.databinding.CardHabitBinding;
import ca.cmput301f21t22.nabu.ui.ItemClickListener;
import ca.cmput301f21t22.nabu.ui.ItemDraggedListener;
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
    @NonNull
    private final HabitTouchHelperCallback habitTouchHelperCallback;
    @NonNull
    private final ItemTouchHelper itemTouchHelper;

    public HabitCardAdapter() {
        this.cards = new ArrayList<>();
        this.habitTouchHelperCallback = new HabitTouchHelperCallback();
        this.itemTouchHelper = new ItemTouchHelper(this.habitTouchHelperCallback);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.itemTouchHelper.attachToRecyclerView(recyclerView);
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

        holder.onBindView(card, position);
        holder.binding.card.setOnClickListener((view) -> {
            if (this.habitCardClickListener != null) {
                this.habitCardClickListener.onItemClicked(this, card, position);
            }
        });
        holder.binding.buttonOverflowMenu.setOnClickListener((view) -> {
            if (this.habitCardMenuClickListener != null) {
                this.habitCardMenuClickListener.onItemMenuClicked(view, card, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    public void setCards(@Nullable List<HabitCard> cards) {
        List<HabitCard> oldList = this.cards;
        List<HabitCard> newList = cards != null ? new ArrayList<>(cards) : new ArrayList<>();

        this.cards = newList;

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(oldList, newList));
        diffResult.dispatchUpdatesTo(this);
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

    public void setHabitCardDraggedListener(@Nullable ItemDraggedListener habitCardDraggedListener) {
        this.habitTouchHelperCallback.listener = habitCardDraggedListener;
    }

    public static class DiffUtilCallback extends DiffUtil.Callback {
        @NonNull
        private final List<HabitCard> oldCards;
        @NonNull
        private final List<HabitCard> newCards;

        public DiffUtilCallback(@NonNull List<HabitCard> oldCards, @NonNull List<HabitCard> newCards) {
            this.oldCards = oldCards;
            this.newCards = newCards;
        }

        @Override
        public int getOldListSize() {
            return this.oldCards.size();
        }

        @Override
        public int getNewListSize() {
            return this.newCards.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return Objects.equals(
                    this.oldCards.get(oldItemPosition).getHabit().getId(),
                    this.newCards.get(newItemPosition).getHabit().getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return Objects.equals(this.oldCards.get(oldItemPosition), this.newCards.get(newItemPosition));
        }
    }

    public static class HabitTouchHelperCallback extends ItemTouchHelper.Callback {
        @Nullable
        private ItemDraggedListener listener;

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            if (!(viewHolder instanceof ViewHolder) || !(target instanceof ViewHolder)) {
                return false;
            }

            Integer originPosition = ((ViewHolder) viewHolder).lastPosition;
            Integer targetPosition = ((ViewHolder) target).lastPosition;

            if (originPosition == null || targetPosition == null) {
                return false;
            } else if (this.listener != null) {
                return this.listener.onItemDragged(originPosition, targetPosition);
            }

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private final DateFormat dateFormat;
        @NonNull
        private final CardHabitBinding binding;
        @NonNull
        private final EventCardAdapter adapter;
        @Nullable
        private Integer lastPosition;

        public ViewHolder(@NonNull CardHabitBinding binding, @NonNull EventCardAdapter adapter) {
            super(binding.getRoot());
            this.dateFormat = DateFormat.getDateInstance();
            this.binding = binding;
            this.adapter = adapter;
        }

        public void onBindView(@NonNull HabitCard card, int position) {
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
            this.lastPosition = position;
        }
    }
}
