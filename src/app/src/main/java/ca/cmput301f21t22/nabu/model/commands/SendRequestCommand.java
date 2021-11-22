package ca.cmput301f21t22.nabu.model.commands;

import androidx.annotation.NonNull;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;
import ca.cmput301f21t22.nabu.model.controllers.UserController;

public class SendRequestCommand implements Command<CompletableFuture<Boolean>> {
    @NonNull
    private final UserController userController;

    @NonNull
    private final User fromUser;
    @NonNull
    private final User toUser;

    public SendRequestCommand(@NonNull User fromUser, @NonNull User toUser) {
        this.userController = UserController.getInstance();

        this.fromUser = fromUser;
        this.toUser = toUser;
    }

    @Override
    public CompletableFuture<Boolean> execute() {
        return this.userController.addRequest(this.toUser.getId(), this.fromUser.getId())
                .thenCompose(sender -> CompletableFuture.supplyAsync(() -> true));
    }
}
