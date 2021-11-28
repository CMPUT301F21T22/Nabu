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

    /**
     * Constructs a string representation of the current date
     * @return
     *  A string representation of the current date
     */
    public String getCurrentDate() {
        StringBuilder dateString = new StringBuilder();

        if (Calendar.MONTH == 0) {
            dateString.append("Jan ");
        }
        else if (Calendar.MONTH == 1) {
            dateString.append("Feb ");
        }
        else if (Calendar.MONTH == 2) {
            dateString.append("Mar ");
        }
        else if (Calendar.MONTH == 3) {
            dateString.append("Apr ");
        }
        else if (Calendar.MONTH == 4) {
            dateString.append("May ");
        }
        else if (Calendar.MONTH == 5) {
            dateString.append("Jun ");
        }
        else if (Calendar.MONTH == 6) {
            dateString.append("Jul ");
        }
        else if (Calendar.MONTH == 7) {
            dateString.append("Aug ");
        }
        else if (Calendar.MONTH == 8) {
            dateString.append("Sep ");
        }
        else if (Calendar.MONTH == 9) {
            dateString.append("Oct ");
        }
        else if (Calendar.MONTH == 10) {
            dateString.append("Nov ");
        }
        else {
            dateString.append("Dec ");
        }
        dateString.append(Calendar.DAY_OF_MONTH);
        dateString.append(", ");
        dateString.append(Calendar.YEAR);
        return dateString.toString();
    }
}
