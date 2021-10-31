package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class LiveEvent extends LiveDocument<Event.Properties> implements Event {
    @Nullable
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private GeoPoint location;

    public LiveEvent(@NonNull DocumentReference ref) {
        super(Event.Properties.class, ref);
    }

    public LiveEvent(@NonNull DocumentReference ref, @NonNull Event event) {
        super(Event.Properties.class, ref);

        this.setDate(event.getDate());
        this.setComment(event.getComment());
        this.setPhotoPath(event.getPhotoPath());
        this.setLocation(event.getLocation());
    }

    @Override
    public void readFields(@NonNull DocumentSnapshot snapshot) {
        Date date = snapshot.getDate("date");
        if (!Objects.equals(this.date, date)) {
            this.date = date;
            this.notifyPropertyChanged(Properties.DATE);
        }

        String comment = snapshot.getString("comment");
        if (!Objects.equals(this.comment, comment)) {
            this.comment = comment;
            this.notifyPropertyChanged(Properties.COMMENT);
        }

        String photoPath = snapshot.getString("photoPath");
        if (!Objects.equals(this.photoPath, photoPath)) {
            this.photoPath = photoPath;
            this.notifyPropertyChanged(Properties.PHOTO_PATH);
        }

        GeoPoint location = snapshot.getGeoPoint("location");
        if (!Objects.equals(this.location, location)) {
            this.location = location;
            this.notifyPropertyChanged(Properties.LOCATION);
        }
    }

    @Override
    public void clearFields() {
        if (this.date != null) {
            this.date = null;
            this.notifyPropertyChanged(Properties.DATE);
        }

        if (this.comment != null) {
            this.comment = null;
            this.notifyPropertyChanged(Properties.COMMENT);
        }

        if (this.photoPath != null) {
            this.photoPath = null;
            this.notifyPropertyChanged(Properties.PHOTO_PATH);
        }

        if (this.location != null) {
            this.location = null;
            this.notifyPropertyChanged(Properties.LOCATION);
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
        LiveEvent event = (LiveEvent) o;
        return Objects.equals(this.date, event.date) && Objects.equals(this.comment, event.comment) &&
               Objects.equals(this.photoPath, event.photoPath) && Objects.equals(this.location, event.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date, this.comment, this.photoPath, this.location);
    }

    @Override
    @Nullable
    public Date getDate() {
        return this.date;
    }

    @Override
    public void setDate(@Nullable Date date) {
        if (date != null) {
            this.ref.update("date", new Timestamp(date));
        } else {
            this.ref.update("date", null);
        }
    }

    @Override
    @Nullable
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(@Nullable String comment) {
        this.ref.update("comment", comment);
    }

    @Override
    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    @Override
    public void setPhotoPath(@Nullable String photoPath) {
        this.ref.update("photoPath", photoPath);
    }

    @Override
    @Nullable
    public GeoPoint getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(@Nullable GeoPoint location) {
        if (location != null) {
            this.ref.update("location", location);
        } else {
            this.ref.update("location", null);
        }
    }
}
