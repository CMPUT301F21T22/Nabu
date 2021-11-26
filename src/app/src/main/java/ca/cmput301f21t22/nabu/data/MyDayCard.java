package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Objects;

import ca.cmput301f21t22.nabu.R;

/**
 * A data object, holding the data needed to render a card in the My Day view.
 */
public class MyDayCard implements Serializable {
    @NonNull
    private final Habit habit;
    @NonNull
    private final Event[] events;

    /**
     * Create an instance of MyDayCard.
     *
     * @param habit  The habit associated with the card view.
     * @param events The events that may or may not have occurred within the last seven days. See
     *               {@link MyDayCard#getEvents()}.
     */
    public MyDayCard(@NonNull Habit habit, @NonNull Event[] events) {
        this.habit = habit;
        this.events = events;
    }

    /**
     * @return Whether two objects are structurally equivalent.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        MyDayCard myDayCard = (MyDayCard) o;
        return this.habit.equals(myDayCard.habit) && Arrays.equals(this.events, myDayCard.events);
    }

    /**
     * @return The hash code of the object's fields.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(this.habit);
        result = 31 * result + Arrays.hashCode(this.events);
        return result;
    }

    /**
     * @return The habit associated with the card view.
     */
    @NonNull
    public Habit getHabit() {
        return this.habit;
    }

    /**
     * An array whose each element correspond to one day in the last week, with 0 being today, and 6 being a week ago.
     * If an event associated with the habit occurred on one of these days, the array at the corresponding position will
     * contain that event. Otherwise, it will be null.
     * <p>
     * For instance, if the habit has events that occurred today, and two days ago, the array would look like this:
     * [Event(...), null, Event(...), null, null, null, null].
     *
     * @return The events that may or may not have occurred within the last seven days.
     */
    @NonNull
    public Event[] getEvents() {
        return this.events;
    }

    /**
     * Get the view icon resource associated with an event in the array. If the event isn't due, the icon returned will
     * be the icon for an event that isn't due. Otherwise, it will check whether or not the array at the given position
     * is null. If it is null, then the event was either failed if the date is in the past, or incomplete if the date is
     * today. Otherwise, it is complete.
     *
     * @param pos The number of days backwards since today, with today being position 0, and 6 being a week ago.
     * @return The view icon resource for the event.
     */
    @NonNull
    public Icon getIcon(int pos) {
        if (pos < 0 || pos >= 7) {
            throw new IndexOutOfBoundsException();
        }

        Occurrence occ = this.habit.getOccurrence();
        LocalDate day = LocalDate.now().minusDays(pos);
        LocalDate startDate = this.habit.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Event event = this.events[pos];

        if (!occ.isOnDayOfWeek(day.getDayOfWeek()) || startDate.isAfter(day)) {
            return Icon.NOT_DUE;
        } else if (event == null && pos == 0) {
            return Icon.INCOMPLETE;
        } else if (event != null) {
            return Icon.COMPLETE;
        } else {
            return Icon.FAILED;
        }
    }

    /**
     * A type-safe way of interacting with the relevant view icons for habit completion.
     */
    public enum Icon {
        INCOMPLETE(R.drawable.ic_outline_circle_18), COMPLETE(R.drawable.ic_baseline_check_circle_18), FAILED(
                R.drawable.ic_baseline_close_18), NOT_DUE(0);

        private final int res;

        Icon(int res) {
            this.res = res;
        }

        /**
         * @return The integer resource for the drawable associated with the view icon.
         */
        public int getResource() {
            return this.res;
        }
    }
}
