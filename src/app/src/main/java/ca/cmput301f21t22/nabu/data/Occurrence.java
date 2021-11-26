package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;

/**
 * A data object, storing the days of the week on which an event occurs.
 */
public class Occurrence implements Serializable {
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
        this.onSunday = false;
        this.onMonday = false;
        this.onTuesday = false;
        this.onWednesday = false;
        this.onThursday = false;
        this.onFriday = false;
        this.onSaturday = false;
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
        Occurrence that = (Occurrence) o;
        return this.onSunday == that.onSunday && this.onMonday == that.onMonday && this.onTuesday == that.onTuesday &&
               this.onWednesday == that.onWednesday && this.onThursday == that.onThursday &&
               this.onFriday == that.onFriday && this.onSaturday == that.onSaturday;
    }

    /**
     * @return The hash code of the object's fields.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.onSunday, this.onMonday, this.onTuesday, this.onWednesday, this.onThursday,
                            this.onFriday, this.onSaturday);
    }

    /**
     * Creates a string representation of the occurrence object in short,
     * North-American style order. For example, if the event occurs on
     * Monday and Saturday, it would return "Mon, Sat".
     *
     * @return String representation of the occurrence object
     * constructed using the day values
     */
    @NonNull
    @Override
    public String toString() {
        if (this.isOnSunday() && this.isOnMonday() && this.isOnTuesday() && this.isOnWednesday() &&
            this.isOnThursday() && this.isOnFriday() && this.isOnSaturday()) {
            return "Every Day";
        }

        StringBuilder representation = new StringBuilder();
        boolean first = true;
        if (this.isOnSunday()) {
            first = false;

            representation.append("Sun");
        }
        if (this.isOnMonday()) {
            if (!first) {
                representation.append(", ");
            } else {
                first = false;
            }

            representation.append("Mon");
        }
        if (this.isOnTuesday()) {
            if (!first) {
                representation.append(", ");
            } else {
                first = false;
            }

            representation.append("Tues");
        }
        if (this.isOnWednesday()) {
            if (!first) {
                representation.append(", ");
            } else {
                first = false;
            }

            representation.append("Wed");
        }
        if (this.isOnThursday()) {
            if (!first) {
                representation.append(", ");
            } else {
                first = false;
            }

            representation.append("Thu");
        }
        if (this.isOnFriday()) {
            if (!first) {
                representation.append(", ");
            } else {
                first = false;
            }

            representation.append("Fri");
        }
        if (this.isOnSaturday()) {
            if (!first) {
                representation.append(", ");
            }

            representation.append("Sat");
        }
        return representation.toString();
    }

    /**
     * @return Whether or not the event occurs on a Sunday.
     */
    public boolean isOnSunday() {
        return this.onSunday;
    }

    /**
     * @param onSunday Whether or not the event should occur on a Sunday.
     */
    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    /**
     * @return Whether or not the event occurs on a Monday.
     */
    public boolean isOnMonday() {
        return this.onMonday;
    }

    /**
     * @param onMonday Whether or not the event should occur on a Monday.
     */
    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    /**
     * @return Whether or not the event occurs on a Tuesday.
     */
    public boolean isOnTuesday() {
        return this.onTuesday;
    }

    /**
     * @param onTuesday Whether or not the event should occur on a Tuesday.
     */
    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    /**
     * @return Whether or not the event occurs on a Wednesday.
     */
    public boolean isOnWednesday() {
        return this.onWednesday;
    }

    /**
     * @param onWednesday Whether or not the event should occur on a Wednesday.
     */
    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    /**
     * @return Whether or not the event occurs on a Thursday
     */
    public boolean isOnThursday() {
        return this.onThursday;
    }

    /**
     * @param onThursday Whether or not the event should occur on a Thursday.
     */
    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    /**
     * @return Whether or not the event occurs on a Friday
     */
    public boolean isOnFriday() {
        return this.onFriday;
    }

    /**
     * @param onFriday Whether or not the event should occur on a Friday.
     */
    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    /**
     * @return Whether or not the event occurs on a Saturday
     */
    public boolean isOnSaturday() {
        return this.onSaturday;
    }

    /**
     * @param onSaturday Whether or not the event should occur on a Saturday.
     */
    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    /**
     * Determine whether or not this occurrence encapsulates the day of week
     * provided.
     *
     * @param dayOfWeek The day of week to check against.
     * @return true if the event occurs on the given day of week, false otherwise.
     */
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
