package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.LatLngPoint;

public class EventTest {

    //Makes sure the different ways of creating an event all work
    @Test
    public void testCreate() {
        LatLngPoint location = new LatLngPoint(66, 4);
        Event event1 = new Event(new Date());
        Event event2 = new Event("501", new Date());
        Event event3 = new Event(new Date(), "Lost 30 pounds", "Photo/events/event202", location);
        Event event4 = new Event("503", new Date(), "Flowers are blooming", "Photo/events/event202", location);
    }

    //Tests to assert the hash code of the elements is preserved
    @Test
    public void testHashCode() {
        String id = "555";
        Date date = new Date();
        String comment = "1st time jogging along the river";
        String photoPath = "Photo/events/event57";
        LatLngPoint location = new LatLngPoint(20, 30);
        Event event = new Event(id, date, comment, photoPath, location);
        assertEquals(Objects.hash(date, comment, photoPath, location), event.hashCode());
    }

    //Tests if the Id value is correctly returned
    @Test
    public void testGetId() {
        String id = "601";
        Event event = new Event(id, new Date());
        assertEquals(id, event.getId());
    }

    //Tests if the date value is correctly returned
    @Test
    public void testGetDate() {
        Date date = new Date(2021, 1, 19);
        Event event = new Event(date);
        assertEquals(date, event.getDate());
    }

    //Tests if date is properly set
    @Test
    public void testSetDate() {
        Event event = new Event(new Date());
        Date date = new Date(2001, 6, 7);
        event.setDate(date);
        assertEquals(date, event.getDate());
    }

    //Tests if comment value is correctly returned
    @Test
    public void testGetComment() {
        String comment = "Just bought some yellows for my next canvas!";
        Event event = new Event(new Date(), comment, "", new LatLngPoint(22, 33));
        assertEquals(comment, event.getComment());
    }

    //Tests if the photo path value is correctly returned
    @Test
    public void testGetPhotopath() {
        String photoPath = "user/events/event233";
        Event event = new Event(new Date(), "Lowered my sugar intake by half", photoPath, new LatLngPoint(30, 50));
        assertEquals(photoPath, event.getPhotoPath());
    }

    //Tests if the location value is correctly returned
    @Test
    public void testGetLocation() {
        LatLngPoint location = new LatLngPoint(12, 73);
        Event event = new Event(new Date(), "", "", location);
        assertEquals(location, event.getLocation());
    }

    //Tests if the Id value is properly set
    @Test
    public void testSetLocation() {
        LatLngPoint location = new LatLngPoint(50, 73);
        Event event = new Event(new Date(), "", "", new LatLngPoint(66, 68));
        event.setLocation(location);
        assertEquals(location, event.getLocation());
    }
}
