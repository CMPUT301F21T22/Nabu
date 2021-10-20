package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import ca.cmput301f21t22.nabu.model.Event;
import ca.cmput301f21t22.nabu.model.EventList;

public class EventListTests {
    @Test
    public void constructEmpty() {
        EventList eventList = new EventList();
        assertEquals(0, eventList.events().size());
    }

    @Test
    public void constructFromEvents() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        EventList eventList = new EventList(Arrays.asList(e1, e2));

        assertEquals(2, eventList.events().size());
        assertTrue(eventList.contains(e1));
        assertTrue(eventList.contains(e2));
    }

    @Test
    public void constructFromEventsWithSameDate() {
        Event e1 = new Event(new Date(200), null, null, null);
        Event e2 = new Event(new Date(200), "Comment", null, null);
        EventList eventList = new EventList(Arrays.asList(e1, e2));

        assertEquals(1, eventList.events().size());
        assertTrue(eventList.contains(e1));
        assertFalse(eventList.contains(e2));
    }

    @Test
    public void addEvents() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        EventList eventList = new EventList();

        assertTrue(eventList.add(e1));
        assertEquals(1, eventList.events().size());
        assertTrue(eventList.contains(e1));

        assertTrue(eventList.add(e2));
        assertEquals(2, eventList.events().size());
        assertTrue(eventList.contains(e2));
    }

    @Test
    public void addEventsWithSameDate() {
        Event e1 = new Event(new Date(200), null, null, null);
        Event e2 = new Event(new Date(200), "Comment", null, null);
        EventList eventList = new EventList();

        assertTrue(eventList.add(e1));
        assertEquals(1, eventList.events().size());
        assertTrue(eventList.contains(e1));

        assertFalse(eventList.add(e2));
        assertEquals(1, eventList.events().size());
        assertFalse(eventList.contains(e2));
    }

    @Test
    public void removeEvent() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        EventList eventList = new EventList(Arrays.asList(e1, e2));

        assertEquals(2, eventList.events().size());
        assertTrue(eventList.contains(e1));
        assertTrue(eventList.contains(e2));
        assertTrue(eventList.remove(e1));
        assertEquals(1, eventList.events().size());
        assertFalse(eventList.contains(e1));
        assertTrue(eventList.contains(e2));
    }

    @Test
    public void removeNonExistentEvent() {
        EventList eventList = new EventList();
        assertFalse(eventList.remove(new Event(new Date())));
    }

    @Test
    public void replaceEvent() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        EventList eventList = new EventList(Collections.singletonList(e1));

        assertEquals(1, eventList.events().size());
        assertTrue(eventList.contains(e1));
        assertFalse(eventList.contains(e2));
        assertTrue(eventList.replace(e1, e2));
        assertEquals(1, eventList.events().size());
        assertFalse(eventList.contains(e1));
        assertTrue(eventList.contains(e2));
    }

    @Test
    public void replaceNonExistentEvent() {
        EventList eventList = new EventList();
        assertFalse(eventList.replace(new Event(new Date()), new Event(new Date())));
    }

    @Test
    public void clearEvents() {
        Event e1 = new Event(new Date(3219));
        Event e2 = new Event(new Date(6435543));
        EventList eventList = new EventList(Arrays.asList(e1, e2));

        assertEquals(2, eventList.events().size());
        assertTrue(eventList.contains(e1));
        assertTrue(eventList.contains(e2));
        eventList.clear();
        assertEquals(0, eventList.events().size());
        assertFalse(eventList.contains(e1));
        assertFalse(eventList.contains(e2));
    }
}
