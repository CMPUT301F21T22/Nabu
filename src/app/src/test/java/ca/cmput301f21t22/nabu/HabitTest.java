package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;

public class HabitTest {

    //Tests to make sure the object's creation works
    @Test
    public void testCreate() {
        List<String> events = new ArrayList<>();
        try{
            Habit habit1 = new Habit();
        }catch (Exception e1){
            assertNull(e1);
        }
        try{
            Habit habit2 = new Habit("Pet Dog", "They're a good boy", new Date(), new Occurrence(), events, false);
        }catch (Exception e2){
            assertNull(e2);
        }
        try{
            Habit habit3 = new Habit("104", "Feed Cat", "They are hungry", new Date(), new Occurrence(), events, true);
        }catch (Exception e3){
            assertNull(e3);
        }
    }

    //Tests to assert the hash code of the elements is preserved
    @Test
    public void testHashCode() {
        String id = "555";
        String title = "Hug Opossum";
        String reason = "Soft";
        Date date = new Date(3011, 3, 27);
        Occurrence occurrence = new Occurrence(true, false, true, true, false, true, false);
        List<String> events = new ArrayList<>();
        events.add("Piece 1");
        boolean shared = false;
        Habit habit = new Habit(id, title, reason, date, occurrence, events, false);
        assertEquals(Objects.hash(title, reason, date, occurrence, events, false), habit.hashCode());
    }

    //Tests if the Id value is correctly returned
    @Test
    public void testGetId() {
        String id = "601";
        Habit habit = new Habit(id, "Eat food", "Sustenance", new Date(), new Occurrence(), new ArrayList<>(), false);
        assertEquals(id, habit.getId());
    }

    @Test
    public void testGetTitle() {
        String title = "Count fireflies";
        Habit habit =
                new Habit(title, "You would not believe your eyes...", new Date(), new Occurrence(), new ArrayList<>(),
                          false);
        assertEquals(title, habit.getTitle());
    }

    @Test
    public void testSetTitle() {
        String title = "Stop Counting Fireflies";
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(), new Occurrence(),
                                new ArrayList<>(), false);
        habit.setTitle(title);
        assertEquals(title, habit.getTitle());
    }

    @Test
    public void testGetReason() {
        String reason = "find lost teeth for tooth fairy";
        Habit habit = new Habit("count teeth", reason, new Date(), new Occurrence(), new ArrayList<>(), false);
        assertEquals(reason, habit.getReason());
    }

    @Test
    public void testSetReason() {
        String reason = "pass the time";
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(), new Occurrence(),
                                new ArrayList<>(), false);
        habit.setReason(reason);
        assertEquals(reason, habit.getReason());
    }

    @Test
    public void testGetStartDate() {
        Date date = new Date(2021, 1, 19);
        Habit habit = new Habit("Acquire stocks", "money", date, new Occurrence(), new ArrayList<>(), false);
        assertEquals(date, habit.getStartDate());
    }

    @Test
    public void testStartDate() {
        Date date = new Date(2006, 13, 12);
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(2001, 12, 13),
                                new Occurrence(), new ArrayList<>(), false);
        habit.setStartDate(date);
        assertEquals(date, habit.getStartDate());
    }

    @Test
    public void testGetOccurrence() {
        Occurrence occurrence = new Occurrence(true, true, true, false, false, true, true);
        Habit habit =
                new Habit("Go to work", "get funds for more dice", new Date(), occurrence, new ArrayList<>(), false);
        assertEquals(occurrence, habit.getOccurrence());
    }

    @Test
    public void testSetOccurrence() {
        Occurrence occurrence = new Occurrence(false, false, true, true, false, true, false);
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(), new Occurrence(),
                                new ArrayList<>(), false);
        habit.setOccurrence(occurrence);
        assertEquals(occurrence, habit.getOccurrence());
    }

    @Test
    public void testGetEvents() {
        List<String> events = new ArrayList<>();
        events.add("First Drink Mixed!!");
        events.add("Getting the hang of it!");
        Habit habit = new Habit("Feed fish", "Prevent starvation", new Date(), new Occurrence(), events, false);
        assertEquals(events, habit.getEvents());
    }

    @Test
    public void testSetEvents() {
        List<String> events1 = new ArrayList<>();
        events1.add("first of a million");
        List<String> events2 = new ArrayList<>();
        events2.add("One light forward");
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(), new Occurrence(),
                                events1, false);
        habit.setEvents(events2);
        assertEquals(events2, habit.getEvents());
    }

    @Test
    public void testIsShared() {
        boolean shared = true;
        Habit habit1 =
                new Habit("Drink less energy drinks", "Can't sleep", new Date(), new Occurrence(), new ArrayList<>(),
                          shared);
        assertTrue(habit1.isShared());
        shared = false;
        Habit habit2 =
                new Habit("Drink less energy drinks", "Can't sleep", new Date(), new Occurrence(), new ArrayList<>(),
                          shared);
        assertFalse(habit2.isShared());
    }

    @Test
    public void testSetShared() {
        boolean shared = false;
        Habit habit = new Habit("Count fireflies", "You would not believe your eyes...", new Date(), new Occurrence(),
                                new ArrayList<>(), shared);
        habit.setShared(true);
        assertTrue(habit.isShared());
    }

    @Test
    public void testEquals() {
        List<String> listChanged = new ArrayList<>();
        listChanged.add("wet food");
        Habit habit1 = new Habit("104", "Feed Cat", "They are hungry", new Date(2020, 11, 12),
                                 new Occurrence(false, false, false, false, false, false, false),
                                 new ArrayList<String>(), true);
        Habit habit3 =
                new Habit("105", "Feed Cat", "They are hungry", new Date(), new Occurrence(), new ArrayList<>(), true);
        Habit habit4 =
                new Habit("104", "Feed Dog", "They are hungry", new Date(), new Occurrence(), new ArrayList<>(), true);
        Habit habit5 =
                new Habit("104", "Feed Cat", "They are grumpy", new Date(), new Occurrence(), new ArrayList<>(), true);
        Habit habit6 = new Habit("104", "Feed Cat", "They are hungry", new Date(2019, 15, 22), new Occurrence(),
                                 new ArrayList<>(), true);
        Habit habit7 = new Habit("104", "Feed Cat", "They are hungry", new Date(),
                                 new Occurrence(true, false, false, false, false, false, false), new ArrayList<>(),
                                 true);
        Habit habit8 = new Habit("104", "Feed Cat", "They are hungry", new Date(), new Occurrence(), listChanged, true);
        Habit habit9 =
                new Habit("104", "Feed Cat", "They are hungry", new Date(), new Occurrence(), new ArrayList<>(), false);

        assertNotEquals(habit1, habit3);
        assertNotEquals(habit1, habit4);
        assertNotEquals(habit1, habit5);
        assertNotEquals(habit1, habit6);
        assertNotEquals(habit1, habit7);
        assertNotEquals(habit1, habit8);
        assertNotEquals(habit1, habit9);
    }
}
