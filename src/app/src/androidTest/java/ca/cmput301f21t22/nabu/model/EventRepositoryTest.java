package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Rule;
import org.junit.Test;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;

public class EventRepositoryTest extends FirestoreTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private EventRepository repository;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.repository = EventRepository.getInstance();
        this.collection = this.db.collection("Events");
    }

    @Test
    public void liveEventAdditions() {
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Event>> observer = events -> {
            Event event = events.get("iTqYxu1N6IO72GNHFBtX");
            if (event != null) {
                assertEquals("iTqYxu1N6IO72GNHFBtX", event.getId());
                assertEquals(new GregorianCalendar(2015, 11, 18).getTime(), event.getDate());
                assertEquals("Some comment", event.getComment());
                assertEquals("https://i.imgur.com/CTk5SXj.png", event.getPhotoPath());
                assertEquals(new GeoPoint(53.5453, -113.4502), event.getLocation());

                complete.set(true);
            }
        };

        this.repository.getEvents().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(new GregorianCalendar(2015, 11, 18).getTime()));
        map.put("comment", "Some comment");
        map.put("photoPath", "https://i.imgur.com/CTk5SXj.png");
        map.put("location", new GeoPoint(53.5453, -113.4502));
        this.collection.document("iTqYxu1N6IO72GNHFBtX").set(map);

        await().until(complete::get);
        this.repository.getEvents().removeObserver(observer);
    }

    @Test
    public void liveEventDeletions() {
        AtomicBoolean received = new AtomicBoolean(false);
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Event>> observer = events -> {
            Event event = events.get("iTqYxu1N6IO72GNHFBtX");
            if (event != null && !received.get()) {
                // Delete the event.
                this.collection.document("iTqYxu1N6IO72GNHFBtX").delete();
                // Indicate the event has been received at least once.
                received.set(true);
            } else if (event == null && received.get()) {
                // If an event has been received, and is now null, then it has been deleted.
                complete.set(true);
            }
        };

        this.repository.getEvents().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(new Date()));
        map.put("comment", "");
        map.put("photoPath", "");
        map.put("location", new GeoPoint(0, 0));
        this.collection.document("iTqYxu1N6IO72GNHFBtX").set(map);

        await().until(complete::get);
        this.repository.getEvents().removeObserver(observer);
    }

    @Test
    public void liveEventUpdates() {
        AtomicBoolean received = new AtomicBoolean(false);
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Event>> observer = events -> {
            Event event = events.get("iTqYxu1N6IO72GNHFBtX");
            if (event != null && !received.get()) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", new Timestamp(new GregorianCalendar(2015, 11, 18).getTime()));
                map.put("comment", "Some comment");
                map.put("photoPath", "https://i.imgur.com/CTk5SXj.png");
                map.put("location", new GeoPoint(53.5453, -113.4502));
                this.collection.document("iTqYxu1N6IO72GNHFBtX").update(map);

                received.set(true);
            } else if (event != null && received.get()) {
                assertEquals("iTqYxu1N6IO72GNHFBtX", event.getId());
                assertEquals(new GregorianCalendar(2015, 11, 18).getTime(), event.getDate());
                assertEquals("Some comment", event.getComment());
                assertEquals("https://i.imgur.com/CTk5SXj.png", event.getPhotoPath());
                assertEquals(new GeoPoint(53.5453, -113.4502), event.getLocation());

                complete.set(true);
            }
        };

        this.repository.getEvents().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(new Date()));
        map.put("comment", "");
        map.put("photoPath", "");
        map.put("location", new GeoPoint(0, 0));
        this.collection.document("iTqYxu1N6IO72GNHFBtX").set(map);

        await().until(complete::get);
        this.repository.getEvents().removeObserver(observer);
    }

    @Test
    public void retrieveEvent() {
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(new GregorianCalendar(2015, 11, 18).getTime()));
        map.put("comment", "Some comment");
        map.put("photoPath", "https://i.imgur.com/CTk5SXj.png");
        map.put("location", new GeoPoint(53.5453, -113.4502));
        this.collection.document("iTqYxu1N6IO72GNHFBtX").set(map);

        await().until(() -> {
            Map<String, Event> events = this.repository.getEvents().getValue();
            return events != null && events.size() > 0;
        });

        AtomicBoolean complete = new AtomicBoolean(false);
        this.repository.retrieveEvent("iTqYxu1N6IO72GNHFBtX").thenAccept(event -> {
            assertEquals("iTqYxu1N6IO72GNHFBtX", event.getId());
            assertEquals(new GregorianCalendar(2015, 11, 18).getTime(), event.getDate());
            assertEquals("Some comment", event.getComment());
            assertEquals("https://i.imgur.com/CTk5SXj.png", event.getPhotoPath());
            assertEquals(new GeoPoint(53.5453, -113.4502), event.getLocation());

            complete.set(true);
        });
        await().until(complete::get);
    }
}
