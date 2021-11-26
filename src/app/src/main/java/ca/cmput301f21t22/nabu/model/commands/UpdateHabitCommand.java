package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

/**
 * Command providing a way to update an existing habit.
 */
public class UpdateHabitCommand implements Command<CompletableFuture<Habit>> {
    @NonNull
    private final HabitRepository habitRepository;

    @NonNull
    private final HabitController habitController;

    @NonNull
    private final Habit habit;

    /**
     * Create an instance of UpdateHabitCommand.
     *
     * @param habit The remote-linked instance of habit that should be updated, whose non-ID fields have been changed to
     *              the new desired values.
     */
    public UpdateHabitCommand(@NonNull Habit habit) {
        this.habitRepository = HabitRepository.getInstance();

        this.habitController = HabitController.getInstance();

        this.habit = habit;
    }

    /**
     * Execute the command.
     *
     * @return A future for the habit as updated.
     */
    @Override
    public CompletableFuture<Habit> execute() {
        return this.habitController.update(this.habit.getId(), this.habit)
                .thenCompose(this.habitRepository::retrieveHabit);
    }
}
