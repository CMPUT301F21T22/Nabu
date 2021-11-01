package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Habit {
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

    @NonNull
    public String getId() {
        return this.id;
    }

    @NonNull
    public String getTitle() {
        return this.title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getReason() {
        return this.reason;
    }

    public void setReason(@NonNull String reason) {
        this.reason = reason;
    }

    @NonNull
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(@NonNull Date startDate) {
        this.startDate = startDate;
    }

    @NonNull
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    public void setOccurrence(@NonNull Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    @NonNull
    public List<String> getEvents() {
        return this.events;
    }

    public void setEvents(@NonNull List<String> events) {
        this.events = events;
    }

    public boolean isShared() {
        return this.shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }
}
