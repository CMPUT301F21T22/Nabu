package ca.cmput301f21t22.nabu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

/**
 * A data object storing details of a habit.
 */
public class Habit {
    @NonNull
    private String title;
    @NonNull
    private String reason;
    @NonNull
    private Date startDate;
    @NonNull
    private Occurrence occurrence;
    @NonNull
    private EventList eventList;

    /**
     * Create an instance of Habit.
     *
     * @param title      Title of the habit.
     * @param reason     Reason the habit was started.
     * @param startDate  Date on which to start the habit.
     * @param occurrence What days of the week the habit should be due.
     * @param eventList  List of events associated with the habit.
     */
    public Habit(@NonNull String title,
                 @NonNull String reason,
                 @NonNull Date startDate,
                 @NonNull Occurrence occurrence,
                 @NonNull EventList eventList) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.occurrence = occurrence;
        this.eventList = eventList;
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
        } else if (!(obj instanceof Habit)) {
            return false;
        } else {
            return this.equals((Habit) obj);
        }
    }

    /**
     * Gets the title of the habit.
     *
     * @return The title.
     */
    @NonNull
    public String getTitle() {
        return this.title;
    }

    /**
     * Sets the title of the habit.
     *
     * @param title New title.
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * Gets the reason the habit was started.
     *
     * @return The reason.
     */
    @NonNull
    public String getReason() {
        return this.reason;
    }

    /**
     * Sets the reason the habit was started.
     *
     * @param reason New reason.
     */
    public void setReason(@NonNull String reason) {
        this.reason = reason;
    }

    /**
     * Gets the date on which to start the habit.
     *
     * @return The date.
     */
    @NonNull
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Sets the date on which to start the habit.
     *
     * @param startDate New date.
     */
    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets what days of the week the habit should be due.
     *
     * @return The days of the week.
     */
    @NonNull
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    /**
     * Sets what days of the week the habit should be due.
     *
     * @param occurrence New occurrence.
     */
    public void setOccurrence(@NonNull Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    /**
     * Gets the list of events associated with the habit.
     *
     * @return The list of events.
     */
    @NonNull
    public EventList getEventList() {
        return this.eventList;
    }

    /**
     * Sets the list of events associated with the habit.
     *
     * @param eventList New event list.
     */
    public void setEventList(@NonNull EventList eventList) {
        this.eventList = eventList;
    }

    /**
     * Indicates whether another Habit is structurally equivalent to this one, with every field being the same.
     *
     * @param habit Other habit.
     * @return Whether the Habits are equivalent.
     */
    public boolean equals(@NonNull Habit habit) {
        return Objects.equals(this.title, habit.title) && Objects.equals(this.reason, habit.reason) &&
               Objects.equals(this.startDate, habit.startDate) && Objects.equals(this.occurrence, habit.occurrence) &&
               Objects.equals(this.eventList, habit.eventList);
    }
}
