package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.DeleteHabitCommand;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class DeleteHabitCommandTest extends AuthenticatedFirestoreTest {
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
    public void deleteHabit() throws ExecutionException, InterruptedException {
        assertTrue(new DeleteHabitCommand(this.habit).execute().get());

        await().until(() -> {
            Map<String, Habit> habits = this.habitRepository.getHabits().getValue();
            if (habits != null) {
                return !habits.containsKey(this.habit.getId());
            }
            return false;
        });

        User user = this.userRepository.retrieveUser(this.parent.getId()).get();
        assertNotNull(user);
        assertFalse(user.getHabits().contains(this.habit.getId()));
    }

    @Test(expected = Exception.class)
    public void deleteLocalHabit() throws ExecutionException, InterruptedException {
        new DeleteHabitCommand(new Habit()).execute().get();
    }
}
