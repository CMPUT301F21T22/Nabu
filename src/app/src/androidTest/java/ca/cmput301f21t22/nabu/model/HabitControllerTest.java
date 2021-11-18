package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;

public class HabitControllerTest extends FirestoreTest {
    private HabitController controller;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.controller = HabitController.getInstance();
        this.collection = this.db.collection("Habits");
    }

    @Test
    public void addHabit() throws ExecutionException, InterruptedException {
        Date startDate = new GregorianCalendar(2016, 6, 2).getTime();
        Occurrence occurrence = new Occurrence(false, true, true, false, false, true, false);
        List<String> events = new ArrayList<>();
        Habit habit = new Habit("Title", "Reason", startDate, occurrence, events, true);

        String habitId = this.controller.add(habit).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertEquals("Title", document.getString("title"));
            assertEquals("Reason", document.getString("reason"));
            assertEquals(new GregorianCalendar(2016, 6, 2).getTime(), document.getDate("startDate"));
            assertEquals(
                    new Occurrence(false, true, true, false, false, true, false),
                    document.get("occurrence", Occurrence.class));
            assertEquals(new ArrayList<>(), document.get("events"));
            assertEquals(true, document.getBoolean("shared"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void updateHabit() throws ExecutionException, InterruptedException {
        String habitId = this.controller.add(new Habit()).get();

        Date startDate = new GregorianCalendar(2016, 6, 2).getTime();
        Occurrence occurrence = new Occurrence(false, true, true, false, false, true, false);
        List<String> events = new ArrayList<>();
        Habit habit = new Habit("Title", "Reason", startDate, occurrence, events, true);

        habitId = this.controller.update(habitId, habit).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertEquals("Title", document.getString("title"));
            assertEquals("Reason", document.getString("reason"));
            assertEquals(new GregorianCalendar(2016, 6, 2).getTime(), document.getDate("startDate"));
            assertEquals(
                    new Occurrence(false, true, true, false, false, true, false),
                    document.get("occurrence", Occurrence.class));
            assertEquals(new ArrayList<>(), document.get("events"));
            assertEquals(true, document.getBoolean("shared"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void addEventToHabit() throws ExecutionException, InterruptedException {
        String habitId = this.controller.add(new Habit()).get();

        habitId = this.controller.addEvent(habitId, "iTqYxu1N6IO72GNHFBtX").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> events = (List<String>) document.get("events");
            assertNotNull(events);
            assertTrue(events.contains("iTqYxu1N6IO72GNHFBtX"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void clearEventsFromHabit() throws ExecutionException, InterruptedException {
        String habitId = this.controller.add(new Habit()).get();
        habitId = this.controller.addEvent(habitId, "event1").get();
        habitId = this.controller.addEvent(habitId, "event2").get();
        habitId = this.controller.addEvent(habitId, "event3").get();
        habitId = this.controller.addEvent(habitId, "event4").get();

        habitId = this.controller.clearEvents(habitId).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> events = (List<String>) document.get("events");
            assertNotNull(events);
            assertEquals(new ArrayList<>(), events);

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void deleteEventFromHabit() throws ExecutionException, InterruptedException {
        String habitId = this.controller.add(new Habit()).get();
        habitId = this.controller.addEvent(habitId, "iTqYxu1N6IO72GNHFBtX").get();

        habitId = this.controller.deleteEvent(habitId, "iTqYxu1N6IO72GNHFBtX").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> events = (List<String>) document.get("events");
            assertNotNull(events);
            assertFalse(events.contains("iTqYxu1N6IO72GNHFBtX"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void deleteHabit() throws ExecutionException, InterruptedException {
        String habitId = this.controller.add(new Habit()).get();

        assertTrue(this.controller.delete(habitId).get());

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(habitId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            assertFalse(document.exists());

            complete.set(true);
        });
        await().until(complete::get);
    }
}
