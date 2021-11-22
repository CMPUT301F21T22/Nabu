package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

public class UnfollowUserCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User activeUser;
    @NonNull
    private final User followedUser;

    public UnfollowUserCommand(@NonNull User activeUser, @NonNull User followedUser) {
        this.userController = UserController.getInstance();

        this.activeUser = activeUser;
        this.followedUser = followedUser;
    }

    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.deleteFollowing(this.activeUser.getId(), this.followedUser.getId())
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
