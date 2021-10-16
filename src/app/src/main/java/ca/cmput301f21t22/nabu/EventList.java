package ca.cmput301f21t22.nabu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;

/**
 * A collection object holding an arbitrary number of uniquely identifiable Events.
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
     * Add an Event if it doesn't already exist within the list.
     *
     * @param event Event to add.
     * @return Whether or not the Event was successfully added.
     */
    public boolean add(Event event) {
        // Try to get an item with the provided id.
        Event e = this.get(event.getId());
        // If no such item exists, we add.
        if (e == null) {
            this.events.add(event);
            return true;
        }
        // If such an item exists, we maintain consistency with the TreeSet api and leave the set unchanged.
        return false;
    }

    /**
     * Remove all Events.
     */
    public void clear() {
        this.events.clear();
    }

    /**
     * Retrieves a read-only wrapper for reading individual Events.
     *
     * @return An unmodifiable Collection of Events.
     */
    @NonNull
    public Collection<Event> events() {
        return Collections.unmodifiableCollection(this.events);
    }

    /**
     * Get the Event with the specified id.
     *
     * @param id Id of the Event to get.
     * @return The Event, or null if non-existent.
     */
    @Nullable
    public Event get(UUID id) {
        for (Event event : this.events) {
            if (id.equals(event.getId())) {
                return event;
            }
        }
        return null;
    }

    /**
     * Remove the Event with the specified id.
     *
     * @param id Id of the Event to remove.
     * @return The Event removed, or null if none was removed.
     */
    @Nullable
    public Event remove(UUID id) {
        Iterator<Event> it = this.events.iterator();
        while (it.hasNext()) {
            Event event = it.next();
            if (id.equals(event.getId())) {
                it.remove();
                return event;
            }
        }
        return null;
    }

    /**
     * Replaces the contents of an existing Event within the list with the same id as a provided Event.
     *
     * @param newEvent Event from which the target id and content fields should be read.
     * @return The contents of the previous Event, or null if nothing was replaced.
     */
    public Event replace(Event newEvent) {
        Event removedEvent = this.remove(newEvent.getId());
        // Only add the new event if something was actually removed.
        if (removedEvent != null) {
            this.events.add(newEvent);
        }
        return removedEvent;
    }

    /**
     * Returns the number of Events in the list.
     *
     * @return The number of Events.
     */
    public int size() {
        return this.events.size();
    }

    /**
     * A comparator for comparing two events chronologically, based on their UUIDs.
     */
    public static class EventComparator implements Comparator<Event> {
        /**
         * Compare two Events, taking into account both natural order (id) and semantic order (chronology).
         *
         * @param e1 The first Event to compare.
         * @param e2 The second Event to compare.
         * @return 0 iff the two objects are strictly equivalent in both id and date; non-zero otherwise.
         */
        @Override
        public int compare(@NonNull Event e1, @NonNull Event e2) {
            int naturalOrder = e1.compareTo(e2);
            int semanticOrder = e1.getDate().compareTo(e2.getDate());

            if (naturalOrder == 0 && semanticOrder == 0) {
                return 0;
            } else if (semanticOrder == 0) {
                return naturalOrder;
            } else {
                return semanticOrder;
            }
        }
    }
}
