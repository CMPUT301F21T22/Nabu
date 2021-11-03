package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class HabitCard {
    private boolean expanded;
    @NonNull
    private final Habit habit;
    @NonNull
    private final List<Event> events;

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
        return this.habit.equals(habitCard.habit) && this.events.equals(habitCard.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.habit, this.events);
    }

    @NonNull
    public Habit getHabit() {
        return this.habit;
    }

    @NonNull
    public List<Event> getEvents() {
        return this.events;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
