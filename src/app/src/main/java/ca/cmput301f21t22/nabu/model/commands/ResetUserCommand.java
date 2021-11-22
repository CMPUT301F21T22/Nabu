package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.HabitRepository;

/**
 * Used to call on controllers to reset all habit and social information attached to a user.
 */
public class ResetUserCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final EventController eventController;
    @NonNull
    private final HabitController habitController;
    @NonNull
    private final UserController userController;

    @NonNull
    private final HabitRepository habitRepository;

    @NonNull
    private final User user;

    /**
     * Create an instance of ResetUserCommand.
     *
     * @param user The user that should be reset. This must be a remote-linked instance.
     * @see User
     */
    public ResetUserCommand(@NonNull User user) {
        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();
        this.userController = UserController.getInstance();

        this.habitRepository = HabitRepository.getInstance();

        this.user = user;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        List<String> habitIds = this.user.getHabits();

        return this.userController.clearFollowing(this.user.getId())
                .thenCompose(this.userController::clearRequests)
                .thenCompose(this.userController::clearHabits)
                .thenCompose(userId -> CompletableFuture.allOf(habitIds.stream().map(habitId -> {
                    Optional<Habit> habit = this.habitRepository.findHabit(h -> h.getId().equals(habitId));
                    List<String> eventIds = habit.map(Habit::getEvents).orElseGet(ArrayList::new);
                    return this.habitController.delete(habitId)
                            .thenCompose(hId -> CompletableFuture.allOf(eventIds.stream()
                                                                                .map(this.eventController::delete)
                                                                                .toArray(CompletableFuture[]::new)));
                }).toArray(CompletableFuture[]::new)))
                .thenCompose(v -> CompletableFuture.supplyAsync(() -> true));
    }
}
