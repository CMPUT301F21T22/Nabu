package ca.cmput301f21t22.nabu.ui.habits;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.HabitCard;

public class HabitCardDiffUtilCallback extends DiffUtil.Callback {
    @NonNull
    private final List<HabitCard> oldCards;
    @NonNull
    private final List<HabitCard> newCards;

    public HabitCardDiffUtilCallback(@NonNull List<HabitCard> oldCards, @NonNull List<HabitCard> newCards) {
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
