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
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = Calendar.DAY_OF_WEEK;
        String day;
        if (dayOfWeek == Calendar.SUNDAY) {
            day = "Sun";
        }
        else if (dayOfWeek == Calendar.MONDAY) {
            day = "Mon";
        }
        else if (dayOfWeek == Calendar.TUESDAY) {
            day = "Tue";
        }
        else if (dayOfWeek == Calendar.WEDNESDAY) {
            day = "Wed";
        }
        else if (dayOfWeek == Calendar.THURSDAY) {
            day = "Thu";
        }
        else if (dayOfWeek == Calendar.FRIDAY) {
            day = "Fri";
        }
        else {
            day = "Sat";
        }
        return day;

    }
}
