package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.data.Occurrence;

public class MyDayCardTest {
    private Habit habit = new Habit("104","Feed Cat", "They are hungry",
            (new GregorianCalendar(2020, 11, 12)).getTime(),
            new Occurrence(false,
            false, false, false, false, false,
            false), new ArrayList<String>(), true);
    private Event[] events = {new Event((new GregorianCalendar(2020, 11, 12))
            .getTime())};

    //Tests to make sure the object's creation works
    @Test
    public void testCreate() {
        MyDayCard myDayCard = new MyDayCard(this.habit, this.events);
    }

    //Tests to assert the hash code of the elements is preserved
    @Test
    public void testHashCode() {
        MyDayCard myDayCard = new MyDayCard(this.habit, this.events);
        assertEquals(Objects.hash(this.habit)* 31 + Arrays.hashCode(this.events),
                myDayCard.hashCode());
    }

    //Tests if the Habit is kept consistent
    @Test
    public void testGetHabit() {
        MyDayCard myDayCard = new MyDayCard(this.habit, this.events);
        assertEquals(this.habit, myDayCard.getHabit());
    }

    //Tests if the Events are being kept consistent
    @Test
    public void testGetEvents() {
        MyDayCard myDayCard = new MyDayCard(this.habit, this.events);
        assertArrayEquals(this.events, myDayCard.getEvents());
    }

    //Tests what input gives what Icons
    @Test
    public void testGetIcon() {
        Habit habit1 = new Habit("111", "Feed lobster", "They are eating a lot",
                new Date(), new Occurrence(), new ArrayList<String>(), true);
        Habit habit2 = new Habit("112", "Feed chicken", "Too many seeds",
                new Date(), new Occurrence(true, true, true,
                true, true, true, true),
                new ArrayList<String>(), true);
        Event[] event = this.events;
        Event[] nullEvent = {null, null};
        MyDayCard myDayCard1 = new MyDayCard(habit1, event);
        MyDayCard myDayCard2 = new MyDayCard(habit2, nullEvent);
        MyDayCard myDayCard3 = new MyDayCard(habit2, event);
        assertEquals(MyDayCard.Icon.NOT_DUE, myDayCard1.getIcon(0));
        assertEquals(MyDayCard.Icon.INCOMPLETE, myDayCard2.getIcon(0));
        assertEquals(MyDayCard.Icon.COMPLETE, myDayCard3.getIcon(0));
        assertEquals(MyDayCard.Icon.NOT_DUE, myDayCard2.getIcon(1));
    }

    //Tests the equals method
    @Test
    public void testEquals() {
        Habit habit1 = new Habit("111", "Feed lobster", "They are eating a lot",
                new Date(), new Occurrence(), new ArrayList<String>(), true);
        Habit habit2 = new Habit("112", "Feed chicken", "Too many seeds",
                new Date(), new Occurrence(true, true, true,
                true, true, true, true),
                new ArrayList<String>(), true);
        Habit habit3 = habit1;

        assertTrue(habit1.equals(habit3));
        assertFalse(habit1.equals(habit2));
    }




}
