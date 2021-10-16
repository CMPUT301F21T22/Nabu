package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class HabitTests {
    @Test
    public void construct() {
        String title = "Habit";
        String reason = "Reason";
        Date startDate = new Date(3223);
        Occurrence occurrence = new Occurrence(false, true, true, true, true, true, false);
        EventList eventList = new EventList();
        Habit habit = new Habit(title, reason, startDate, occurrence, eventList);

        assertEquals(title, habit.getTitle());
        assertEquals(reason, habit.getReason());
        assertEquals(startDate, habit.getStartDate());
        assertEquals(occurrence, habit.getOccurrence());
        assertEquals(eventList, habit.getEventList());
    }

    @Test
    public void updateFields() {
        Habit habit = new Habit("",
                                "",
                                new Date(),
                                new Occurrence(false, false, false, false, false, false, false),
                                new EventList());

        String title = "Habit";
        String reason = "Reason";
        Date startDate = new Date(3223);
        Occurrence occurrence = new Occurrence(false, true, true, true, true, true, false);
        EventList eventList = new EventList();

        habit.setTitle(title);
        habit.setReason(reason);
        habit.setStartDate(startDate);
        habit.setOccurrence(occurrence);
        habit.setEventList(eventList);

        assertEquals(title, habit.getTitle());
        assertEquals(reason, habit.getReason());
        assertEquals(startDate, habit.getStartDate());
        assertEquals(occurrence, habit.getOccurrence());
        assertEquals(eventList, habit.getEventList());
    }

    @Test
    public void structuralEquality() {
        String title = "Habit";
        String reason = "Reason";
        Date startDate = new Date(3223);
        Occurrence occurrence = new Occurrence(false, true, true, true, true, true, false);
        EventList eventList = new EventList();
        Habit habit1 = new Habit(title, reason, startDate, occurrence, eventList);
        Habit habit2 = new Habit(title, reason, startDate, occurrence, eventList);
        Habit habit3 = new Habit("",
                                 "",
                                 new Date(),
                                 new Occurrence(false, false, false, false, false, false, false),
                                 new EventList());

        assertEquals(habit1, habit2);
        assertNotEquals(habit2, habit3);
    }

    @Test
    public void invalidEquality() {
        Habit habit = new Habit("",
                                "",
                                new Date(),
                                new Occurrence(false, false, false, false, false, false, false),
                                new EventList());
        //noinspection ConstantConditions, SimplifiableAssertion
        assertFalse(habit.equals((Object) null));
        //noinspection EqualsBetweenInconvertibleTypes, SimplifiableAssertion
        assertFalse(habit.equals(new Date()));
    }
}
