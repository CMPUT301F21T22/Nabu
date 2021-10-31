package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.Objects;

public class LocalEvent extends BaseObservable<Event.Properties> implements Event {
    @Nullable
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private GeoPoint location;

    public LocalEvent() {
        super(Event.Properties.class);
    }

    public LocalEvent(@Nullable Date date,
                      @Nullable String comment,
                      @Nullable String photoPath,
                      @Nullable GeoPoint location) {
        super(Event.Properties.class);

        this.date = date;
        this.comment = comment;
        this.photoPath = photoPath;
        this.location = location;
    }

    public LocalEvent(@NonNull Event event) {
        super(Event.Properties.class);

        this.date = event.getDate();
        this.comment = event.getComment();
        this.photoPath = event.getPhotoPath();
        this.location = event.getLocation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        LocalEvent that = (LocalEvent) o;
        return Objects.equals(this.date, that.date) && Objects.equals(this.comment, that.comment) &&
               Objects.equals(this.photoPath, that.photoPath) && Objects.equals(this.location, that.location);
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
        if (!Objects.equals(this.date, date)) {
            this.date = date;
            this.notifyPropertyChanged(Properties.DATE);
        }
    }

    @Override
    @Nullable
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(@Nullable String comment) {
        if (!Objects.equals(this.comment, comment)) {
            this.comment = comment;
            this.notifyPropertyChanged(Properties.COMMENT);
        }
    }

    @Override
    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    @Override
    public void setPhotoPath(@Nullable String photoPath) {
        if (!Objects.equals(this.photoPath, photoPath)) {
            this.photoPath = photoPath;
            this.notifyPropertyChanged(Properties.PHOTO_PATH);
        }
    }

    @Override
    @Nullable
    public GeoPoint getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(@Nullable GeoPoint location) {
        if (!Objects.equals(this.location, location)) {
            this.location = location;
            this.notifyPropertyChanged(Properties.LOCATION);
        }
    }
}
