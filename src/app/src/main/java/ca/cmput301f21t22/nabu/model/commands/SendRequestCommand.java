package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

/**
 * Used to call on controllers to send a follow request to a user.
 */
public class SendRequestCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User fromUser;
    @NonNull
    private final User toUser;

    /**
     * Create an instance of SendRequestCommand.
     *
     * @param fromUser The user that sends the follow request. This must be a remote-linked instance.
     * @param toUser   The user that receives the follow request. This must be a remote-linked instance.
     */
    public SendRequestCommand(@NonNull User fromUser, @NonNull User toUser) {
        this.userController = UserController.getInstance();

        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.addRequest(this.toUser.getId(), this.fromUser.getId())
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
