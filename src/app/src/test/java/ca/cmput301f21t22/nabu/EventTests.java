package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

public class EventTests {
    @Test
    public void construct() {
        Date date = new Date();
        Event event = new Event(date);

        assertNotNull(event.getId());
        assertEquals(date, event.getDate());
        assertNull(event.getComment());
        assertNull(event.getPhotoPath());
        assertNull(event.getLocation());
    }

    @Test
    public void constructFull() {
        UUID id = UUID.randomUUID();
        Date date = new Date();
        String comment = "Some comment.";
        String photoPath = "/home/default/photos/test.png";
        String location = "Edmonton, AB";
        Event event = new Event(id, date, comment, photoPath, location);

        assertEquals(id, event.getId());
        assertEquals(date, event.getDate());
        assertEquals(comment, event.getComment());
        assertEquals(photoPath, event.getPhotoPath());
        assertEquals(location, event.getLocation());
    }

    @Test
    public void updateFields() {
        Event event = new Event(new Date(4309712));

        Date date = new Date();
        String comment = "Some comment.";
        String photoPath = "/home/default/photos/test.png";
        String location = "Edmonton, AB";

        event.setDate(date);
        event.setComment(comment);
        event.setPhotoPath(photoPath);
        event.setLocation(location);

        assertEquals(date, event.getDate());
        assertEquals(comment, event.getComment());
        assertEquals(photoPath, event.getPhotoPath());
        assertEquals(location, event.getLocation());
    }

    @Test
    public void invalidInequality() {
        Event event = new Event(new Date());

        //noinspection ConstantConditions, SimplifiableAssertion
        assertFalse(event.equals((Object) null));
        //noinspection EqualsBetweenInconvertibleTypes, SimplifiableAssertion
        assertFalse(event.equals(new Date()));
    }

    @Test
    public void fieldInequality() {
        Event event1 = new Event(new Date(32132132));
        Event event2 = new Event(new Date(6543422));

        assertNotEquals(event1, event2);
    }

    @Test
    public void idInequality() {
        Event event1 = new Event(new Date(6543422));
        Event event2 = new Event(new Date(6543422));

        // Even if all fields are equal, if ids are not equal, the objects are not equivalent.
        assertEquals(event1.getDate(), event2.getDate());
        assertEquals(event1.getComment(), event2.getComment());
        assertEquals(event1.getPhotoPath(), event2.getPhotoPath());
        assertEquals(event1.getLocation(), event2.getLocation());

        assertNotEquals(event1.getId(), event2.getId());
        assertNotEquals(0, event1.compareTo(event2));
        assertNotEquals(event1, event2);
    }

    @Test
    public void idEquality() {
        UUID id = UUID.randomUUID();
        Event event1 = new Event(id, new Date(32132132), null, null, null);
        Event event2 = new Event(id, new Date(6543422), null, null, null);

        // On the other hand, even if fields differ, objects are equivalent when their IDs are the same.
        assertEquals(event1, event2);
        assertEquals(0, event1.compareTo(event2));
    }
}
