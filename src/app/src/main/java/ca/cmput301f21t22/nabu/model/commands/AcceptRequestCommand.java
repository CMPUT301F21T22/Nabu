package ca.cmput301f21t22.nabu.model.commands;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;

public class AcceptRequestCommand implements Command<CompletableFuture<Boolean>> {

    public AcceptRequestCommand(User receiver, User sender) {

    }

    @Override
    public CompletableFuture<Boolean> execute() {
        return null;
    }
}
