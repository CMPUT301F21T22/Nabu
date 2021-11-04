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
 * Used to call on controllers to add an event to the database of habit events
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

    public AddEventCommand(@NonNull Habit habit, @NonNull Event event) {
        this.eventRepository = EventRepository.getInstance();

        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();

        this.habit = habit;
        this.event = event;
    }

    @Override
    public CompletableFuture<Event> execute() {
        return this.eventController.add(this.event)
                .thenCompose(eventId -> this.habitController.addEvent(this.habit.getId(), eventId)
                        .thenCompose(habitId -> CompletableFuture.supplyAsync(() -> eventId)))
                .thenCompose(this.eventRepository::retrieveEvent);
    }
}
