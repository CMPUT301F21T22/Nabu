package ca.cmput301f21t22.nabu.data;

import java.util.Comparator;
import java.util.Date;

public class EventChronologicalComparator implements Comparator<Event> {
    private static final Comparator<Date> dateComparator = Comparator.nullsFirst(Date::compareTo);
    private static final Comparator<Event> eventComparator =
            Comparator.nullsFirst(Comparator.comparing((Event::getDate), dateComparator));

    @Override
    public int compare(Event e1, Event e2) {
        return eventComparator.compare(e1, e2);
    }
}
