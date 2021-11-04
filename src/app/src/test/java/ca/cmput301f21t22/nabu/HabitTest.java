package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;

public class HabitTest {

    @Test
    void testCreate() {
        List<String> events = new ArrayList<>();
        Habit habit1 = new Habit();
        Habit habit2 = new Habit("Pet Dog", "They're a good boy", new Date(),
                new Occurrence(), events, false);
        Habit habit3 = new Habit("104","Feed Cat", "They are hungry", new Date(),
                new Occurrence(), events, true);
    }

    //Tests to assert the hash code of the elements is preserved
    @Test
    void testHashCode() {
        String id = "555";
        String title = "Hug Opossum";
        String reason = "Soft";
        Date date = new Date(3011, 3, 27);
        Occurrence occurrence = new Occurrence(true, false, true,
                true, false, true, false);
        List<String> events = new ArrayList<>();
        events.add("Piece 1");
        Boolean shared = false;
        Habit habit = new Habit(id, title, reason, date, occurrence, events, shared);
        assertEquals(Objects.hash(id, title, reason, date, occurrence, events, shared), habit.hashCode());
    }

    //Tests if the Id value is correctly returned
    @Test
    void testGetId() {
        String id = "601";
        Habit habit = new Habit(id,"Eat food", "Sustenance", new Date(),
                new Occurrence(), new ArrayList<>(), false);
        assertEquals(id, habit.getId());
    }

    @Test
    void testGetTitle() {
        String title = "Count fireflies";
        Habit habit = new Habit(title, "You would not believe your eyes...", new Date(),
                new Occurrence(), new ArrayList<>(), false);
        assertEquals(title, habit.getTitle());
    }

    @Test
    void testSetTitle() {
        String title = "Stop Counting Fireflies";
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(), new Occurrence(), new ArrayList<>(), false);
        habit.setTitle(title);
        assertEquals(title, habit.getTitle());
    }

    @Test
    void testGetReason() {
        String reason = "find lost teeth for tooth fairy";
        Habit habit = new Habit("count teeth",  reason, new Date(), new Occurrence(),
                new ArrayList<>(), false);
        assertEquals(reason, habit.getReason());
    }

    @Test
    void testSetReason() {
        String reason = "pass the time";
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(), new Occurrence(), new ArrayList<>(), false);
        habit.setReason(reason);
        assertEquals(reason, habit.getTitle());
    }

    @Test
    void testGetStartDate() {
        Date date = new Date(2021, 1, 19);
        Habit habit = new Habit("Acquire stocks", "money", date, new Occurrence(),
                new ArrayList<>(), false);
        assertEquals(date, habit.getStartDate());
    }

    @Test
    void testStartDate() {
        Date date = new Date(2006, 13, 12);
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(2001, 12, 13), new Occurrence(), new ArrayList<>(), false);
        habit.setStartDate(date);
        assertEquals(date, habit.getStartDate());
    }

    @Test
    void testGetOccurrence() {
        Occurrence occurrence = new Occurrence(true, true, true,
                false, false, true, true);
        Habit habit = new Habit("Go to work", "get funds for more dice", new Date(),
                occurrence, new ArrayList<>(), false);
        assertEquals(occurrence, habit.getOccurrence());
    }

    @Test
    void testSetOccurrence() {
        Occurrence occurrence = new Occurrence(false, false, true,
                true, false, true, false);
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(), new Occurrence(), new ArrayList<>(), false);
        habit.setOccurrence(occurrence);
        assertEquals(occurrence, habit.getOccurrence());
    }

    @Test
    void testGetEvents() {
        List<String> events = new ArrayList<>();
        events.add("First Drink Mixed!!");
        events.add("Getting the hang of it!");
        Habit habit = new Habit("Feed fish", "Prevent starvation", new Date(),
                new Occurrence(), events, false);
        assertEquals(events, habit.getEvents());
    }

    @Test
    void testSetEvents() {
        List<String> events1 = new ArrayList<>();
        events1.add("first of a million");
        List<String> events2 = new ArrayList<>();
        events2.add("One light forward");
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(), new Occurrence(), events1, false);
        habit.setEvents(events2);
        assertEquals(events2, habit.getEvents());
    }

    @Test
    void testIsShared() {
        Boolean shared = true;
        Habit habit1 = new Habit("Drink less energy drinks", "Can't sleep", new Date(),
                new Occurrence(), new ArrayList<>(), shared);
        assertEquals(true, habit1.isShared());
        shared = false;
        Habit habit2 = new Habit("Drink less energy drinks", "Can't sleep", new Date(),
                new Occurrence(), new ArrayList<>(), shared);
        assertEquals(false, habit2.isShared());
    }

    @Test
    void testSetShared() {
        Boolean shared = true;
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...",
                new Date(), new Occurrence(), new ArrayList<>(), false);
        habit.setShared(shared);
        assertEquals(shared, habit.getTitle());
    }

    @Test
    void testEquals() {
        List<String> listChanged = new ArrayList<>();
        listChanged.add("wet food");
        Habit habit1 = new Habit("104","Feed Cat", "They are hungry",
                new Date(2020, 11, 12), new Occurrence(false,
                false, false, false, false, false,
                false), new ArrayList<String>(), true);
        Habit habit2 = habit1;
        Habit habit3 = new Habit("105","Feed Cat", "They are hungry", new Date(),
                new Occurrence(), new ArrayList<>(), true);
        Habit habit4 = new Habit("104","Feed Dog", "They are hungry", new Date(),
                new Occurrence(), new ArrayList<>(), true);
        Habit habit5 = new Habit("104","Feed Cat", "They are grumpy", new Date(),
                new Occurrence(), new ArrayList<>(), true);
        Habit habit6 = new Habit("104","Feed Cat", "They are hungry",
                new Date(2019, 15, 22), new Occurrence(), new ArrayList<>(), true);
        Habit habit7 = new Habit("104","Feed Cat", "They are hungry", new Date(),
                new Occurrence(true, false, false, false,
                        false, false, false), new ArrayList<>(), true);
        Habit habit8 = new Habit("104","Feed Cat", "They are hungry", new Date(),
                new Occurrence(), listChanged, true);
        Habit habit9 = new Habit("104","Feed Cat", "They are hungry", new Date(),
                new Occurrence(), new ArrayList<>(), false);

        assertEquals(habit1, habit2);
        assertNotEquals(habit1, habit3);
        assertNotEquals(habit1, habit4);
        assertNotEquals(habit1, habit5);
        assertNotEquals(habit1, habit6);
        assertNotEquals(habit1, habit7);
        assertNotEquals(habit1, habit8);
        assertNotEquals(habit1, habit9);
    }

}
