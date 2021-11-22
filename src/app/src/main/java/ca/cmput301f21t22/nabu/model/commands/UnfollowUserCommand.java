package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

/**
 * Used to call on controllers to unfollow a user.
 */
public class UnfollowUserCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User activeUser;
    @NonNull
    private final User followedUser;

    /**
     * Create an instance of UnfollowUserCommand.
     *
     * @param activeUser   The user who is unfollowing some other user. This must be a remote-linked instance.
     * @param followedUser The user that should be unfollowed. This must be a remote-linked instance.
     */
    public UnfollowUserCommand(@NonNull User activeUser, @NonNull User followedUser) {
        this.userController = UserController.getInstance();

        this.activeUser = activeUser;
        this.followedUser = followedUser;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.deleteFollowing(this.activeUser.getId(), this.followedUser.getId())
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
