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

    public DeleteHabitCommand(@NonNull Habit habit) {
        this.userRepository = UserRepository.getInstance();

        this.eventController = EventController.getInstance();
        this.habitController = HabitController.getInstance();
        this.userController = UserController.getInstance();

        this.habit = habit;
    }

    @Override
    public CompletableFuture<Boolean> execute() {
        Optional<User> parent = this.userRepository.findUser(user -> user.getHabits().contains(this.habit.getId()));
        Stream<String> children = this.habit.getEvents().stream();

        CompletableFuture<?>[] deleteChildrenFutures = children.map(
                eventId -> this.habitController.deleteEvent(this.habit.getId(), eventId)
                        .thenCompose(habitId -> this.eventController.delete(eventId)))
                .toArray(CompletableFuture[]::new);

        CompletableFuture<Boolean> deleteFuture = CompletableFuture.allOf(deleteChildrenFutures)
                .thenCompose(v -> this.habitController.delete(this.habit.getId()));

        if (parent.isPresent()) {
            return this.userController.deleteHabit(parent.get().getId(), this.habit.getId())
                    .thenCompose(userId -> deleteFuture);
        }

        return deleteFuture;
    }
}
