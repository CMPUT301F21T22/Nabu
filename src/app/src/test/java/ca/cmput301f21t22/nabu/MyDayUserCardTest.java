package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.MyDayCard;
import ca.cmput301f21t22.nabu.data.MyDayUserCard;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.data.User;

public class MyDayUserCardTest {
    private Event[] events = {new Event((new GregorianCalendar(2020, 11, 12))
            .getTime())};

    //Tests to make sure the object's creation works
    @Test
    public void testCreate() {
        MyDayUserCard myDayUserCard = new MyDayUserCard(new User("114", "boggles", new ArrayList<>()), new ArrayList<>());
    }

    //Tests if the User is kept consistent
    @Test
    public void testGetUser() {
        List<String> habitNames = new ArrayList<String>();
        habitNames.add("scheme");
        habitNames.add("eat");
        habitNames.add("plot");

        ArrayList<MyDayCard> habits = new ArrayList<>();
        Habit habit = new Habit("Pet Dog", "They're a good boy", new Date(), new Occurrence(), new ArrayList<>(), false);
        habits.add(new MyDayCard(habit, this.events));

        User user = new User("665", "boggles@gmail.com", habitNames);
        MyDayUserCard myDayUserCard = new MyDayUserCard(user, habits);
        assertEquals(user, myDayUserCard.getUser());
    }

    //Tests if the user id is kept consistent
    @Test
    public void testGetUserId() {
        List<String> habitNames = new ArrayList<String>();
        habitNames.add("walk dog");
        habitNames.add("feed dog");

        ArrayList<MyDayCard> habits = new ArrayList<>();
        Habit habit = new Habit("walk dog", "They need food", new Date(), new Occurrence(), new ArrayList<>(), false);
        habits.add(new MyDayCard(habit, this.events));

        String userId = "665";
        User user = new User(userId, "boggles@gmail.com", habitNames);
        MyDayUserCard myDayUserCard = new MyDayUserCard(user, habits);
        assertEquals(userId, myDayUserCard.getUserId());
    }

    //Tests if the user's email is kept consistent
    @Test
    public void testGetEmail() {
        List<String> habitNames = new ArrayList<String>();
        habitNames.add("plant roses");

        ArrayList<MyDayCard> habits = new ArrayList<>();
        Habit habit1 = new Habit("Plant roses", "I like how they look", new Date(), new Occurrence(), new ArrayList<>(), false);
        Habit habit2 = new Habit("place lights", "Let there be light", new Date(), new Occurrence(), new ArrayList<>(), true);
        habits.add(new MyDayCard(habit1, this.events));
        habits.add(new MyDayCard(habit2, this.events));

        String email = "bog@gmail.com";
        User user = new User("665", email, habitNames);
        MyDayUserCard myDayUserCard = new MyDayUserCard(user, habits);
        assertEquals(email, myDayUserCard.getEmail());
    }

    //Tests if the user's habits are kept consistent
    @Test
    public void testGetUserHabits() {
        List<String> habitNames = new ArrayList<String>();
        habitNames.add("Buy tea");
        habitNames.add("Brew tea");
        habitNames.add("Enjoy tea with friends");

        ArrayList<MyDayCard> habits = new ArrayList<>();
        Habit habit = new Habit("Brew tea", "I want to drink tea", new Date(), new Occurrence(), new ArrayList<>(), true);
        habits.add(new MyDayCard(habit, this.events));

        User user = new User("778", "bingo@hotmail.com", habitNames);
        MyDayUserCard myDayUserCard = new MyDayUserCard(user, habits);
        assertEquals(habits, myDayUserCard.getUserHabits());
    }

    //Tests if changing the user's habits works
    @Test
    public void testSetUserHabits() {
        List<String> habitNames = new ArrayList<String>();
        habitNames.add("Buy tea");
        habitNames.add("Brew tea");
        habitNames.add("Enjoy tea with friends");

        ArrayList<MyDayCard> habits = new ArrayList<>();
        Habit habit = new Habit("Brew tea", "I want to drink tea", new Date(), new Occurrence(), new ArrayList<>(), true);
        habits.add(new MyDayCard(habit, this.events));

        User user = new User("778", "bingo@hotmail.com", habitNames);
        MyDayUserCard myDayUserCard = new MyDayUserCard(user, habits);
        assertEquals(habits, myDayUserCard.getUserHabits());

        ArrayList<MyDayCard> newHabits = new ArrayList<>();
        Habit newHabit = new Habit("104","Feed Cat", "They are hungry", (new GregorianCalendar(2020, 11, 12)).getTime(), new Occurrence(false, false, false, false, false, false, false), new ArrayList<String>(), true);
        newHabits.add(new MyDayCard(newHabit, this.events));
        myDayUserCard.setHabits(newHabits);
        assertEquals(newHabits, myDayUserCard.getUserHabits());
    }
}
