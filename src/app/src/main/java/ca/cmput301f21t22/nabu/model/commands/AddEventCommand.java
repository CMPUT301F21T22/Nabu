package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;

/**
 * Command providing a way to add an event to the database of habit events, and associate the new event with an
 * existing habit.
 */
public class AddEventCommand implements Command<CompletableFuture<Event>> {
    @NonNull
    private final EventRepository eventRepository;
    @NonNull
    private final EventController eventController;
    @NonNull
    private final HabitController habitController;

    @NonNull
    private final Habit habit;
    @NonNull
    private final Event event;

    /**
     * Create an instance of AddEventCommand.
     *
     * @param habit The habit the event should be added to.
     * @param event The event that user wants to add. This may be a local-only instance, as described in {@link Event#Event()}.
     */

    public AddEventCommand(@NonNull Habit habit, @NonNull Event event) {
        this.eventRepository = EventRepository.getInstance();
        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();

        this.habit = habit;
        this.event = event;
    }

    /**
     * Execute the command.
     *
     * @return A future for a remote-linked instance of the event as added.
     */
    @Override
    public CompletableFuture<Event> execute() {
        return this.eventController.add(this.event)
                .thenCompose(eventId -> this.habitController.addEvent(this.habit.getId(), eventId)
                        .thenCompose(habitId -> CompletableFuture.supplyAsync(() -> eventId)))
                .thenCompose(this.eventRepository::retrieveEvent);
    }
}
