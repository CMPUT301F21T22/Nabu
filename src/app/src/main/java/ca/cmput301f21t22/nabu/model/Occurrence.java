package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A data object storing the days of the week on which an event occurs.
 */
public class Occurrence {
    private final boolean sunday;
    private final boolean monday;
    private final boolean tuesday;
    private final boolean wednesday;
    private final boolean thursday;
    private final boolean friday;
    private final boolean saturday;

    /**
     * Create an instance of Occurrence.
     *
     * @param sunday    Whether or not the event occurs on Sunday.
     * @param monday    Whether or not the event occurs on Monday.
     * @param tuesday   Whether or not the event occurs on Tuesday.
     * @param wednesday Whether or not the event occurs on Wednesday.
     * @param thursday  Whether or not the event occurs on Thursday.
     * @param friday    Whether or not the event occurs on Friday.
     * @param saturday  Whether or not the event occurs on Saturday.
     */
    public Occurrence(boolean sunday,
                      boolean monday,
                      boolean tuesday,
                      boolean wednesday,
                      boolean thursday,
                      boolean friday,
                      boolean saturday) {
        this.sunday = sunday;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
    }

    /**
     * Indicates whether another Object is equivalent to this one.
     *
     * @param obj Other object.
     * @return Whether the Objects are equivalent.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Occurrence)) {
            return false;
        } else {
            return this.equals((Occurrence) obj);
        }
    }

    public boolean onSunday() {
        return this.sunday;
    }

    public boolean onMonday() {
        return this.monday;
    }

    public boolean onTuesday() {
        return this.tuesday;
    }

    public boolean onWednesday() {
        return this.wednesday;
    }

    public boolean onThursday() {
        return this.thursday;
    }

    public boolean onFriday() {
        return this.friday;
    }

    public boolean onSaturday() {
        return this.saturday;
    }

    /**
     * Indicates whether another Occurrence is structurally equivalent to this one, with every field being the same.
     *
     * @param occurrence Other occurrence.
     * @return Whether the Occurrences are equivalent.
     */
    public boolean equals(@NonNull Occurrence occurrence) {
        return this.sunday == occurrence.sunday && this.monday == occurrence.monday &&
               this.tuesday == occurrence.tuesday && this.wednesday == occurrence.wednesday &&
               this.thursday == occurrence.thursday && this.friday == occurrence.friday &&
               this.saturday == occurrence.saturday;
    }
}
