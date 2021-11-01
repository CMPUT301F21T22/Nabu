package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.Objects;

/**
 * A data object storing the days of the week on which an event occurs.
 */
public class Occurrence {
    private boolean onSunday;
    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;

    /**
     * Create an instance of Occurrence.
     */
    public Occurrence() {
    }

    /**
     * Create an instance of Occurrence.
     *
     * @param onSunday    Whether or not the event occurs on Sunday.
     * @param onMonday    Whether or not the event occurs on Monday.
     * @param onTuesday   Whether or not the event occurs on Tuesday.
     * @param onWednesday Whether or not the event occurs on Wednesday.
     * @param onThursday  Whether or not the event occurs on Thursday.
     * @param onFriday    Whether or not the event occurs on Friday.
     * @param onSaturday  Whether or not the event occurs on Saturday.
     */
    public Occurrence(boolean onSunday,
                      boolean onMonday,
                      boolean onTuesday,
                      boolean onWednesday,
                      boolean onThursday,
                      boolean onFriday,
                      boolean onSaturday) {
        this.onSunday = onSunday;
        this.onMonday = onMonday;
        this.onTuesday = onTuesday;
        this.onWednesday = onWednesday;
        this.onThursday = onThursday;
        this.onFriday = onFriday;
        this.onSaturday = onSaturday;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Occurrence that = (Occurrence) o;
        return this.onSunday == that.onSunday && this.onMonday == that.onMonday && this.onTuesday == that.onTuesday &&
               this.onWednesday == that.onWednesday && this.onThursday == that.onThursday &&
               this.onFriday == that.onFriday && this.onSaturday == that.onSaturday;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.onSunday, this.onMonday, this.onTuesday, this.onWednesday, this.onThursday,
                            this.onFriday, this.onSaturday);
    }

    public boolean isOnSunday() {
        return this.onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    public boolean isOnMonday() {
        return this.onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return this.onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return this.onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return this.onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean isOnFriday() {
        return this.onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return this.onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    public boolean isOnDayOfWeek(@NonNull DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return this.onMonday;
            case TUESDAY:
                return this.onTuesday;
            case WEDNESDAY:
                return this.onWednesday;
            case THURSDAY:
                return this.onThursday;
            case FRIDAY:
                return this.onFriday;
            case SATURDAY:
                return this.onSaturday;
            case SUNDAY:
                return this.onSunday;
            default:
                return false;
        }
    }
}
