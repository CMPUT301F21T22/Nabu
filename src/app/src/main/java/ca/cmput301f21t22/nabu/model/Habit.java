package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Occurrence;

public class Habit extends Document<Habit.Properties> {
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

    public Habit(@NonNull DocumentReference ref) {
        super(Habit.Properties.class, ref);
    }

    @Override
    public void readFields(@NonNull DocumentSnapshot snapshot) {
        Boolean shared = snapshot.getBoolean("shared");
        if (!Objects.equals(this.shared, shared)) {
            this.shared = shared;
            this.notifyPropertyChanged(Properties.SHARED);
        }

        String title = snapshot.getString("title");
        if (!Objects.equals(this.title, title)) {
            this.title = title;
            this.notifyPropertyChanged(Properties.TITLE);
        }

        String reason = snapshot.getString("reason");
        if (!Objects.equals(this.reason, reason)) {
            this.reason = reason;
            this.notifyPropertyChanged(Properties.REASON);
        }

        Date startDate = snapshot.getDate("startDate");
        if (!Objects.equals(this.startDate, startDate)) {
            this.startDate = startDate;
            this.notifyPropertyChanged(Properties.START_DATE);
        }

        Occurrence occurrence = snapshot.get("occurrence", Occurrence.class);
        if (!Objects.equals(this.occurrence, occurrence)) {
            this.occurrence = occurrence;
            this.notifyPropertyChanged(Properties.OCCURRENCE);
        }

        // noinspection unchecked
        List<String> events = (List<String>) snapshot.get("events");
        if (!Objects.equals(this.events, events)) {
            this.events = events;
            this.notifyPropertyChanged(Properties.EVENTS);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Habit habit = (Habit) o;
        return Objects.equals(this.shared, habit.shared) && Objects.equals(this.title, habit.title) &&
               Objects.equals(this.reason, habit.reason) && Objects.equals(this.startDate, habit.startDate) &&
               Objects.equals(this.occurrence, habit.occurrence) && Objects.equals(this.events, habit.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.shared, this.title, this.reason, this.startDate, this.occurrence, this.events);
    }

    @Nullable
    public Boolean getShared() {
        return this.shared;
    }

    public void setShared(@Nullable Boolean shared) {
        if (this.isAlive()) {
            this.ref.update("shared", shared);
        }
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        if (this.isAlive()) {
            this.ref.update("title", title);
        }
    }

    @Nullable
    public String getReason() {
        return this.reason;
    }

    public void setReason(@Nullable String reason) {
        if (this.isAlive()) {
            this.ref.update("reason", reason);
        }
    }

    @Nullable
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(@Nullable Date startDate) {
        if (this.isAlive()) {
            if (startDate != null) {
                this.ref.update("startDate", new Timestamp(startDate));
            } else {
                this.ref.update("startDate", null);
            }
        }
    }

    @Nullable
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    public void setOccurrence(@Nullable Occurrence occurrence) {
        if (this.isAlive()) {
            this.ref.update("occurrence", occurrence);
        }
    }

    @Nullable
    public List<String> getEvents() {
        return this.events;
    }

    public void setEvents(@Nullable List<String> events) {
        if (this.isAlive()) {
            this.ref.update("events", events);
        }
    }

    public enum Properties {
        SHARED, TITLE, REASON, START_DATE, OCCURRENCE, EVENTS
    }
}
