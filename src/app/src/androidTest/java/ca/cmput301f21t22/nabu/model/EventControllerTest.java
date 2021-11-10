package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.model.controllers.EventController;

public class EventControllerTest extends FirestoreTest {
    private EventController controller;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.controller = EventController.getInstance();
        this.collection = this.db.collection("Events");
    }

    @Test
    public void addEvent() throws ExecutionException, InterruptedException {
        Date date = new GregorianCalendar(2019, 11, 28).getTime();
        GeoPoint location = new GeoPoint(53.512, -113.5076);
        Event event = new Event(date, "Some comment", "https://i.imgur.com/7cRKlOs.jpeg", location);

        String eventId = this.controller.add(event).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(eventId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertEquals(new GregorianCalendar(2019, 11, 28).getTime(), document.getDate("date"));
            assertEquals("Some comment", document.getString("comment"));
            assertEquals("https://i.imgur.com/7cRKlOs.jpeg", document.getString("photoPath"));
            assertEquals(new GeoPoint(53.512, -113.5076), document.getGeoPoint("location"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void updateEvent() throws ExecutionException, InterruptedException {
        String eventId = this.controller.add(new Event(new Date())).get();

        Date date = new GregorianCalendar(2019, 11, 28).getTime();
        GeoPoint location = new GeoPoint(53.512, -113.5076);
        Event event = new Event(date, "Some comment", "https://i.imgur.com/7cRKlOs.jpeg", location);

        eventId = this.controller.update(eventId, event).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(eventId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertEquals(new GregorianCalendar(2019, 11, 28).getTime(), document.getDate("date"));
            assertEquals("Some comment", document.getString("comment"));
            assertEquals("https://i.imgur.com/7cRKlOs.jpeg", document.getString("photoPath"));
            assertEquals(new GeoPoint(53.512, -113.5076), document.getGeoPoint("location"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void deleteEvent() throws ExecutionException, InterruptedException {
        String eventId = this.controller.add(new Event(new Date())).get();

        assertTrue(this.controller.delete(eventId).get());
        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(eventId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertFalse(document.exists());

            complete.set(true);
        });
        await().until(complete::get);
    }
}
