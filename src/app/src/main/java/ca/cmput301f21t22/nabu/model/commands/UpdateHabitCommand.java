package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

/**
 * Used to call on controllers to update a habit to the database of user's habits
 */

public class UpdateHabitCommand implements Command<CompletableFuture<Habit>> {
    @NonNull
    private final HabitRepository habitRepository;

    @NonNull
    private final HabitController habitController;

    @NonNull
    private final Habit habit;

    /**
     * Update the habit to current user
     * @param user -> Current User
     * @param habit -> Habit that user wants to update
     */

    public UpdateHabitCommand(@NonNull Habit habit) {
        this.habitRepository = HabitRepository.getInstance();

        this.habitController = HabitController.getInstance();

        this.habit = habit;
    }

    @Override
    public CompletableFuture<Habit> execute() {
        return this.habitController.update(this.habit.getId(), this.habit)
                .thenCompose(this.habitRepository::retrieveHabit);
    }
}
