package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.HabitCard;
import ca.cmput301f21t22.nabu.data.Occurrence;

public class OccurrenceTest {

    @Test
    public void testCreate() {
        Occurrence occurrence1 = new Occurrence();
        Occurrence occurrence2 = new Occurrence(false, true, true,
                false, false, true, false);

        assertEquals(new Occurrence(false, false, false,
                false, false, false, false), occurrence1);
        assertEquals(occurrence2,new Occurrence(false, true, true,
                false, false, true, false));
    }


    @Test
    public void testHashCode() {
        boolean onSunday = true;
        boolean onMonday = false;
        boolean onTuesday = false;
        boolean onWednesday = true;
        boolean onThursday = false;
        boolean onFriday = true;
        boolean onSaturday = false;

        Occurrence occurrence = new Occurrence(onSunday, onMonday, onTuesday, onWednesday,
                onThursday, onFriday, onSaturday);
        assertEquals(Objects.hash(onSunday, onMonday, onTuesday, onWednesday, onThursday,
                onFriday, onSaturday), occurrence.hashCode());
    }

    @Test
    public void testToString() {
        Occurrence occurrence = new Occurrence(true, true, true,
                true,  true, true, true);
        assertEquals("Every Day", occurrence.toString());

        Occurrence occurrence1 = new Occurrence();
        assertEquals("", occurrence1.toString());

        Occurrence occurrence2 = new Occurrence(true, false, false,
                false, true, false, false);
        Occurrence occurrence3 = new Occurrence(true, true, true,
                true, true, true, false);
        Occurrence occurrence4 = new Occurrence(false, true, true,
                true, true, true, true);
        Occurrence occurrence5 = new Occurrence();
        occurrence5.setOnWednesday(true);

        assertEquals("Sun, Thu", occurrence2.toString());
        assertEquals("Sun, Mon, Tues, Wed, Thu, Fri", occurrence3.toString());
        assertEquals("Mon, Tues, Wed, Thu, Fri, Sat", occurrence4.toString());
        assertEquals("Wed", occurrence5.toString());
    }

    @Test
    public void testOnSunday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(true, false, false,
                false, false, false, false);
        assertFalse(occurrence.isOnSunday());
        assertTrue(occurrence1.isOnSunday());

        occurrence.setOnSunday(true);
        occurrence1.setOnSunday(false);
        assertTrue(occurrence.isOnSunday());
        assertFalse(occurrence1.isOnSunday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnSunday(true);
        occurrence1.setOnSunday(false);
        assertTrue(occurrence.isOnSunday());
        assertFalse(occurrence1.isOnSunday());
    }

