package ca.cmput301f21t22.nabu.model;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public interface Event extends Observable<Event.Properties> {
    @Nullable
    Date getDate();

    void setDate(@Nullable Date date);

    @Nullable
    String getComment();

    void setComment(@Nullable String comment);

    @Nullable
    String getPhotoPath();

    void setPhotoPath(@Nullable String photoPath);

    @Nullable
    GeoPoint getLocation();

    void setLocation(@Nullable GeoPoint location);

    enum Properties {
        DATE, COMMENT, PHOTO_PATH, LOCATION
    }
}
