package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Event {
    @NonNull
    private final String id;
    @NonNull
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private GeoPoint location;

    public Event(@NonNull Date date) {
        this("", date);
    }

    public Event(@NonNull Date date,
                 @Nullable String comment,
                 @Nullable String photoPath,
                 @Nullable GeoPoint location) {
        this("", date, comment, photoPath, location);
    }

    public Event(@NonNull String id, @NonNull Date date) {
        this(id, date, null, null, null);
    }

    public Event(@NonNull String id,
                 @NonNull Date date,
                 @Nullable String comment,
                 @Nullable String photoPath,
                 @Nullable GeoPoint location) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.photoPath = photoPath;
        this.location = location;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return this.id.equals(event.id) && this.date.equals(event.date) &&
               Objects.equals(this.comment, event.comment) && Objects.equals(this.photoPath, event.photoPath) &&
               Objects.equals(this.location, event.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.date, this.comment, this.photoPath, this.location);
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    @NonNull
    public Date getDate() {
        return this.date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(@Nullable String photoPath) {
        this.photoPath = photoPath;
    }

    @Nullable
    public GeoPoint getLocation() {
        return this.location;
    }

    public void setLocation(@Nullable GeoPoint location) {
        this.location = location;
    }
}
