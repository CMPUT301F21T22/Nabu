package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Occurrence;

public class LocalHabit implements Habit {
    @Nullable
    private Boolean shared;
    @Nullable
    private String title;
    @Nullable
    private String reason;
    @Nullable
    private Date startDate;
    @Nullable
    private Occurrence occurrence;
    @Nullable
    private List<String> events;

    public LocalHabit() {
    }

    public LocalHabit(@Nullable Boolean shared,
                      @Nullable String title,
                      @Nullable String reason,
                      @Nullable Date startDate,
                      @Nullable Occurrence occurrence,
                      @Nullable List<String> events) {
        this.shared = shared;
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.occurrence = occurrence;
        this.events = events;
    }

    public LocalHabit(@NonNull Habit habit) {
        this.shared = habit.getShared();
        this.title = habit.getTitle();
        this.reason = habit.getReason();
        this.startDate = habit.getStartDate();
        this.occurrence = habit.getOccurrence();
        this.events = habit.getEvents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LocalHabit that = (LocalHabit) o;
        return Objects.equals(this.shared, that.shared) && Objects.equals(this.title, that.title) &&
               Objects.equals(this.reason, that.reason) && Objects.equals(this.startDate, that.startDate) &&
               Objects.equals(this.occurrence, that.occurrence) && Objects.equals(this.events, that.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shared, this.title, this.reason, this.startDate, this.occurrence, this.events);
    }

    @Override
    @Nullable
    public Boolean getShared() {
        return this.shared;
    }

    @Override
    public void setShared(@Nullable Boolean shared) {
        this.shared = shared;
    }

    @Override
    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    @Override
    @Nullable
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }

    @Override
    @Nullable
    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public void setStartDate(@Nullable Date startDate) {
        this.startDate = startDate;
    }

    @Override
    @Nullable
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    @Override
    public void setOccurrence(@Nullable Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    @Override
    @Nullable
    public List<String> getEvents() {
        return this.events;
    }

    @Override
    public void setEvents(@Nullable List<String> events) {
        this.events = events;
    }
}
