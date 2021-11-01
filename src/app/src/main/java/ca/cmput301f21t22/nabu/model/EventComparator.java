package ca.cmput301f21t22.nabu.model;

import java.util.Comparator;
import java.util.Date;

public class EventComparator implements Comparator<Event> {
    private final static Comparator<Event> EVENT_COMPARATOR =
            Comparator.nullsFirst(Comparator.comparing(Event::getDate, Comparator.nullsFirst(Date::compareTo)));

    @Override
    public int compare(Event event1, Event event2) {
        return EVENT_COMPARATOR.compare(event1, event2);
    }
}
