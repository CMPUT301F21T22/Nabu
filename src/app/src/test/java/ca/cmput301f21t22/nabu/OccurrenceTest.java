package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
        assertEquals(false, occurrence.isOnSunday());
        assertEquals(true, occurrence1.isOnSunday());

        occurrence.setOnSunday(true);
        occurrence1.setOnSunday(false);
        assertEquals(true, occurrence.isOnSunday());
        assertEquals(false, occurrence1.isOnSunday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnSunday(true);
        occurrence1.setOnSunday(false);
        assertEquals(true, occurrence.isOnSunday());
        assertEquals(false, occurrence1.isOnSunday());
    }

    @Test
    public void testOnMonday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, true, false,
                false, false, false, false);
        assertEquals(false, occurrence.isOnMonday());
        assertEquals(true, occurrence1.isOnMonday());

        occurrence.setOnMonday(true);
        occurrence1.setOnMonday(false);
        assertEquals(true, occurrence.isOnMonday());
        assertEquals(false, occurrence1.isOnMonday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnMonday(true);
        occurrence1.setOnMonday(false);
        assertEquals(true, occurrence.isOnMonday());
        assertEquals(false, occurrence1.isOnMonday());
    }

    @Test
    public void testOnTuesday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, true,
                false, false, false, false);
        assertEquals(false, occurrence.isOnTuesday());
        assertEquals(true, occurrence1.isOnTuesday());

        occurrence.setOnTuesday(true);
        occurrence1.setOnTuesday(false);
        assertEquals(true, occurrence.isOnTuesday());
        assertEquals(false, occurrence1.isOnTuesday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnTuesday(true);
        occurrence1.setOnTuesday(false);
        assertEquals(true, occurrence.isOnTuesday());
        assertEquals(false, occurrence1.isOnTuesday());
    }

    @Test
    public void testOnWednesday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                true, false, false, false);
        assertEquals(false, occurrence.isOnWednesday());
        assertEquals(true, occurrence1.isOnWednesday());

        occurrence.setOnWednesday(true);
        occurrence1.setOnWednesday(false);
        assertEquals(true, occurrence.isOnWednesday());
        assertEquals(false, occurrence1.isOnWednesday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnWednesday(true);
        occurrence1.setOnWednesday(false);
        assertEquals(true, occurrence.isOnWednesday());
        assertEquals(false, occurrence1.isOnWednesday());
    }

    @Test
    public void testOnThursday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, true, false, false);
        assertEquals(false, occurrence.isOnThursday());
        assertEquals(true, occurrence1.isOnThursday());

        occurrence.setOnThursday(true);
        occurrence1.setOnThursday(false);
        assertEquals(true, occurrence.isOnThursday());
        assertEquals(false, occurrence1.isOnThursday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnThursday(true);
        occurrence1.setOnThursday(false);
        assertEquals(true, occurrence.isOnThursday());
        assertEquals(false, occurrence1.isOnThursday());
    }

    @Test
    public void testOnFriday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, false, true, false);
        assertEquals(false, occurrence.isOnFriday());
        assertEquals(true, occurrence1.isOnFriday());

        occurrence.setOnFriday(true);
        occurrence1.setOnFriday(false);
        assertEquals(true, occurrence.isOnFriday());
        assertEquals(false, occurrence1.isOnFriday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnFriday(true);
        occurrence1.setOnFriday(false);
        assertEquals(true, occurrence.isOnFriday());
        assertEquals(false, occurrence1.isOnFriday());
    }

    @Test
    public void testOnSaturday() {
        Occurrence occurrence = new Occurrence();
        Occurrence occurrence1 = new Occurrence(false, false, false,
                false, false, false, true);
        assertEquals(false, occurrence.isOnSaturday());
        assertEquals(true, occurrence1.isOnSaturday());

        occurrence.setOnSaturday(true);
        occurrence1.setOnSaturday(false);
        assertEquals(true, occurrence.isOnSaturday());
        assertEquals(false, occurrence1.isOnSaturday());

        //Repeat to make sure function does not just reverse the value at each call
        occurrence.setOnSaturday(true);
        occurrence1.setOnSaturday(false);
        assertEquals(true, occurrence.isOnSaturday());
        assertEquals(false, occurrence1.isOnSaturday());
    }

    @Test
    public void testIsOnDayOfWeek() {
        Occurrence occurrence = new Occurrence();

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.SUNDAY));
        occurrence.setOnSunday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.SUNDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.MONDAY));
        occurrence.setOnMonday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.MONDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.TUESDAY));
        occurrence.setOnTuesday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.TUESDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        occurrence.setOnWednesday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.WEDNESDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.THURSDAY));
        occurrence.setOnThursday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.THURSDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.FRIDAY));
        occurrence.setOnFriday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.FRIDAY));

        assertEquals(false, occurrence.isOnDayOfWeek(DayOfWeek.SATURDAY));
        occurrence.setOnSaturday(true);
        assertEquals(true, occurrence.isOnDayOfWeek(DayOfWeek.SATURDAY));

        Occurrence occurrence1 = new Occurrence(true, true, true,
                true, false, false, false);
        Occurrence occurrence2 = new Occurrence(false, false, false,
                false, true, true, true);

        assertEquals(true, occurrence1.isOnDayOfWeek(DayOfWeek.SUNDAY));
        assertEquals(true, occurrence1.isOnDayOfWeek(DayOfWeek.MONDAY));
        assertEquals(true, occurrence1.isOnDayOfWeek(DayOfWeek.TUESDAY));
        assertEquals(true, occurrence1.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        assertEquals(false, occurrence1.isOnDayOfWeek(DayOfWeek.THURSDAY));
        assertEquals(false, occurrence1.isOnDayOfWeek(DayOfWeek.FRIDAY));
        assertEquals(false, occurrence1.isOnDayOfWeek(DayOfWeek.SATURDAY));

        assertEquals(false, occurrence2.isOnDayOfWeek(DayOfWeek.SUNDAY));
        assertEquals(false, occurrence2.isOnDayOfWeek(DayOfWeek.MONDAY));
        assertEquals(false, occurrence2.isOnDayOfWeek(DayOfWeek.TUESDAY));
        assertEquals(false, occurrence2.isOnDayOfWeek(DayOfWeek.WEDNESDAY));
        assertEquals(true, occurrence2.isOnDayOfWeek(DayOfWeek.THURSDAY));
        assertEquals(true, occurrence2.isOnDayOfWeek(DayOfWeek.FRIDAY));
        assertEquals(true, occurrence2.isOnDayOfWeek(DayOfWeek.SATURDAY));
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

        assertEquals(true, occurrence1.equals(new Occurrence(true, true,
                true, true, true, true, true)));
        assertEquals(false, occurrence1.equals(occurrence2));
        assertEquals(false, occurrence1.equals(occurrence3));
        assertEquals(false, occurrence1.equals(occurrence4));
        assertEquals(false, occurrence1.equals(occurrence5));
        assertEquals(false, occurrence1.equals(occurrence6));
        assertEquals(false, occurrence1.equals(occurrence7));
        assertEquals(false, occurrence1.equals(occurrence8));

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

        assertEquals(true, occurrence9.equals(new Occurrence()));
        assertEquals(false, occurrence9.equals(occurrence10));
        assertEquals(false, occurrence9.equals(occurrence11));
        assertEquals(false, occurrence9.equals(occurrence12));
        assertEquals(false, occurrence9.equals(occurrence13));
        assertEquals(false, occurrence9.equals(occurrence14));
        assertEquals(false, occurrence9.equals(occurrence15));
        assertEquals(false, occurrence9.equals(occurrence16));
    }






}
