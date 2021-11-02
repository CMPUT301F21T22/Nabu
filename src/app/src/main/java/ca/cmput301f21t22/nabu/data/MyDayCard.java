package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

import ca.cmput301f21t22.nabu.R;

public class MyDayCard implements Serializable {
    @NonNull
    private final Habit habit;
    @NonNull
    private final Event[] events;

    public MyDayCard(@NonNull Habit habit, @NonNull Event[] events) {
        this.habit = habit;
        this.events = events;
    }

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

    @Override
    public int hashCode() {
        int result = Objects.hash(this.habit);
        result = 31 * result + Arrays.hashCode(this.events);
        return result;
    }

    @NonNull
    public Habit getHabit() {
        return this.habit;
    }

    @NonNull
    public Event[] getEvents() {
        return this.events;
    }

    @NonNull
    public Icon getIcon(int pos) {
        if (pos < 0 || pos >= 7) {
            throw new IndexOutOfBoundsException();
        }

        Occurrence occ = this.habit.getOccurrence();
        LocalDate day = LocalDate.now().minusDays(pos);
        Event event = this.events[pos];

        if (!occ.isOnDayOfWeek(day.getDayOfWeek())) {
            return Icon.NOT_DUE;
        } else if (event == null && pos == 0) {
            return Icon.INCOMPLETE;
        } else if (event != null) {
            return Icon.COMPLETE;
        } else {
            return Icon.FAILED;
        }
    }

    public enum Icon {
        INCOMPLETE(R.drawable.ic_outline_circle_18), COMPLETE(R.drawable.ic_baseline_check_circle_18), FAILED(
                R.drawable.ic_baseline_close_18), NOT_DUE(0);

        private final int res;

        Icon(int res) {
            this.res = res;
        }

        public int getResource() {
            return this.res;
        }
    }
}
