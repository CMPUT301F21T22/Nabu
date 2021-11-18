package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.ResetUserCommand;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class ResetUserCommandTest extends AuthenticatedFirestoreTest {
    private User user;
    private UserRepository userRepository;
    private HabitRepository habitRepository;
    private EventRepository eventRepository;

    @Override
    public void setUp() {
        super.setUp();
        this.userRepository = UserRepository.getInstance();
        this.habitRepository = HabitRepository.getInstance();
        this.eventRepository = EventRepository.getInstance();

        // Setup test situation.
        UserController userController = UserController.getInstance();
        HabitController habitController = HabitController.getInstance();
        EventController eventController = EventController.getInstance();

        try {
            // Create a testing user.
            this.createMockUser();
            FirebaseUser user = this.auth.getCurrentUser();
            assertNotNull(user);

            String eventId1 = eventController.add(new Event(new GregorianCalendar(1983, 9, 12).getTime())).get();
            String eventId2 = eventController.add(new Event(new GregorianCalendar(1983, 9, 12).getTime())).get();
            String eventId3 = eventController.add(new Event(new GregorianCalendar(1983, 9, 12).getTime())).get();
            String eventId4 = eventController.add(new Event(new GregorianCalendar(1983, 9, 12).getTime())).get();

            String habitId1 = habitController.add(new Habit()).get();
            String habitId2 = habitController.add(new Habit()).get();

            habitController.addEvent(habitId1, eventId1).get();
            habitController.addEvent(habitId1, eventId2).get();

            habitController.addEvent(habitId2, eventId3).get();
            habitController.addEvent(habitId2, eventId4).get();

            userController.addHabit(user.getUid(), habitId1).get();
            userController.addHabit(user.getUid(), habitId2).get();

            this.user = this.userRepository.retrieveUser(user.getUid()).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void resetUser() throws ExecutionException, InterruptedException {
        assertTrue(new ResetUserCommand(this.user).execute().get());

        await().until(() -> {
            Map<String, Habit> habits = this.habitRepository.getHabits().getValue();
            Map<String, Event> events = this.eventRepository.getEvents().getValue();

            if (habits != null && events != null) {
                return habits.size() == 0 && events.size() == 0;
            }
            return false;
        });

        User user = this.userRepository.retrieveUser(this.user.getId()).get();
        assertNotNull(user);
        assertEquals(new ArrayList<>(), user.getHabits());
    }

    @Test(expected = Exception.class)
    public void resetLocalUser() throws ExecutionException, InterruptedException {
        new ResetUserCommand(new User("", "", new ArrayList<>())).execute().get();
    }
}
