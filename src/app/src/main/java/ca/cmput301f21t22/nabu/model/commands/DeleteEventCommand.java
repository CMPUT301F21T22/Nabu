package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Optional;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class DeleteEventCommand implements Command {
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
    public void execute() {
        Map<String, Habit> habitsMap = this.habitRepository.getHabits().getValue();
        if (habitsMap != null) {
            Optional<Habit> parent = habitsMap.values()
                    .stream()
                    .filter(habit -> habit.getEvents().contains(this.event.getId()))
                    .findFirst();
            if (parent.isPresent()) {
                this.habitController.deleteEvent(parent.get().getId(), this.event.getId());
            } else {
                this.eventController.delete(this.event.getId());
            }
        } else {
            this.eventController.delete(this.event.getId());
        }
    }
}
