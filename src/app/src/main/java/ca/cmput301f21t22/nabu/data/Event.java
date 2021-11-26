package ca.cmput301f21t22.nabu.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A data object storing all the details of an event. An event is created every
 * time a habit is completed on a given day. An event is associated implicitly
 * with a single habit, but does not hold a back-reference. An event can only be
 * retrieved by ID from its associated habit, and not the other way around.
 * <p>
 * It is uniquely identified by an ID string within the context of the Firebase
 * database, but is a plain data object otherwise.
 */
public class Event implements Serializable {
    @NonNull
    private final String id;
    @NonNull
    private Date date;
    @Nullable
    private String comment;
    @Nullable
    private String photoPath;
    @Nullable
    private LatLngPoint location;

    /**
     * Create a local-only instance of Event.
     *
     * @see Event#Event(Date, String, String, LatLngPoint)
     */
    public Event() {
        this(new Date());
    }

    /**
     * Create a local-only instance of Event.
     *
     * @param date The date on which the event occurred.
     * @see Event#Event(Date, String, String, LatLngPoint)
     */
    public Event(@NonNull Date date) {
        this("", date);
    }

    /**
     * Create a local-only instance of Event.
     * <p>
     * Local-only instances of Event cannot be used to update existing Events
     * in the Firestore, as they are unlinked from a remote data instance. They
     * only store the data associated with the Event, not the reference.
     *
     * @param date      The date on which the event occurred.
     * @param comment   A comment associated with the event.
     * @param photoPath URL of a photo associated with the event.
     * @param location  A point on the earth associated with the event.
     */
    public Event(@NonNull Date date,
                 @Nullable String comment,
                 @Nullable String photoPath,
                 @Nullable LatLngPoint location) {
        this("", date, comment, photoPath, location);
    }

    /**
     * Create a remote-linked instance of Event.
     *
     * @param id   The unique remote ID of the event.
     * @param date The date on which the event occurred.
     * @see Event#Event(String, Date, String, String, LatLngPoint)
     */
    public Event(@NonNull String id, @NonNull Date date) {
        this(id, date, null, null, null);
    }

    /**
     * Create a remote-linked instance of Event.
     * <p>
     * Remote-linked instances of Event should only be created by the Repositories,
     * or "cloned" from another instance of Event, as they carry an ID which
     * uniquely references a remote instance of the object. Since they carry this
     * remote reference, they can be used to update existing remote data.
     *
     * @param id        The unique remote ID of the event.
     * @param date      The date on which the event occurred.
     * @param comment   A comment associated with the event.
     * @param photoPath URL of a photo associated with the event.
     * @param location  A point on the earth associated with the event.
     */
    public Event(@NonNull String id,
                 @NonNull Date date,
                 @Nullable String comment,
                 @Nullable String photoPath,
                 @Nullable LatLngPoint location) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.photoPath = photoPath;
        this.location = location;
    }

    /**
     * @return Whether two objects are structurally equivalent.
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Event event = (Event) o;
        return this.date.equals(event.date) && Objects.equals(this.comment, event.comment) &&
               Objects.equals(this.photoPath, event.photoPath) && Objects.equals(this.location, event.location);
    }

    /**
     * @return The hash code of the object's fields.
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.date, this.comment, this.photoPath, this.location);
    }

    /**
     * @return The unique remote ID of the event.
     */
    @NonNull
    public String getId() {
        return this.id;
    }

    /**
     * @return The date on which the event occurred.
     */
    @NonNull
    public Date getDate() {
        return this.date;
    }

    /**
     * @param date The date on which the event should occur.
     */
    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    /**
     * @return A comment associated with the event.
     */
    @Nullable
    public String getComment() {
        return this.comment;
    }

    /**
     * @param comment A comment that should be associated with the event.
     */
    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    /**
     * @return URL of a photo associated with the event.
     */
    @Nullable
    public String getPhotoPath() {
        return this.photoPath;
    }

    /**
     * @param photoPath URL of a photo that should be associated with the event.
     */
    public void setPhotoPath(@Nullable String photoPath) {
        this.photoPath = photoPath;
    }

    /**
     * @return A point on the earth associated with the event.
     */
    @Nullable
    public LatLngPoint getLocation() {
        return this.location;
    }

    /**
     * @param location A point on the earth that should be associated with the event.
     */
    public void setLocation(@Nullable LatLngPoint location) {
        this.location = location;
    }
}
