package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.repositories.EventRepository;

/**
 * Command providing a way to update an existing event.
 */
public class UpdateEventCommand implements Command<CompletableFuture<Event>> {
    @NonNull
    private final EventRepository eventRepository;

    @NonNull
    private final EventController eventController;

    @NonNull
    private final Event event;

    /**
     * Create an instance of UpdateEventCommand.
     *
     * @param event The event to be updated. This must be a remote-linked instance, whose non-ID fields have been
     *              changed to the new values.
     */
    public UpdateEventCommand(@NonNull Event event) {
        this.eventRepository = EventRepository.getInstance();

        this.eventController = EventController.getInstance();

        this.event = event;
    }

    /**
     * Execute the command.
     *
     * @return A future for the event as updated.
     */
    @Override
    public CompletableFuture<Event> execute() {
        return this.eventController.update(this.event.getId(), this.event)
                .thenCompose(this.eventRepository::retrieveEvent);
    }
}
