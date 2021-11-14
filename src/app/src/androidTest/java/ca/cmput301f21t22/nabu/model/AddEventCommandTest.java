package ca.cmput301f21t22.nabu.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;

import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.commands.AddEventCommand;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class AddEventCommandTest extends AuthenticatedFirestoreTest {
    private Habit parent;
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
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void addLocalEvent() throws ExecutionException, InterruptedException {
        Event event = new Event(new GregorianCalendar(2000, 10, 19).getTime());
        Event result = new AddEventCommand(this.parent, event).execute().get();

        assertEquals(event, result);
        assertNotEquals("", result.getId());
    }

    @Test
    public void addLinkedEvent() throws ExecutionException, InterruptedException {
        Map<String, Event> events = this.eventRepository.getEvents().getValue();
        assertNotNull(events);
        Optional<Event> query = events.values().stream().findAny();
        assertTrue(query.isPresent());
        Event event = query.get();
        Event result = new AddEventCommand(this.parent, event).execute().get();

        assertEquals(event, result);
        assertNotEquals(event.getId(), result.getId());

        Habit habit = this.habitRepository.retrieveHabit(this.parent.getId()).get();
        assertNotNull(habit);
        assertEquals(2, habit.getEvents().size());
        assertTrue(habit.getEvents().contains(event.getId()));
        assertTrue(habit.getEvents().contains(result.getId()));
    }

    @Test(expected = ExecutionException.class)
    public void addEventToLocalHabit() throws ExecutionException, InterruptedException {
        Habit habit = new Habit();
        new AddEventCommand(habit, new Event()).execute().get();
    }
}
