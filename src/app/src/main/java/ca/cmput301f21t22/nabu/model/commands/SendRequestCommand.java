package ca.cmput301f21t22.nabu.model.commands;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.Command;

public class SendRequestCommand implements Command<CompletableFuture<Boolean>> {

    public SendRequestCommand(User fromUser, User toUser) {

    }

    @Override
    public CompletableFuture<Boolean> execute() {
        return null;
    }
}
