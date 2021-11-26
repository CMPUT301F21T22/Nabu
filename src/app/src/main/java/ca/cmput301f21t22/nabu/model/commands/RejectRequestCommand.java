package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

/**
 * Command providing a way to reject an incoming follow request from the context of a receiving user.
 */
public class RejectRequestCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User receiver;
    @NonNull
    private final User sender;

    /**
     * Create an instance of RejectRequestCommand.
     *
     * @param receiver The user that has received a follow request. This must be a remote-linked instance.
     * @param sender   The user that sent the follow request. This must be a remote-linked instance.
     */
    public RejectRequestCommand(@NonNull User receiver, @NonNull User sender) {
        this.userController = UserController.getInstance();

        this.receiver = receiver;
        this.sender = sender;
    }

    /**
     * Execute the command.
     *
     * @return A future indicating whether the operation succeeded.
     */
    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.deleteRequest(this.receiver.getId(), this.sender.getId())
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
