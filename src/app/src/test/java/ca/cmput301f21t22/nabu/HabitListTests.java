package ca.cmput301f21t22.nabu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import ca.cmput301f21t22.nabu.model.EventList;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.HabitList;
import ca.cmput301f21t22.nabu.model.Occurrence;

public class HabitListTests {
    public static Habit mockHabit1() {
        return new Habit("Run",
                         "",
                         new Date(),
                         new Occurrence(false, true, true, false, false, true, true),
                         new EventList());
    }

    public static Habit mockHabit2() {
        return new Habit("Work",
                         "",
                         new Date(),
                         new Occurrence(false, true, true, true, true, false, false),
                         new EventList());
    }

    @Test
    public void constructEmpty() {
        HabitList habitList = new HabitList();
        assertEquals(0, habitList.habits().size());
    }

    @Test
    public void constructFromHabits() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit2();
        HabitList habitList = new HabitList(Arrays.asList(h1, h2));

        assertEquals(2, habitList.habits().size());
        assertTrue(habitList.contains(h1));
        assertTrue(habitList.contains(h2));
    }

    @Test
    public void constructFromHabitsWithSameTitle() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit1();
        HabitList habitList = new HabitList(Arrays.asList(h1, h2));

        assertEquals(1, habitList.habits().size());
        assertTrue(habitList.contains(h1));
        assertFalse(habitList.contains(h2));
    }

    @Test
    public void addHabits() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit2();
        HabitList habitList = new HabitList();

        assertTrue(habitList.add(h1));
        assertEquals(1, habitList.habits().size());
        assertTrue(habitList.contains(h1));

        assertTrue(habitList.add(h2));
        assertEquals(2, habitList.habits().size());
        assertTrue(habitList.contains(h2));
    }

    @Test
    public void addHabitsWithSameTitle() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit1();
        HabitList habitList = new HabitList();

        assertTrue(habitList.add(h1));
        assertEquals(1, habitList.habits().size());
        assertTrue(habitList.contains(h1));

        assertFalse(habitList.add(h2));
        assertEquals(1, habitList.habits().size());
        assertFalse(habitList.contains(h2));
    }

    @Test
    public void removeHabit() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit2();
        HabitList habitList = new HabitList(Arrays.asList(h1, h2));

        assertEquals(2, habitList.habits().size());
        assertTrue(habitList.contains(h1));
        assertTrue(habitList.contains(h2));

        assertTrue(habitList.remove(h2));
        assertEquals(1, habitList.habits().size());
        assertTrue(habitList.contains(h1));
        assertFalse(habitList.contains(h2));
    }

    @Test
    public void removeNonExistentHabit() {
        HabitList habitList = new HabitList();
        assertFalse(habitList.remove(mockHabit1()));
    }

    @Test
    public void replaceHabit() {
        Habit h1 = mockHabit1();
        Habit h2 = mockHabit2();
        HabitList habitList = new HabitList(Collections.singletonList(h1));

        assertEquals(1, habitList.habits().size());
        assertTrue(habitList.contains(h1));
        assertFalse(habitList.contains(h2));

        assertTrue(habitList.replace(h1, h2));
        assertEquals(1, habitList.habits().size());
        assertFalse(habitList.contains(h1));
        assertTrue(habitList.contains(h2));
    }

    @Test
    public void replaceNonExistentHabit() {
        HabitList habitList = new HabitList();
        assertFalse(habitList.replace(mockHabit1(), mockHabit2()));
    }

    @Test
    public void clearHabits() {
        HabitList habitList = new HabitList(Arrays.asList(mockHabit1(), mockHabit2()));
        assertEquals(2, habitList.habits().size());

        habitList.clear();
        assertEquals(0, habitList.habits().size());
    }
}
