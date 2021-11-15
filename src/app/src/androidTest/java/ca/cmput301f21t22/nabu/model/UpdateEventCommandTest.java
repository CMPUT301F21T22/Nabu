package ca.cmput301f21t22.nabu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.commands.UpdateEventCommand;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class UpdateEventCommandTest extends AuthenticatedFirestoreTest {
    private Habit parent;
    private Event event;
    private EventRepository eventRepository;
    private HabitRepository habitRepository;

    @Override
    public void setUp() {
        super.setUp();
        this.eventRepository = EventRepository.getInstance();
        this.habitRepository = HabitRepository.getInstance();

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
    public void updateEvent() throws ExecutionException, InterruptedException {
        assertNotEquals("", this.event.getId());
        assertEquals(new GregorianCalendar(1983, 9, 12).getTime(), this.event.getDate());

        this.event.setDate(new GregorianCalendar(1623, 4, 23).getTime());
        this.event = new UpdateEventCommand(this.event).execute().get();

        assertEquals(new GregorianCalendar(1623, 4, 23).getTime(), this.event.getDate());
    }

    @Test(expected = Exception.class)
    public void updateLocalEvent() throws ExecutionException, InterruptedException {
        new UpdateEventCommand(new Event()).execute().get();
    }
}
