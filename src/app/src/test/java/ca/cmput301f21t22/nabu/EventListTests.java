package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class EventListTests {
    @Test
    public void constructEmpty() {
        EventList eventList = new EventList();

        assertEquals(0, eventList.size());
        assertEquals(0, eventList.events().size());
        assertNull(eventList.get(UUID.randomUUID()));
    }

    @Test
    public void constructFromExistingEvents() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        List<Event> events = Arrays.asList(e1, e2);
        EventList eventList = new EventList(events);

        assertEquals(2, eventList.size());
        assertEquals(2, eventList.events().size());
        assertEquals(e1, eventList.get(e1.getId()));
        assertEquals(e2, eventList.get(e2.getId()));
    }

    @Test
    public void addDistinctEvents() {
        EventList eventList = new EventList();

        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));

        assertTrue(eventList.add(e1));
        assertTrue(eventList.add(e2));

        assertEquals(2, eventList.size());
        assertEquals(2, eventList.events().size());
        assertEquals(e1, eventList.get(e1.getId()));
        assertEquals(e2, eventList.get(e2.getId()));
    }

    @Test
    public void addDistinctEventsWithSameDate() {
        EventList eventList = new EventList();

        Event e1 = new Event(new Date(200));
        Event e2 = new Event(new Date(200));

        assertTrue(eventList.add(e1));
        assertTrue(eventList.add(e2));

        assertEquals(2, eventList.size());
        assertEquals(2, eventList.events().size());
        assertEquals(e1, eventList.get(e1.getId()));
        assertEquals(e2, eventList.get(e2.getId()));
    }

    @Test
    public void addEquivalentEvents() {
        UUID id = UUID.randomUUID();
        EventList eventList = new EventList();

        Event e1 = new Event(id, new Date(3219), null, null, null);
        Event e2 = new Event(id, new Date(6435543), null, null, null);

        assertTrue(eventList.add(e1));
        assertFalse(eventList.add(e2));

        assertEquals(1, eventList.size());
        assertEquals(1, eventList.events().size());
        assertEquals(e1, eventList.get(e1.getId()));
        //noinspection ConstantConditions
        assertEquals(e1.getDate(), eventList.get(e1.getId()).getDate());
    }

    @Test
    public void removeEvents() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        List<Event> events = Arrays.asList(e1, e2);
        EventList eventList = new EventList(events);

        assertEquals(2, eventList.size());
        assertEquals(2, eventList.events().size());

        assertEquals(e1, eventList.remove(e1.getId()));
        assertEquals(1, eventList.size());
        assertEquals(1, eventList.events().size());

        assertEquals(e2, eventList.remove(e2.getId()));
        assertEquals(0, eventList.size());
        assertEquals(0, eventList.events().size());
    }

    @Test
    public void removeNonExistent() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        List<Event> events = Arrays.asList(e1, e2);
        EventList eventList = new EventList(events);

        assertNull(eventList.remove(UUID.randomUUID()));
    }

    @Test
    public void clearEvents() {
        List<Event> events = Arrays.asList(new Event(new Date()), new Event(new Date()));
        EventList eventList = new EventList(events);

        assertEquals(2, eventList.size());
        assertEquals(2, eventList.events().size());

        eventList.clear();

        assertEquals(0, eventList.size());
        assertEquals(0, eventList.events().size());
    }

    @Test
    public void replaceExistingEvent() {
        UUID id = UUID.randomUUID();
        EventList eventList = new EventList();

        Event e1 = new Event(id, new Date(3219), null, null, null);
        Event e2 = new Event(id, new Date(6435543), null, null, null);

        eventList.add(e1);

        //noinspection ConstantConditions
        assertEquals(e1.getDate(), eventList.get(id).getDate());
        assertEquals(e1, eventList.replace(e2));
        //noinspection ConstantConditions
        assertEquals(e2.getDate(), eventList.get(id).getDate());
    }

    @Test
    public void replaceNonExistent() {
        EventList eventList = new EventList();
        assertNull(eventList.replace(new Event(new Date())));
    }

    @Test
    public void comparisons() {
        Event event1 = new Event(new Date(6543422));
        Event event2 = new Event(new Date(6543422));
        Event event3 = new Event(new Date(321332));
        Event event4 = new Event(new Date(765756765));

        EventList.EventComparator comparator = new EventList.EventComparator();

        assertNotEquals(0, comparator.compare(event1, event2));
        assertEquals(event1.compareTo(event2), comparator.compare(event1, event2));
        assertTrue(comparator.compare(event1, event3) > 0);
        assertTrue(comparator.compare(event1, event4) < 0);
    }
}
