package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;
import java.util.List;

import ca.cmput301f21t22.nabu.data.Occurrence;

public class Habit extends LiveDocument<Habit.Properties> {
    @Nullable
    private Boolean shared;
    @Nullable
    private String title;
    @Nullable
    private Date startDate;
    @Nullable
    private Occurrence occurrence;
    @Nullable
    private List<String> events;

    public Habit(@NonNull DocumentReference ref) {
        super(ref);
    }

    @Override
    protected void readFields(@NonNull DocumentSnapshot snapshot) {
        this.shared = snapshot.getBoolean("shared");
        this.notifyPropertyChanged(Properties.SHARED);

        this.title = snapshot.getString("title");
        this.notifyPropertyChanged(Properties.TITLE);

        this.startDate = snapshot.getDate("startDate");
        this.notifyPropertyChanged(Properties.START_DATE);

        this.occurrence = snapshot.get("occurrence", Occurrence.class);
        this.notifyPropertyChanged(Properties.OCCURRENCE);

        // noinspection unchecked
        this.events = (List<String>) snapshot.get("events");
        this.notifyPropertyChanged(Properties.EVENTS);
    }

    @Nullable
    public Boolean getShared() {
        return this.shared;
    }

    public void setShared(@Nullable Boolean shared) {
        if (this.alive) {
            this.ref.update("shared", shared);
        }
    }

    @Nullable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(@Nullable String title) {
        if (this.alive) {
            this.ref.update("title", title);
        }
    }

    @Nullable
    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(@Nullable Date startDate) {
        if (this.alive) {
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
        if (this.alive) {
            this.ref.update("occurrence", occurrence);
        }
    }

    @Nullable
    public List<String> getEvents() {
        return this.events;
    }

    public void setEvents(@Nullable List<String> events) {
        if (this.alive) {
            this.ref.update("events", events);
        }
    }

    public enum Properties {
        SHARED, TITLE, START_DATE, OCCURRENCE, EVENTS
    }
}
