package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;

public class AddEventCommand implements Command {
    @NonNull
    private final EventController eventController;
    @NonNull
    private final HabitController habitController;

    @NonNull
    private final Habit habit;
    @NonNull
    private final Event event;

    public AddEventCommand(@NonNull Habit habit, @NonNull Event event) {
        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();

        this.habit = habit;
        this.event = event;
    }

    @Override
    public void execute() {
        this.eventController.add(this.event)
                .thenCompose(eventId -> this.habitController.addEvent(this.habit.getId(), eventId));
    }
}
