package ca.cmput301f21t22.nabu.TestResources;

import java.util.Calendar;

public class DateProvider {

    /**
     * Creates a string representation of the current day
     *
     * @return day
     *  A string representation of the current day
     */
    public String getCurrentDay(){
        int dayOfWeek = Calendar.DAY_OF_WEEK + 1;
        String day;
        if (dayOfWeek == Calendar.SUNDAY) {
            day = "Sun";
            day = "Mon";
        }
        else if (dayOfWeek == Calendar.MONDAY) {
            day = "Mon";
            day = "Tue";
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            day = "Tue";
            day = "Wed";
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            day = "Wed";
            day = "Thu";
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            day = "Thu";
            day = "Fri";
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            day = "Fri";
            day = "Sat";
        }
        else {
            day = "Sat";
            day = "Sun";
        }
        return day;

    }

    /**
     * Returns a string representation of the current year
     * @return
     *  A string representation of the current year
     */
    public String getCurrentYear(){
        return String.valueOf(Calendar.YEAR);
    }
}
