package ca.cmput301f21t22.nabu.data;

import java.util.Comparator;
import java.util.Date;

/**
 * A Comparator that compares two Event objects in chronological order, with earlier
 * events placed earlier in the order.
 */
public class EventChronologicalComparator implements Comparator<Event> {
    private static final Comparator<Date> dateComparator = Comparator.nullsFirst(Date::compareTo);
    private static final Comparator<Event> eventComparator =
            Comparator.nullsFirst(Comparator.comparing((Event::getDate), dateComparator));

    /**
     * Compare two Event objects.
     *
     * @param e1 The first Event object to compare.
     * @param e2 The second Event object to compare.
     * @return The result of the Event dates compared. See
     * {@link Date#compareTo(Date)} for semantics.
     */
    @Override
    public int compare(Event e1, Event e2) {
        return eventComparator.compare(e1, e2);
    }
}
