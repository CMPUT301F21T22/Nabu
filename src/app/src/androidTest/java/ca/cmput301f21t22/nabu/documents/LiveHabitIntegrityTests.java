package ca.cmput301f21t22.nabu.documents;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import ca.cmput301f21t22.nabu.model.Occurrence;
import ca.cmput301f21t22.nabu.model.LiveHabit;

public class LiveHabitIntegrityTests extends IntegrityTests {
    @Test
    public void WriteHabit() {
        LiveHabit habit = new LiveHabit(this.ref);

        assertNull(habit.getShared());
        assertNull(habit.getTitle());
        assertNull(habit.getReason());
        assertNull(habit.getStartDate());
        assertNull(habit.getOccurrence());
        assertNull(habit.getEvents());

        habit.setShared(true);
        await().until(() -> habit.getShared() != null);
        assertEquals(true, habit.getShared());

        habit.setTitle("Go to Work");
        await().until(() -> habit.getTitle() != null);
        assertEquals("Go to Work", habit.getTitle());

        habit.setReason("Need that money");
        await().until(() -> habit.getReason() != null);
        assertEquals("Need that money", habit.getReason());

        habit.setStartDate(new Date(4321098));
        await().until(() -> habit.getStartDate() != null);
        assertEquals(new Date(4321098), habit.getStartDate());

        habit.setOccurrence(new Occurrence(true, true, true, false, false, true, true));
        await().until(() -> habit.getOccurrence() != null);
        assertEquals(new Occurrence(true, true, true, false, false, true, true), habit.getOccurrence());

        habit.setEvents(Arrays.asList("Event1", "Event4", "Event2"));
        await().until(() -> habit.getEvents() != null);
        assertEquals(Arrays.asList("Event1", "Event4", "Event2"), habit.getEvents());
    }

    @Test
    public void WriteThenReadHabit() {
        LiveHabit write = new LiveHabit(this.ref);

        write.setShared(true);
        write.setTitle("Go to Work");
        write.setReason("Need that money");
        write.setStartDate(new Date(4321098));
        write.setOccurrence(new Occurrence(true, true, true, false, false, true, true));
        write.setEvents(Arrays.asList("Event1", "Event4", "Event2"));

        LiveHabit read = new LiveHabit(this.ref);

        await().until(() -> read.getShared() != null);
        assertEquals(true, read.getShared());

        await().until(() -> read.getTitle() != null);
        assertEquals("Go to Work", read.getTitle());

        await().until(() -> read.getReason() != null);
        assertEquals("Need that money", read.getReason());

        await().until(() -> read.getStartDate() != null);
        assertEquals(new Date(4321098), read.getStartDate());

        await().until(() -> read.getOccurrence() != null);
        assertEquals(new Occurrence(true, true, true, false, false, true, true), read.getOccurrence());

        await().until(() -> read.getEvents() != null);
        assertEquals(Arrays.asList("Event1", "Event4", "Event2"), read.getEvents());
    }

    @Test
    public void LiveUpdateHabit() {
        LiveHabit habit1 = new LiveHabit(this.ref);

        LiveHabit habit2 = new LiveHabit(this.ref);

        assertNull(habit1.getShared());
        assertNull(habit2.getShared());
        habit1.setShared(true);
        await().until(() -> Objects.equals(habit1.getShared(), habit2.getShared()));
        assertEquals(true, habit1.getShared());
        assertEquals(true, habit2.getShared());

        assertNull(habit1.getTitle());
        assertNull(habit2.getTitle());
        habit2.setTitle("Exercise");
        await().until(() -> Objects.equals(habit1.getTitle(), habit2.getTitle()));
        assertEquals("Exercise", habit1.getTitle());
        assertEquals("Exercise", habit2.getTitle());

        assertNull(habit1.getReason());
        assertNull(habit2.getReason());
        habit1.setReason("Need that healthy life");
        await().until(() -> Objects.equals(habit1.getReason(), habit2.getReason()));
        assertEquals("Need that healthy life", habit1.getReason());
        assertEquals("Need that healthy life", habit2.getReason());

        assertNull(habit1.getStartDate());
        assertNull(habit2.getStartDate());
        habit2.setStartDate(new Date(2138432));
        await().until(() -> Objects.equals(habit1.getStartDate(), habit2.getStartDate()));
        assertEquals(new Date(2138432), habit1.getStartDate());
        assertEquals(new Date(2138432), habit2.getStartDate());

        assertNull(habit1.getOccurrence());
        assertNull(habit2.getOccurrence());
        habit1.setOccurrence(new Occurrence());
        await().until(() -> Objects.equals(habit1.getOccurrence(), habit2.getOccurrence()));
        assertEquals(new Occurrence(), habit1.getOccurrence());
        assertEquals(new Occurrence(), habit2.getOccurrence());

        assertNull(habit1.getEvents());
        assertNull(habit2.getEvents());
        habit2.setEvents(Arrays.asList("Event3", "Event1"));
        await().until(() -> Objects.equals(habit1.getEvents(), habit2.getEvents()));
        assertEquals(Arrays.asList("Event3", "Event1"), habit1.getEvents());
        assertEquals(Arrays.asList("Event3", "Event1"), habit2.getEvents());
    }

    @Test
    public void WriteThenDeleteHabit() {
        LiveHabit habit = new LiveHabit(this.ref);

        habit.setShared(true);
        habit.setTitle("Go to Work");
        habit.setReason("Need that money");
        habit.setStartDate(new Date(4321098));
        habit.setOccurrence(new Occurrence(true, true, true, false, false, true, true));
        habit.setEvents(Arrays.asList("Event1", "Event4", "Event2"));
        await().until(() -> habit.getShared() != null && habit.getTitle() != null && habit.getStartDate() != null &&
                            habit.getOccurrence() != null && habit.getEvents() != null);

        habit.delete();
        await().until(() -> habit.getShared() == null && habit.getTitle() == null && habit.getStartDate() == null &&
                            habit.getOccurrence() == null && habit.getEvents() == null);
        this.ref.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(snapshot);
            Map<String, Object> data = snapshot.getData();
            assertNull(data);
        }).addOnFailureListener(task -> fail());
    }
}
