package ca.cmput301f21t22.nabu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.UpdateHabitCommand;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class UpdateHabitCommandTest extends AuthenticatedFirestoreTest {
    private User parent;
    private Habit habit;
    private UserRepository userRepository;
    private HabitRepository habitRepository;

    @Override
    public void setUp() {
        super.setUp();
        this.userRepository = UserRepository.getInstance();
        this.habitRepository = HabitRepository.getInstance();

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
            this.habit = this.habitRepository.retrieveHabit(habitId).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void updateHabit() throws ExecutionException, InterruptedException {
        assertNotEquals("", this.habit.getId());
        assertEquals("", this.habit.getTitle());

        this.habit.setTitle("Some title");
        this.habit = new UpdateHabitCommand(this.habit).execute().get();

        assertEquals("Some title", this.habit.getTitle());
    }

    @Test(expected = Exception.class)
    public void updateLocalHabit() throws ExecutionException, InterruptedException {
        new UpdateHabitCommand(new Habit()).execute().get();
    }
}
