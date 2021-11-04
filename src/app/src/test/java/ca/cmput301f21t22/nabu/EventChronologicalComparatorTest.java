package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Date;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.EventChronologicalComparator;

public class EventChronologicalComparatorTest {

    //Tests if two events with the same date are identified as the same
    @Test
    void testEqualEvents() {
        Event event1 = new Event(new Date(1811, 6, 15));
        Event event2 = new Event(new Date(1811, 6, 15));
        assertEquals(1, (new EventChronologicalComparator()).compare(event1, event2));
    }

    //Tests if two events with different dates are identified as different
    @Test
    void testNotEqualEvents() {
        Event event1 = new Event(new Date(1922, 6, 15));
        Event event2 = new Event(new Date(1923, 6, 15));
        assertEquals(0, (new EventChronologicalComparator()).compare(event1, event2));
    }
}
