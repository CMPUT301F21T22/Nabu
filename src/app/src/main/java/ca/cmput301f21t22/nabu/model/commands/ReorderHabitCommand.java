package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

/**
 * Command providing a way to change the order of a single habit in a given user.
 */
public class ReorderHabitCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User user;
    private final int originalPosition;
    private final int targetPosition;

    /**
     * Create an instance of ReorderHabitCommand.
     *
     * @param user             The user whose habits should be reordered.
     * @param originalPosition The current position of the habit that should be moved.
     * @param targetPosition   The intended position of the habit.
     */
    public ReorderHabitCommand(@NonNull User user, int originalPosition, int targetPosition) {
        this.userController = UserController.getInstance();

        this.user = user;
        this.originalPosition = originalPosition;
        this.targetPosition = targetPosition;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        List<String> habitIds = new ArrayList<>(this.user.getHabits());
        String id = habitIds.remove(this.originalPosition);
        habitIds.add(this.targetPosition, id);
        return this.userController.updateHabits(this.user.getId(), habitIds)
                .thenCompose(userId -> CompletableFuture.supplyAsync(() -> true));
    }
}
