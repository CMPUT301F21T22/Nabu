package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class EventTests {
    @Test
    public void construct() {
        Date date = new Date();
        Event event = new Event(date);

        assertEquals(date, event.getDate());
        assertNull(event.getComment());
        assertNull(event.getPhotoPath());
        assertNull(event.getLocation());
    }

    @Test
    public void constructFull() {
        Date date = new Date();
        String comment = "Some comment.";
        String photoPath = "/home/default/photos/test.png";
        String location = "Edmonton, AB";
        Event event = new Event(date, comment, photoPath, location);

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
    public void structuralEquality() {
        Date date = new Date();
        String comment = "Some comment.";
        String photoPath = "/home/default/photos/test.png";
        String location = "Edmonton, AB";
        Event event1 = new Event(date, comment, photoPath, location);
        Event event2 = new Event(date, comment, photoPath, location);
        Event event3 = new Event(new Date());

        assertEquals(event1, event2);
        assertNotEquals(event2, event3);
    }

    @Test
    public void invalidEquality() {
        Event event = new Event(new Date());
        //noinspection ConstantConditions, SimplifiableAssertion
        assertFalse(event.equals((Object) null));
        //noinspection EqualsBetweenInconvertibleTypes, SimplifiableAssertion
        assertFalse(event.equals(new Date()));
    }
}