    @Test
    public void testOnMonday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, true, false,
                false, false, false, false);
        assertFalse(occurrence.isOnMonday());
        assertTrue(occurrence1.isOnMonday());

        occurrence.setOnMonday(true);
        occurrence1.setOnMonday(false);
        assertTrue(occurrence.isOnMonday());
        assertFalse(occurrence1.isOnMonday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnMonday(true);
        occurrence1.setOnMonday(false);
        assertTrue(occurrence.isOnMonday());
        assertFalse(occurrence1.isOnMonday());
    }

    @Test
    public void testOnTuesday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, true,
                false, false, false, false);
        assertFalse(occurrence.isOnTuesday());
        assertTrue(occurrence1.isOnTuesday());

        occurrence.setOnTuesday(true);
        occurrence1.setOnTuesday(false);
        assertTrue(occurrence.isOnTuesday());
        assertFalse(occurrence1.isOnTuesday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnTuesday(true);
        occurrence1.setOnTuesday(false);
        assertTrue(occurrence.isOnTuesday());
        assertFalse(occurrence1.isOnTuesday());
    }

    @Test
    public void testOnWednesday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                true, false, false, false);
        assertFalse(occurrence.isOnWednesday());
        assertTrue(occurrence1.isOnWednesday());

        occurrence.setOnWednesday(true);
        occurrence1.setOnWednesday(false);
        assertTrue(occurrence.isOnWednesday());
        assertFalse(occurrence1.isOnWednesday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnWednesday(true);
        occurrence1.setOnWednesday(false);
        assertTrue(occurrence.isOnWednesday());
        assertFalse(occurrence1.isOnWednesday());
    }

    @Test
    public void testOnThursday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, true, false, false);
        assertFalse(occurrence.isOnThursday());
        assertTrue(occurrence1.isOnThursday());

        occurrence.setOnThursday(true);
        occurrence1.setOnThursday(false);
        assertTrue(occurrence.isOnThursday());
        assertFalse(occurrence1.isOnThursday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnThursday(true);
        occurrence1.setOnThursday(false);
        assertTrue(occurrence.isOnThursday());
        assertFalse(occurrence1.isOnThursday());
    }

    @Test
    public void testOnFriday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, false, true, false);
        assertFalse(occurrence.isOnFriday());
        assertTrue(occurrence1.isOnFriday());

        occurrence.setOnFriday(true);
        occurrence1.setOnFriday(false);
        assertTrue(occurrence.isOnFriday());
        assertFalse(occurrence1.isOnFriday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnFriday(true);
        occurrence1.setOnFriday(false);
        assertTrue(occurrence.isOnFriday());
        assertFalse(occurrence1.isOnFriday());
    }

    @Test
    public void testOnSaturday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, false, false, true);
        assertFalse(occurrence.isOnSaturday());
        assertTrue(occurrence1.isOnSaturday());

        occurrence.setOnSaturday(true);
        occurrence1.setOnSaturday(false);
        assertTrue(occurrence.isOnSaturday());
        assertFalse(occurrence1.isOnSaturday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnSaturday(true);
        occurrence1.setOnSaturday(false);
        assertTrue(occurrence.isOnSaturday());
        assertFalse(occurrence1.isOnSaturday());
    }

    @Test
    public void testIsOnDayOfWeek() {
        Occurrence occurrence = new Occurrence();

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.SUNDAY));
        occurrence.setOnSunday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.SUNDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.MONDAY));
        occurrence.setOnMonday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.MONDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.TUESDAY));
        occurrence.setOnTuesday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.TUESDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        occurrence.setOnWednesday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.WEDNESDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.THURSDAY));
        occurrence.setOnThursday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.THURSDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.FRIDAY));
        occurrence.setOnFriday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.FRIDAY));

        assertFalse(occurrence.isOnDayOfWeek(DayOfWeek.SATURDAY));
        occurrence.setOnSaturday(true);
        assertTrue(occurrence.isOnDayOfWeek(DayOfWeek.SATURDAY));

        Occurrence occurrence1 = new Occurrence(true, true, true,
                true, false, false, false);
        Occurrence occurrence2 = new Occurrence(false, false, false,
                false, true, true, true);

        assertTrue(occurrence1.isOnDayOfWeek(DayOfWeek.SUNDAY));
        assertTrue(occurrence1.isOnDayOfWeek(DayOfWeek.MONDAY));
        assertTrue(occurrence1.isOnDayOfWeek(DayOfWeek.TUESDAY));
        assertTrue(occurrence1.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        assertFalse(occurrence1.isOnDayOfWeek(DayOfWeek.THURSDAY));
        assertFalse(occurrence1.isOnDayOfWeek(DayOfWeek.FRIDAY));
        assertFalse(occurrence1.isOnDayOfWeek(DayOfWeek.SATURDAY));

        assertFalse(occurrence2.isOnDayOfWeek(DayOfWeek.SUNDAY));
        assertFalse(occurrence2.isOnDayOfWeek(DayOfWeek.MONDAY));
        assertFalse(occurrence2.isOnDayOfWeek(DayOfWeek.TUESDAY));
        assertFalse(occurrence2.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        assertTrue(occurrence2.isOnDayOfWeek(DayOfWeek.THURSDAY));
        assertTrue(occurrence2.isOnDayOfWeek(DayOfWeek.FRIDAY));
        assertTrue(occurrence2.isOnDayOfWeek(DayOfWeek.SATURDAY));
    }

    @Test
    public void testEquals() {
        Occurrence occurrence1 = new Occurrence(true, true, true,
                true, true, true, true);
        Occurrence occurrence2 = new Occurrence(false, true, true,
                true, true, true, true);
        Occurrence occurrence3 = new Occurrence(true, false,
                true, true, true, true, true);
        Occurrence occurrence4 = new Occurrence(true, true,
                false, true, true, true, true);
        Occurrence occurrence5 = new Occurrence(true, true,
                true, false, true, true, true);
        Occurrence occurrence6 = new Occurrence(true, true,
                true, true, false, true, true);
        Occurrence occurrence7 = new Occurrence(true, true,
                true, true, true, false, true);
        Occurrence occurrence8 = new Occurrence(true, true,
                true, true, true, true, false);

        assertEquals(occurrence1, new Occurrence(true, true,
                true, true, true, true, true));
        assertNotEquals(occurrence1, occurrence2);
        assertNotEquals(occurrence1, occurrence3);
        assertNotEquals(occurrence1, occurrence4);
        assertNotEquals(occurrence1, occurrence5);
        assertNotEquals(occurrence1, occurrence6);
        assertNotEquals(occurrence1, occurrence7);
        assertNotEquals(occurrence1, occurrence8);

        Occurrence occurrence9 = new Occurrence();
        Occurrence occurrence10 = new Occurrence();
        occurrence10.setOnSunday(true);
        Occurrence occurrence11 = new Occurrence();
        occurrence11.setOnMonday(true);
        Occurrence occurrence12 = new Occurrence();
        occurrence12.setOnTuesday(true);
        Occurrence occurrence13 = new Occurrence();
        occurrence13.setOnWednesday(true);
        Occurrence occurrence14 = new Occurrence();
        occurrence14.setOnThursday(true);
        Occurrence occurrence15 = new Occurrence();
        occurrence15.setOnFriday(true);
        Occurrence occurrence16 = new Occurrence();
        occurrence16.setOnSaturday(true);

        assertEquals(occurrence9, new Occurrence());
        assertNotEquals(occurrence9, occurrence10);
        assertNotEquals(occurrence9, occurrence11);
        assertNotEquals(occurrence9, occurrence12);
        assertNotEquals(occurrence9, occurrence13);
        assertNotEquals(occurrence9, occurrence14);
        assertNotEquals(occurrence9, occurrence15);
        assertNotEquals(occurrence9, occurrence16);
    }






}
