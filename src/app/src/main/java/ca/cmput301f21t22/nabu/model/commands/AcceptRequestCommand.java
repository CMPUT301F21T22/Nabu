package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

public class AcceptRequestCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User receiver;
    @NonNull
    private final User sender;

    public AcceptRequestCommand(@NonNull User receiver, @NonNull User sender) {
        this.userController = UserController.getInstance();

        this.receiver = receiver;
        this.sender = sender;
    }

    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.deleteRequest(this.receiver.getId(), this.sender.getId())
                .thenCompose(receiver -> this.userController.addFollowing(this.sender.getId(), receiver))
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
