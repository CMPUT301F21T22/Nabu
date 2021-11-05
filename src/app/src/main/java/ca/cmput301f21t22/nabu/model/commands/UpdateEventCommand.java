package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;

/**
 * Used to call on controllers to update an event to the database of habit's events
 */

public class UpdateEventCommand implements Command<CompletableFuture<Event>> {
    @NonNull
    private final EventRepository eventRepository;

    @NonNull
    private final EventController eventController;

    @NonNull
    private final Event event;

    public UpdateEventCommand(@NonNull Event event) {
        this.eventRepository = EventRepository.getInstance();

        this.eventController = EventController.getInstance();

        this.event = event;
    }

    @Override
    public CompletableFuture<Event> execute() {
        return this.eventController.update(this.event.getId(), this.event)
                .thenCompose(this.eventRepository::retrieveEvent);
    }
}
