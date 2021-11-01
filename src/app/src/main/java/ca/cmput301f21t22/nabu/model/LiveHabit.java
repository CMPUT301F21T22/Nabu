package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class LiveHabit extends LiveDocument<Habit.Properties> implements Habit {
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

    public LiveHabit(@NonNull DocumentReference ref) {
        super(Habit.Properties.class, ref);
    }

    public LiveHabit(@NonNull DocumentReference ref, @NonNull Habit habit) {
        super(Habit.Properties.class, ref);

        this.setShared(habit.getShared());
        this.setTitle(habit.getTitle());
        this.setReason(habit.getReason());
        this.setStartDate(habit.getStartDate());
        this.setOccurrence(habit.getOccurrence());
        this.setEvents(habit.getEvents());
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
    public void clearFields() {
        if (this.shared != null) {
            this.shared = null;
            this.notifyPropertyChanged(Properties.SHARED);
        }

        if (this.title != null) {
            this.title = null;
            this.notifyPropertyChanged(Properties.TITLE);
        }

        if (this.reason != null) {
            this.reason = null;
            this.notifyPropertyChanged(Properties.REASON);
        }

        if (this.startDate != null) {
            this.startDate = null;
            this.notifyPropertyChanged(Properties.START_DATE);
        }

        if (this.occurrence != null) {
            this.occurrence = null;
            this.notifyPropertyChanged(Properties.OCCURRENCE);
        }

        if (this.events != null) {
            this.events = null;
            this.notifyPropertyChanged(Properties.EVENTS);
        }
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LiveHabit habit = (LiveHabit) o;
        return Objects.equals(this.shared, habit.shared) && Objects.equals(this.title, habit.title) &&
               Objects.equals(this.reason, habit.reason) && Objects.equals(this.startDate, habit.startDate) &&
               Objects.equals(this.occurrence, habit.occurrence) && Objects.equals(this.events, habit.events);
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
        this.ref.update("shared", shared);
    }

    @Override
    @Nullable
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(@Nullable String title) {
        this.ref.update("title", title);
    }

    @Override
    @Nullable
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(@Nullable String reason) {
        this.ref.update("reason", reason);
    }

    @Override
    @Nullable
    public Date getStartDate() {
        return this.startDate;
    }

    @Override
    public void setStartDate(@Nullable Date startDate) {
        if (startDate != null) {
            this.ref.update("startDate", new Timestamp(startDate));
        } else {
            this.ref.update("startDate", null);
        }
    }

    @Override
    @Nullable
    public Occurrence getOccurrence() {
        return this.occurrence;
    }

    @Override
    public void setOccurrence(@Nullable Occurrence occurrence) {
        this.ref.update("occurrence", occurrence);
    }

    @Override
    @Nullable
    public List<String> getEvents() {
        return this.events;
    }

    @Override
    public void setEvents(@Nullable List<String> events) {
        this.ref.update("events", events);
    }
}
