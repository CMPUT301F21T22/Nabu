package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class HabitRepositoryTest extends FirestoreTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private HabitRepository repository;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.repository = HabitRepository.getInstance();
        this.collection = this.db.collection("Habits");
    }

    @Test
    public void liveHabitAdditions() {
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Habit>> observer = habits -> {
            Habit habit = habits.get("UiSfaFKK7WxX6HOAShoR");
            if (habit != null) {
                assertEquals("UiSfaFKK7WxX6HOAShoR", habit.getId());
                assertEquals("Some title", habit.getTitle());
                assertEquals("Some reason", habit.getReason());
                assertEquals(new GregorianCalendar(1971, 12, 3).getTime(), habit.getStartDate());
                assertEquals(new Occurrence(), habit.getOccurrence());
                assertEquals(new ArrayList<>(), habit.getEvents());
                assertTrue(habit.isShared());

                complete.set(true);
            }
        };

        this.repository.getHabits().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("title", "Some title");
        map.put("reason", "Some reason");
        map.put("startDate", new Timestamp(new GregorianCalendar(1971, 12, 3).getTime()));
        map.put("occurrence", new Occurrence());
        map.put("events", new ArrayList<>());
        map.put("shared", true);
        this.collection.document("UiSfaFKK7WxX6HOAShoR").set(map);

        await().until(complete::get);
        this.repository.getHabits().removeObserver(observer);
    }

    @Test
    public void liveHabitDeletions() {
        AtomicBoolean received = new AtomicBoolean(false);
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Habit>> observer = habits -> {
            Habit habit = habits.get("UiSfaFKK7WxX6HOAShoR");
            if (habit != null && !received.get()) {
                // Delete the habit.
                this.collection.document("UiSfaFKK7WxX6HOAShoR").delete();
                // Indicate the habit has been received at least once.
                received.set(true);
            } else if (habit == null && received.get()) {
                // If an habit has been received, and is now null, then it has been deleted.
                complete.set(true);
            }
        };

        this.repository.getHabits().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("title", "");
        map.put("reason", "");
        map.put("startDate", new Timestamp(new Date()));
        map.put("occurrence", new Occurrence());
        map.put("events", new ArrayList<>());
        map.put("shared", false);
        this.collection.document("UiSfaFKK7WxX6HOAShoR").set(map);

        await().until(complete::get);
        this.repository.getHabits().removeObserver(observer);
    }

    @Test
    public void liveHabitUpdates() {
        AtomicBoolean received = new AtomicBoolean(false);
        AtomicBoolean complete = new AtomicBoolean(false);
        Observer<Map<String, Habit>> observer = habits -> {
            Habit habit = habits.get("UiSfaFKK7WxX6HOAShoR");
            if (habit != null && !received.get()) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", "Some title");
                map.put("reason", "Some reason");
                map.put("startDate", new Timestamp(new GregorianCalendar(1971, 12, 3).getTime()));
                map.put("occurrence", new Occurrence());
                map.put("events", new ArrayList<>());
                map.put("shared", true);
                this.collection.document("UiSfaFKK7WxX6HOAShoR").set(map);

                received.set(true);
            } else if (habit != null && received.get()) {
                assertEquals("UiSfaFKK7WxX6HOAShoR", habit.getId());
                assertEquals("Some title", habit.getTitle());
                assertEquals("Some reason", habit.getReason());
                assertEquals(new GregorianCalendar(1971, 12, 3).getTime(), habit.getStartDate());
                assertEquals(new Occurrence(), habit.getOccurrence());
                assertEquals(new ArrayList<>(), habit.getEvents());
                assertTrue(habit.isShared());

                complete.set(true);
            }
        };

        this.repository.getHabits().observeForever(observer);

        Map<String, Object> map = new HashMap<>();
        map.put("title", "");
        map.put("reason", "");
        map.put("startDate", new Timestamp(new Date()));
        map.put("occurrence", new Occurrence());
        map.put("events", new ArrayList<>());
        map.put("shared", false);
        this.collection.document("UiSfaFKK7WxX6HOAShoR").set(map);

        await().until(complete::get);
        this.repository.getHabits().removeObserver(observer);
    }

    @Test
    public void findHabit() {
        Occurrence testOccurrence = new Occurrence(true, true, false, false, true, true, false);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("title", "Some title 1");
        map1.put("reason", "Some reason 1");
        map1.put("startDate", new Timestamp(new GregorianCalendar(1971, 12, 3).getTime()));
        map1.put("occurrence", testOccurrence);
        map1.put("events", new ArrayList<>());
        map1.put("shared", true);
        this.collection.document("Habit1").set(map1);

        Map<String, Object> map2 = new HashMap<>();
        map1.put("title", "Some title 2");
        map1.put("reason", "Some reason 2");
        map1.put("startDate", new Timestamp(new GregorianCalendar(1983, 6, 23).getTime()));
        map1.put("occurrence", new Occurrence());
        map1.put("events", new ArrayList<>());
        map1.put("shared", false);
        this.collection.document("Habit2").set(map1);

        await().until(() -> {
            Map<String, Habit> events = this.repository.getHabits().getValue();
            return events != null && events.size() >= 2;
        });

        Optional<Habit> habit = this.repository.findHabit(h -> h.getOccurrence().equals(testOccurrence));
        assertTrue(habit.isPresent());
        assertEquals("Some title 1", habit.get().getTitle());
        assertEquals("Some reason 1", habit.get().getReason());
        assertEquals(new GregorianCalendar(1971, 12, 3).getTime(), habit.get().getStartDate());
        assertEquals(testOccurrence, habit.get().getOccurrence());
        assertEquals(new ArrayList<>(), habit.get().getEvents());
        assertTrue(habit.get().isShared());
    }

    @Test
    public void retrieveHabit() {
        Map<String, Object> map = new HashMap<>();
        map.put("title", "Some title");
        map.put("reason", "Some reason");
        map.put("startDate", new Timestamp(new GregorianCalendar(1971, 12, 3).getTime()));
        map.put("occurrence", new Occurrence());
        map.put("events", new ArrayList<>());
        map.put("shared", true);
        this.collection.document("UiSfaFKK7WxX6HOAShoR").set(map);

        await().until(() -> {
            Map<String, Habit> events = this.repository.getHabits().getValue();
            return events != null && events.size() > 0;
        });

        AtomicBoolean complete = new AtomicBoolean(false);
        this.repository.retrieveHabit("UiSfaFKK7WxX6HOAShoR").thenAccept(habit -> {
            assertEquals("UiSfaFKK7WxX6HOAShoR", habit.getId());
            assertEquals("Some title", habit.getTitle());
            assertEquals("Some reason", habit.getReason());
            assertEquals(new GregorianCalendar(1971, 12, 3).getTime(), habit.getStartDate());
            assertEquals(new Occurrence(), habit.getOccurrence());
            assertEquals(new ArrayList<>(), habit.getEvents());
            assertTrue(habit.isShared());

            complete.set(true);
        });
        await().until(complete::get);
    }
}
