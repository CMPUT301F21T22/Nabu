package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.EventController;
import ca.cmput301f21t22.nabu.model.controllers.HabitController;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

/**
 * Used to call on controllers to delete a habit from the database of habits, delete any events it owns, and remove any
 * references to it held by users.
 */
public class DeleteHabitCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final EventController eventController;
    @NonNull
    private final HabitController habitController;
    @NonNull
    private final UserController userController;

    @NonNull
    private final Habit habit;

    /**
     * Create an instance of DeleteHabitCommand.
     *
     * @param habit The habit to be deleted. This must be a remote-linked instance.
     * @see Habit
     */
    public DeleteHabitCommand(@NonNull Habit habit) {
        this.userRepository = UserRepository.getInstance();

        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();
        this.userController = UserController.getInstance();

        this.habit = habit;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        // TODO: Refactor to use HabitController.clearEvents

        // The user that owns this habit.
        Optional<User> parent = this.userRepository.findUser(user -> user.getHabits().contains(this.habit.getId()));
        // The children events of the habit.
        Stream<String> children = this.habit.getEvents().stream();

        // This future deletes all events owned by this habit.
        CompletableFuture<?>[] deleteChildrenFutures = children.map(
                eventId -> this.habitController.deleteEvent(this.habit.getId(), eventId)
                        .thenCompose(habitId -> this.eventController.delete(eventId)))
                .toArray(CompletableFuture[]::new);

        // This future first deletes child events, then the habit itself.
        CompletableFuture<Boolean> deleteFuture = CompletableFuture.allOf(deleteChildrenFutures)
                .thenCompose(v -> this.habitController.delete(this.habit.getId()));

        // If the parent exists, we first detach it from the parent, before deleting.
        if (parent.isPresent()) {
            return this.userController.deleteHabit(parent.get().getId(), this.habit.getId())
                    .thenCompose(userId -> deleteFuture);
        }

        return deleteFuture;
    }
}
