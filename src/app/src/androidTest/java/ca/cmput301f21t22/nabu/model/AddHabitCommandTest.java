package ca.cmput301f21t22.nabu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.AddHabitCommand;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class AddHabitCommandTest extends AuthenticatedFirestoreTest {
    private User parent;
    private HabitRepository habitRepository;
    private UserRepository userRepository;

    @Override
    public void setUp() {
        super.setUp();
        this.habitRepository = HabitRepository.getInstance();
        this.userRepository = UserRepository.getInstance();

        // Setup test situation.
        UserController userController = UserController.getInstance();
        HabitController habitController = HabitController.getInstance();

        try {
            this.createMockUser();
            FirebaseUser user = this.auth.getCurrentUser();
            assertNotNull(user);
            // Add a valid habit to the database.
            String habitId = habitController.add(new Habit()).get();
            // Attach the habit to the user.
            userController.addHabit(user.getUid(), habitId).get();
            this.parent = this.userRepository.retrieveUser(user.getUid()).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void addLocalHabit() throws ExecutionException, InterruptedException {
        Habit habit = new Habit("Some title", "Some reason", new Date(), new Occurrence(), new ArrayList<>(), false);
        Habit result = new AddHabitCommand(this.parent, habit).execute().get();

        assertEquals(habit, result);
        assertNotEquals("", result.getId());
    }

    @Test
    public void addLinkedHabit() throws ExecutionException, InterruptedException {
        Map<String, Habit> habits = this.habitRepository.getHabits().getValue();
        assertNotNull(habits);
        Optional<Habit> query = habits.values().stream().findAny();
        assertTrue(query.isPresent());
        Habit habit = query.get();
        Habit result = new AddHabitCommand(this.parent, habit).execute().get();

        assertEquals(habit, result);
        assertNotEquals(habit.getId(), result.getId());

        User user = this.userRepository.retrieveUser(this.parent.getId()).get();
        assertNotNull(user);
        assertEquals(2, user.getHabits().size());
        assertTrue(user.getHabits().contains(habit.getId()));
        assertTrue(user.getHabits().contains(result.getId()));
    }

    @Test(expected = Exception.class)
    public void addHabitToLocalUser() throws ExecutionException, InterruptedException {
        User user = new User("", "some@noncemail.org", new ArrayList<>());
        new AddHabitCommand(user, new Habit()).execute().get();
    }
}
