package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class Event extends LiveDocument<Event.Properties> {
    @Nullable
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private GeoPoint location;

    public Event(@NonNull DocumentReference ref) {
        super(ref);
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

    @Nullable
    public Date getDate() {
        return this.date;
    }

    public void setDate(@Nullable Date date) {
        if (this.isAlive()) {
            if (date != null) {
                this.ref.update("date", new Timestamp(date));
            } else {
                this.ref.update("date", null);
            }
        }
    }

    @Nullable
    public String getComment() {
        return this.comment;
    }

    public void setComment(@Nullable String comment) {
        if (this.isAlive()) {
            this.ref.update("comment", comment);
        }
    }

    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(@Nullable String photoPath) {
        if (this.isAlive()) {
            this.ref.update("photoPath", photoPath);
        }
    }

    @Nullable
    public GeoPoint getLocation() {
        return this.location;
    }

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
