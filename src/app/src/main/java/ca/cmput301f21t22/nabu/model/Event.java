package ca.cmput301f21t22.nabu.model;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Event extends LiveDocument<Event.Properties> {
    @Nullable
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private Location location;

    public Event(@NonNull DocumentReference ref) {
        super(ref);
    }

    @Override
    protected void readFields(@NonNull DocumentSnapshot snapshot) {
        this.date = snapshot.getDate("date");
        this.notifyPropertyChanged(Properties.DATE);

        this.comment = snapshot.getString("comment");
        this.notifyPropertyChanged(Properties.COMMENT);

        this.photoPath = snapshot.getString("photoPath");
        this.notifyPropertyChanged(Properties.PHOTO_PATH);

        GeoPoint gp = snapshot.getGeoPoint("location");
        if (gp != null) {
            this.location = new Location("");
            this.location.setLatitude(gp.getLatitude());
            this.location.setLongitude(gp.getLongitude());
        } else {
            this.location = null;
        }
        this.notifyPropertyChanged(Properties.LOCATION);
    }

    @Nullable
    public Date getDate() {
        return this.date;
    }

    public void setDate(@Nullable Date date) {
        if (this.alive) {
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
        if (this.alive) {
            this.ref.update("comment", comment);
        }
    }

    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    public void setPhotoPath(@Nullable String photoPath) {
        if (this.alive) {
            this.ref.update("photoPath", photoPath);
        }
    }

    @Nullable
    public Location getLocation() {
        return this.location;
    }

    public void setLocation(@Nullable Location location) {
        if (this.alive) {
            if (location != null) {
                this.ref.update("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
            } else {
                this.ref.update("location", null);
            }
        }
    }

    public enum Properties {
        DATE, COMMENT, PHOTO_PATH, LOCATION
    }
}
