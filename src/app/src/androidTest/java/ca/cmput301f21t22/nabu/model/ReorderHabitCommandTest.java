package ca.cmput301f21t22.nabu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.ReorderHabitCommand;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class ReorderHabitCommandTest extends AuthenticatedFirestoreTest {
    private User user;
    private UserRepository userRepository;

    public void setUp() {
        super.setUp();
        this.userRepository = UserRepository.getInstance();

        // Setup test situation.
        UserController userController = UserController.getInstance();
        HabitController habitController = HabitController.getInstance();

        try {
            // Create a testing user.
            this.createMockUser();
            FirebaseUser user = this.auth.getCurrentUser();
            assertNotNull(user);

            String habitId1 = habitController.add(new Habit()).get();
            String habitId2 = habitController.add(new Habit()).get();
            String habitId3 = habitController.add(new Habit()).get();
            String habitId4 = habitController.add(new Habit()).get();

            userController.addHabit(user.getUid(), habitId1).get();
            userController.addHabit(user.getUid(), habitId2).get();
            userController.addHabit(user.getUid(), habitId3).get();
            userController.addHabit(user.getUid(), habitId4).get();

            this.user = this.userRepository.retrieveUser(user.getUid()).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void reorderUserHabits() throws ExecutionException, InterruptedException {
        List<String> original = this.user.getHabits();

        assertTrue(new ReorderHabitCommand(this.user, 1, 3).execute().get());

        User updatedUser = this.userRepository.retrieveUser(this.user.getId()).get();
        assertNotNull(updatedUser);
        List<String> updated = updatedUser.getHabits();

        assertEquals(original.get(0), updated.get(0));
        assertEquals(original.get(1), updated.get(3));
        assertEquals(original.get(2), updated.get(1));
        assertEquals(original.get(3), updated.get(2));
    }

    @Test(expected = Exception.class)
    public void reorderLocalUserHabits() throws ExecutionException, InterruptedException {
        new ReorderHabitCommand(new User("", "", Arrays.asList("1", "2", "3")), 0, 1).execute().get();
    }
}
