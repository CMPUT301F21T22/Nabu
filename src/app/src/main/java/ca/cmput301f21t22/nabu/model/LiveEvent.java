package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class LiveEvent extends LiveDocument<LiveEvent.Properties> implements Event {
    @Nullable
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private GeoPoint location;

    public LiveEvent(@NonNull DocumentReference ref) {
        super(LiveEvent.Properties.class, ref);
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
        if (this.isAlive()) {
            if (date != null) {
                this.ref.update("date", new Timestamp(date));
            } else {
                this.ref.update("date", null);
            }
        }
    }

    @Override
    @Nullable
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(@Nullable String comment) {
        if (this.isAlive()) {
            this.ref.update("comment", comment);
        }
    }

    @Override
    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    @Override
    public void setPhotoPath(@Nullable String photoPath) {
        if (this.isAlive()) {
            this.ref.update("photoPath", photoPath);
        }
    }

    @Override
    @Nullable
    public GeoPoint getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(@Nullable GeoPoint location) {
        if (this.isAlive()) {
            if (location != null) {
                this.ref.update("location", location);
            } else {
                this.ref.update("location", null);
            }
        }
    }

    public enum Properties {
        DATE, COMMENT, PHOTO_PATH, LOCATION
    }
}
