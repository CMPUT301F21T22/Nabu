package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.commands.DeleteEventCommand;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class DeleteEventCommandTest extends AuthenticatedFirestoreTest {
    private Habit parent;
    private Event event;
    private HabitRepository habitRepository;
    private EventRepository eventRepository;

    @Override
    public void setUp() {
        super.setUp();
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
            // Add a valid event to the database.
            String eventId = eventController.add(new Event(new GregorianCalendar(1983, 9, 12).getTime())).get();
            // Add a valid habit to the database.
            String habitId = habitController.add(new Habit()).get();
            // Attach the event to the habit.
            habitController.addEvent(habitId, eventId).get();
            // Attach the habit to the user.
            userController.addHabit(user.getUid(), habitId).get();
            this.parent = this.habitRepository.retrieveHabit(habitId).get();
            this.event = this.eventRepository.retrieveEvent(eventId).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void deleteEvent() throws ExecutionException, InterruptedException {
        assertTrue(new DeleteEventCommand(this.event).execute().get());

        await().until(() -> {
            Map<String, Event> events = this.eventRepository.getEvents().getValue();
            if (events != null) {
                return !events.containsKey(this.event.getId());
            }
            return false;
        });

        Habit habit = this.habitRepository.retrieveHabit(this.parent.getId()).get();
        assertNotNull(habit);
        assertFalse(habit.getEvents().contains(this.event.getId()));
    }

    @Test(expected = Exception.class)
    public void deleteLocalEvent() throws ExecutionException, InterruptedException {
        new DeleteEventCommand(new Event()).execute().get();
    }
}
