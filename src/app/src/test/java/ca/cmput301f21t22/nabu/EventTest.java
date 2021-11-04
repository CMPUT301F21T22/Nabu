package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;

public class EventTest {

        private Event mockEvent() {
            return new Event();
        }

        //Makes sure the different ways of creating an event all work
        @Test
        void testCreate() {
            GeoPoint geopoint = new GeoPoint(66, 4);
            Event event1 = new Event(new Date());
            Event event2 = new Event("501", new Date());
            Event event3 = new Event(new Date(), "Lost 30 pounds", "Photo/events/event202", geopoint);
            Event event4 = new Event("503", new Date(), "Flowers are blooming", "Photo/events/event202", geopoint);
        }

        //Tests to assert the hash code of the elements is preserved
        @Test
        void testHashCode() {
            String id = "555"
            Date date = new Date();
            String comment = "1st time jogging along the river";
            String photoPath = "Photo/events/event57";
            GeoPoint geoPoint = new GeoPoint(220, 430);
            Event event = new Event(id, date, comment, photoPath, geoPoint);
            assertEquals(Objects.hash(date, comment, photoPath, geoPoint), event.hashCode());
        }

        //Tests if the Id value is correctly returned
        @Test
        void testGetId() {
            String id = "601";
            Event event = new Event(id, new Date());
            assertEquals(id, event.getId());
        }

        //Tests if the date value is correctly returned
        @Test
        void testGetDate() {
            Date date = new Date(2021, 1, 19);
            Event event = new Event(date);
            assertEquals(date, event.getDate());

        }

        //Tests if date is properly set
        @Test
        void testSetDate() {
            Event event = new Event(new Date());
            Date date = new Date(2001, 6, 7);
            event.setDate(date);
            assertEquals(date, event.getDate());
        }

        //Tests if comment value is correctly returned
        @Test
        void testGetComment() {
            Event event = new Event(new Date());
        }

        //Tests if the photo path value is correctly returned
        @Test
        void testGetPhotopath() {

        }

        //Tests if the location value is correctly returned
        @Test
        void testGetLocation() {

        }

        //Tests if the Id value is properly set
        @Test
        void testSetLocation() {

        }
}
