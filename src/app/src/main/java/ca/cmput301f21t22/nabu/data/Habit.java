package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * A data object storing all the details of a habit. A habit is associated with
 * an arbitrary number of events, and holds forward-references to the events. A
 * habit is associated implicitly with a single user, but does not hold a
 * back-reference. A habit can only be retrieved by ID from its associated user,
 * and not the other way around.
 * <p>
 * It is uniquely identified by an ID string within the context of the Firebase
 * database, but is a plain data object otherwise.
 */
public class Habit implements Serializable {
    @NonNull
    private final String id;
    @NonNull
    private String title;
    @NonNull
    private String reason;
    @NonNull
    private Date startDate;
    @NonNull
    private Occurrence occurrence;
    @NonNull
    private List<String> events;
    private boolean shared;

    /**
     * Create a local-only instance of Habit.
     *
     * @see Habit#Habit(String, String, Date, Occurrence, List, boolean)
     */
    public Habit() {
        this("", "", new Date(), new Occurrence(), new ArrayList<>(), false);
    }

    /**
     * Create a local-only instance of Habit.
     * <p>
     * Local-only instances of Habit cannot be used to updated existing habits
     * in the Firestore, as they are unlinked from a remote data instance. They
     * only store the data associated with the habit, not the reference.
     *
     * @param title      The title of the habit.
     * @param reason     The reason for wanting to develop the habit.
     * @param startDate  The date on which habit development should start.
     * @param occurrence The days of the week on which the habit occurs.
     * @param events     A list of references to remote events associated with this habit.
     * @param shared     Whether or not the habit should be publically shared.
     */
    public Habit(@NonNull String title,
                 @NonNull String reason,
                 @NonNull Date startDate,
                 @NonNull Occurrence occurrence,
                 @NonNull List<String> events,
                 boolean shared) {
        this("", title, reason, startDate, occurrence, events, shared);
    }

    /**
     * Create a remote-linked instance of Habit.
     * <p>
     * Remote-linked instances of Habit should only be created by the Repositories,
     * or "cloned" from another instance of Habit, as they carry an ID which
     * uniquely references a remote instance of the object. Since they carry this
     * remote reference, they can be used to update existing remote data.
     *
     * @param id         The unique remote ID of the habit.
     * @param title      The title of the habit.
     * @param reason     The reason for wanting to develop the habit.
     * @param startDate  The date on which habit development should start.
     * @param occurrence The days of the week on which the habit occurs.
     * @param events     A list of references to remote events associated with this habit.
     * @param shared     Whether or not the habit should be publicly shared.
     */
    public Habit(@NonNull String id,
                 @NonNull String title,
                 @NonNull String reason,
                 @NonNull Date startDate,
                 @NonNull Occurrence occurrence,
                 @NonNull List<String> events,
                 boolean shared) {
        this.id = id;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.occurrence = occurrence;
        this.events = events;
        this.shared = shared;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Habit habit = (Habit) o;
        return this.shared == habit.shared && this.id.equals(habit.id) && this.title.equals(habit.title) &&
               this.reason.equals(habit.reason) && this.startDate.equals(habit.startDate) &&
               this.occurrence.equals(habit.occurrence) && this.events.equals(habit.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                this.id, this.title, this.reason, this.startDate, this.occurrence, this.events, this.shared);
    }

    /**
     * @return The unique remote ID of the habit.
     */
    @NonNull
    public String getId() {
        return this.id;
    }

    /**
     * @return The title of the habit.
     */
    @NonNull
    public String getTitle() {
        return this.title;
    }

    /**
     * @param title The title that should be associated with the habit.
     */
    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    /**
     * @return The reason for wanting to develop the habit.
     */
    @NonNull
    public String getReason() {
        return this.reason;
    }

    /**
     * @param reason The reason for wanting to develop the habit.
     */
    public void setReason(@NonNull String reason) {
        this.reason = reason;
    }

    /**
     * @return The date on which habit development should start.
     */
    @NonNull
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * @param startDate The date on which habit development should start.
     */
    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return The days of the week on which the habit occurs.
     */
    @NonNull
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    /**
     * @param occurrence The days of the week on which the habit should occur.
     */
    public void setOccurrence(@NonNull Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    /**
     * @return A list of references to remote events associated with this habit.
     */
    @NonNull
    public List<String> getEvents() {
        return this.events;
    }

    /**
     * @param events A list of references to remote events that should be associated with this habit.
     */
    public void setEvents(@NonNull List<String> events) {
        this.events = events;
    }

    /**
     * @return Whether or not the habit should be publicly shared.
     */
    public boolean isShared() {
        return this.shared;
    }

    /**
     * @param shared Whether or not the habit should be publicly shared.
     */
    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
