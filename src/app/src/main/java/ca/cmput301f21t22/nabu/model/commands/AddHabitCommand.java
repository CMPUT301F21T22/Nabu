package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

public class AddHabitCommand implements Command<CompletableFuture<Habit>> {
    @NonNull
    private final HabitRepository habitRepository;

    @NonNull
    private final HabitController habitController;
    @NonNull
    private final UserController userController;

    @NonNull
    private final User user;
    @NonNull
    private final Habit habit;

    public AddHabitCommand(@NonNull User user, @NonNull Habit habit) {
        this.habitRepository = HabitRepository.getInstance();

        this.habitController = HabitController.getInstance();
        this.userController = UserController.getInstance();

        this.user = user;
        this.habit = habit;
    }

    @Override
    public CompletableFuture<Habit> execute() {
        return this.habitController.add(this.habit)
                .thenCompose(habitId -> this.userController.addHabit(this.user.getId(), habitId)
                        .thenCompose(userId -> CompletableFuture.supplyAsync(() -> habitId)))
                .thenCompose(this.habitRepository::retrieveHabit);
    }
}
