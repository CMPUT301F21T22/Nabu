package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A data object storing details of a habit event.
 */
public class Event {
    @NonNull
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private String location;

    /**
     * Create an instance of Event.
     *
     * @param date Date on which the event was created.
     */
    public Event(@NonNull Date date) {
        this(date, null, null, null);
    }

    /**
     * Create an instance of Event.
     *
     * @param date      Date on which the event was created.
     * @param comment   Optional comment associated with the event.
     * @param photoPath Path to an optional photo associated with the event.
     * @param location  Optional location associated with the event.
     */
    public Event(@NonNull Date date, @Nullable String comment, @Nullable String photoPath, @Nullable String location) {
        this.date = date;
        this.comment = comment;
        this.photoPath = photoPath;
        this.location = location;
    }

    /**
     * Create an instance of Event from a Map returned by asMap.
     *
     * @param map Map returned by asMap.
     */
    public Event(@NonNull Map<String, Object> map) {
        Timestamp d = (Timestamp) map.get("date");
        if (d == null) {
            throw new IllegalArgumentException();
        }

        this.date = d.toDate();
        this.comment = (String) map.get("comment");
        this.photoPath = (String) map.get("photoPath");
        this.location = (String) map.get("location");
    }

    /**
     * Indicates whether another Object is equivalent to this one.
     *
     * @param obj Other object.
     * @return Whether the Objects are equivalent.
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Event)) {
            return false;
        } else {
            return this.equals((Event) obj);
        }
    }

    /**
     * Gets the date on which the event was created.
     *
     * @return The date.
     */
    @NonNull
    public Date getDate() {
        return this.date;
    }

    /**
     * Sets the date on which the event was created.
     *
     * @param date New date.
     */
    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    /**
     * Gets the optional comment associated with the event.
     *
     * @return The optional comment.
     */
    @Nullable
    public String getComment() {
        return this.comment;
    }

    /**
     * Sets the optional comment associated with the event.
     *
     * @param comment New comment, or null to clear.
     */
    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    /**
     * Gets the path to an optional photo associated with the event.
     *
     * @return The path to the optional photo.
     */
    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    /**
     * Sets the path to an optional photo associated with the event.
     *
     * @param photoPath New path, or null to clear.
     */
    public void setPhotoPath(@Nullable String photoPath) {
        this.photoPath = photoPath;
    }

    /**
     * Gets the optional location associated with the event.
     *
     * @return The optional location.
     */
    @Nullable
    public String getLocation() {
        return this.location;
    }

    /**
     * Sets the optional location associated with the event.
     *
     * @param location New location, or null to clear.
     */
    public void setLocation(@Nullable String location) {
        this.location = location;
    }

    /**
     * Indicates whether another Event is structurally equivalent to this one, with every field being the same.
     *
     * @param event Other event.
     * @return Whether the Events are equivalent.
     */
    public boolean equals(@NonNull Event event) {
        return Objects.equals(this.date, event.date) && Objects.equals(this.comment, event.comment) &&
               Objects.equals(this.photoPath, event.photoPath) && Objects.equals(this.location, event.location);
    }

    /**
     * Returns this object's data as a Map. Note the returned Map uses Firestore conventions for data types:
     * https://firebase.google.com/docs/firestore/manage-data/add-data#data_types
     *
     * @return A Map in which string keys correspond with their respective data fields.
     */
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(this.date));
        map.put("comment", this.comment);
        map.put("photoPath", this.photoPath);
        map.put("location", this.location);
        return map;
    }
}
