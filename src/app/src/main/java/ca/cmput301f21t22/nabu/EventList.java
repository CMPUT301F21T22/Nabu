package ca.cmput301f21t22.nabu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A collection object holding an arbitrary number of unique Events, with uniqueness determined by event date. No
 * two Events in the EventList may have the same date.
 * <p>
 * EventList is bound to a Firestore collection, and is fully responsible for ensuring the consistency between the local
 * instance of the class, and the remote collection.
 */
public class EventList {
    @NonNull
    private final TreeSet<Event> events;

    /**
     * Create an instance of EventList.
     */
    public EventList() {
        this.events = new TreeSet<>(new EventComparator());
    }

    /**
     * Create an instance of EventList.
     *
     * @param events An initial collection of events.
     */
    public EventList(@NonNull Collection<Event> events) {
        this();
        this.events.addAll(events);
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
        } else if (!(obj instanceof EventList)) {
            return false;
        } else {
            return this.equals((EventList) obj);
        }
    }

    /**
     * Add an Event if it doesn't already exist within the list.
     *
     * @param event Event to add.
     * @return Whether or not the Event was successfully added.
     */
    public boolean add(@NonNull Event event) {
        return this.events.add(event);
    }

    /**
     * Remove all Events.
     */
    public void clear() {
        this.events.clear();
    }

    /**
     * Determines whether or not the list contains the provided Event.
     *
     * @param event Event to compare against.
     * @return Whether or not the list contains the Event.
     */
    public boolean contains(@NonNull Event event) {
        try {
            Event retrieved = this.events.tailSet(event).first();
            return event.equals(retrieved);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Retrieves a read-only wrapper for reading Events.
     *
     * @return An unmodifiable Collection of Events.
     */
    @NonNull
    public Collection<Event> events() {
        return Collections.unmodifiableCollection(this.events);
    }

    /**
     * Remove the Event matching the provided Event.
     *
     * @param event Event to remove.
     * @return Whether or not the Event was removed.
     */
    public boolean remove(@NonNull Event event) {
        return this.events.remove(event);
    }

    /**
     * Replace a target Event with a replacement Event.
     *
     * @param target      Event to find to be replaced.
     * @param replacement Event to replace with.
     * @return Whether or not the Event was replaced.
     */
    public boolean replace(@NonNull Event target, @NonNull Event replacement) {
        if (!this.events.remove(target)) {
            return false;
        }

        this.events.add(replacement);
        return true;
    }

    /**
     * Indicates whether another EventList is associated with the same Firestore collection as this one.
     *
     * @param eventList Other event list.
     * @return Whether the EventLists are equivalent.
     */
    public boolean equals(@NonNull EventList eventList) {
        return this == eventList;
    }

    /**
     * A comparator class for comparing two Events chronologically.
     */
    public static class EventComparator implements Comparator<Event> {
        /**
         * Compare two events chronologically.
         *
         * @param e1 First event to compare.
         * @param e2 Second event to compare.
         * @return 0 if the two events occured on the same date; non-zero otherwise.
         * @see Date#compareTo(Date)
         */
        @Override
        public int compare(@NonNull Event e1, @NonNull Event e2) {
            return e1.getDate().compareTo(e2.getDate());
        }
    }
}
