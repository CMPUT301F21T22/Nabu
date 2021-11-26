package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.commands.AcceptRequestCommand;
import ca.cmput301f21t22.nabu.model.commands.RejectRequestCommand;
import ca.cmput301f21t22.nabu.model.controllers.UserController;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class RejectRequestCommandTest extends AuthenticatedFirestoreTest{
    private User user1;
    private User user2;
    private UserRepository userRepository;

    @Override
    public void setUp() {
        super.setUp();
        this.userRepository = UserRepository.getInstance();

        // Setup test situation.
        UserController userController = UserController.getInstance();
        try {
            this.createUser("test1@example.com", "password1");
            this.createUser("test2@example.com", "password2");

            await().until(() -> {
                Map<String, User> users = this.userRepository.getUsers().getValue();
                if (users != null) {
                    return users.size() >= 2;
                }
                return false;
            });

            Optional<User> u1 = this.userRepository.findUser(u -> u.getEmail().equals("test1@example.com"));
            assertTrue(u1.isPresent());
            Optional<User> u2 = this.userRepository.findUser(u -> u.getEmail().equals("test2@example.com"));
            assertTrue(u2.isPresent());

            this.user1 = u1.get();
            this.user2 = u2.get();

            userController.addRequest(this.user1.getId(), this.user2.getId()).get();
        } catch (Exception e) {
            assertNull(e);
        }
    }

    @Test
    public void RejectRequest() throws ExecutionException, InterruptedException {
        assertTrue(new RejectRequestCommand(this.user1, this.user2).execute().get());

        User u1 = this.userRepository.retrieveUser(this.user1.getId()).get();
        User u2 = this.userRepository.retrieveUser(this.user2.getId()).get();

        assertEquals(new ArrayList<>(), u1.getRequests());
        assertEquals(new ArrayList<>(), u1.getFollowing());

        assertEquals(new ArrayList<>(), u2.getRequests());
        assertFalse(u2.getFollowing().contains(u1.getId()));
    }
}

