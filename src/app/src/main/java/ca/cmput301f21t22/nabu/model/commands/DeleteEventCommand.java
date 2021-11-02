package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class DeleteEventCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final HabitRepository habitRepository;

    @NonNull
    private final EventController eventController;
    @NonNull
    private final HabitController habitController;

    @NonNull
    private final Event event;

    public DeleteEventCommand(@NonNull Event event) {
        this.habitRepository = HabitRepository.getInstance();

        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();

        this.event = event;
    }

    @Override
    public CompletableFuture<Boolean> execute() {
        Optional<Habit> parent =
                this.habitRepository.findHabit(habit -> habit.getEvents().contains(this.event.getId()));
        if (parent.isPresent()) {
            return this.habitController.deleteEvent(parent.get().getId(), this.event.getId())
                    .thenCompose(habitId -> this.eventController.delete(this.event.getId()));
        }
        return this.eventController.delete(this.event.getId());
    }
}
