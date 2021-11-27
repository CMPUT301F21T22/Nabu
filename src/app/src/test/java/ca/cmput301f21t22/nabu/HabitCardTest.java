package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.data.LatLngPoint;
import ca.cmput301f21t22.nabu.data.Occurrence;

public class HabitCardTest {

    @Test
    public void testCreate() {
        try{
            HabitCard habitCard = new HabitCard(new Habit(), new ArrayList<Event>());
        }catch (Exception e) {
            assertNull(e);
        }
    }


    @Test
    public void testHashCode() {
        assertEquals(this.mockHabitCard().hashCode(),
                     Objects.hash(this.mockHabitCard().isExpanded(), this.mockHabitCard().getHabit(),
                                  this.mockHabitCard().getEvents()));
    }

    @Test
    public void testGetHabit() {
        Habit habit = new Habit(
                "105",
                "Feed Cat",
                "They are hungry",
                new Date(),
                new Occurrence(),
                new ArrayList<>(),
                true);
        HabitCard habitCard = new HabitCard(habit, new ArrayList<>());
        assertEquals(habit, habitCard.getHabit());
    }

    @Test
    public void testGetEvents() {
        Habit habit = new Habit("Drink less energy drinks",
                        "Can't sleep",
                        new Date(),
                        new Occurrence(),
                        new ArrayList<>(),
                        false);
        List<Event> events = new ArrayList<>();
        events.add(new Event(new Date(), "Lowered my sugar intake by half", "user/events/event233",
                             new LatLngPoint(30, 50)));
        HabitCard habitCard = new HabitCard(habit, events);
        assertEquals(events, habitCard.getEvents());
    }

    @Test
    public void testEquals() {
        HabitCard habitCard1 = this.mockHabitCard();
        HabitCard habitCard2 = new HabitCard(new Habit(), new ArrayList<>());
        HabitCard habitCard3 = this.mockHabitCard();
        habitCard3.getHabit().setShared(true);

        List<Event> changedList = new ArrayList<>();
        changedList.add(new Event());
        HabitCard habitCard4 = new HabitCard(
                new Habit("Pet Dog",
                        "They're a good boy",
                        new GregorianCalendar(1989 + 1900, 12, 13).getTime(),
                        new Occurrence(),
                        new ArrayList<>(),
                        false), changedList);

        assertEquals(this.mockHabitCard(), habitCard1);
        assertNotEquals(habitCard1, habitCard2);
        assertNotEquals(habitCard1, habitCard3);
        assertNotEquals(habitCard1, habitCard4);
    }

    @Test
    public void testSetExpanded() {
        HabitCard habitCard = this.mockHabitCard();
        habitCard.setExpanded(true);
        assertTrue(habitCard.isExpanded());
        habitCard.setExpanded(false);
        assertFalse(habitCard.isExpanded());
        habitCard.setExpanded(false);
        assertFalse(habitCard.isExpanded());
        habitCard.setExpanded(true);
        assertTrue(habitCard.isExpanded());
    }

    private HabitCard mockHabitCard() {
        Habit habit = new Habit("Pet Dog",
                "They're a good boy",
                new GregorianCalendar(89 + 19, 12, 13).getTime(),
                new Occurrence(),
                new ArrayList<>(),
                false);

        List<Event> events = new ArrayList<>();
        events.add(
                new Event(new GregorianCalendar(11 + 19, 11, 11).getTime(),
                        "Lost 30 pounds",
                        "Photo/events/event202",
                        new LatLngPoint(56, 2)));

        events.add(new Event(new GregorianCalendar(2021 + 1900, 11, 3).getTime(),
                "Lost 20 pounds",
                "Photo/events/event204",
                new LatLngPoint(81, 10)));
        return new HabitCard(habit, events);
    }
}
