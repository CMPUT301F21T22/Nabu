package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * A data object, holding the data needed to render a card in the Habits view.
 */
public class HabitCard {
    private boolean expanded;
    @NonNull
    private final Habit habit;
    @NonNull
    private final List<Event> events;

    /**
     * Create an instance of HabitCard.
     *
     * @param habit  The habit associated with the card view.
     * @param events The events associated with the given habit.
     */
    public HabitCard(@NonNull Habit habit, @NonNull List<Event> events) {
        this.habit = habit;
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        HabitCard habitCard = (HabitCard) o;
        return this.expanded == habitCard.expanded && this.habit.equals(habitCard.habit) &&
               this.events.equals(habitCard.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.expanded, this.habit, this.events);
    }

    /**
     * @return The habit associated with the card view.
     */
    @NonNull
    public Habit getHabit() {
        return this.habit;
    }

    /**
     * @return The events associated with the given habit.
     */
    @NonNull
    public List<Event> getEvents() {
        return this.events;
    }

    /**
     * @return Whether or not the card is "expanded", showing all events.
     */
    public boolean isExpanded() {
        return this.expanded;
    }

    /**
     * @param expanded Whether or not the card should be "expanded", showing all events.
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
