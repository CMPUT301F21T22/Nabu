package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ca.cmput301f21t22.nabu.data.User;

public class UserTests {

    @Test
    public void testCreate() {
        User user = new User("1522", "boggles@swamp.bog", new ArrayList<>());
    }

    @Test
    public void testHasCode() {
        String id = "5622";
        String email = "bogglesJr@swamp.bog";
        List<String> habits = new ArrayList<>();
        habits.add("Water Lily pads");
        User user = new User(id, email, habits);
        assertEquals(Objects.hash(id, email, habits), user.hashCode());
    }

    @Test
    public void testGetID() {
        String id = "423";
        User user = new User(id, "drboggles@swamp.bog", new ArrayList<>());
        assertEquals(id, user.getId());
    }

    @Test
    public void testGetEmail() {
        String email = "bogglesSr@swamp.bog";
        User user = new User("5", email, new ArrayList<>());
        assertEquals(email, user.getEmail());
    }

    @Test
    public void  testSetEmail() {
        String email = "newbogglesSr@swamp.bog";
        User user = new User("5", "bogglesSr@swamp.bog", new ArrayList<>());
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testGetHabits() {
        List<String> habits = new ArrayList<>();
        habits.add("Try another bogloaf recipe");
        User user = new User("376", "Mcboggles@swamp.bog", habits);
        assertEquals(habits, user.getHabits());
    }

    @Test
    public void testSetHabits() {
        List<String> habits = new ArrayList<>();
        habits.add("Try another bogloaf recipe");
        List<String> notHabits = new ArrayList<>();
        notHabits.add("Gibberish");

        User user = new User("376", "Mcboggles@swamp.bog", notHabits);
        user.setHabits(habits);
        assertEquals(habits, user.getHabits());
    }

    @Test
    public void testEquals() {
        List<String> habits = new ArrayList<>();
        habits.add("Find more boggles");
        List<String> notHabits = new ArrayList<>();
        notHabits.add("Be tyrannical");

        User user = new User("333", "kingboggles@swamp.bog", habits);
        User user1 = new User("333","kingboggles@swamp.bog", habits);
        assertEquals(true, user.equals(user1));

        User notUser = new User("334", "kingboggles@swamp.bog", habits);
        assertEquals(false, user.equals(notUser));

        User notUser1 = new User("333", "boggles@swamp.bog", habits);
        assertEquals(false, user.equals(notUser));

        User notUser2 = new User("333", "kingboggles@swamp.bog", notHabits);
        assertEquals(false, user.equals(notUser));
    }
}
