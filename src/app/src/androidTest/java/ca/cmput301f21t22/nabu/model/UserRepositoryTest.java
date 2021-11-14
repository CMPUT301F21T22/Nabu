package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public class UserRepositoryTest extends AuthenticatedFirestoreTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private UserRepository repository;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.repository = UserRepository.getInstance();
        this.collection = this.db.collection("Users");
    }

    @Test
    public void findUserByEmail() throws ExecutionException, InterruptedException {
        this.createUser("test1@example.com", "password1");
        this.createUser("test2@example.com", "password2");
        this.createUser("test3@example.com", "password3");

        await().until(() -> {
            Map<String, User> users = this.repository.getUsers().getValue();
            if (users != null) {
                return users.size() >= 3;
            }
            return false;
        });

        Optional<User> user = this.repository.findUser(u -> u.getEmail().equals("test2@example.com"));
        assertTrue(user.isPresent());
        assertEquals("test2@example.com", user.get().getEmail());
        assertEquals(new ArrayList<>(), user.get().getHabits());
    }

    @Test
    public void liveNewAccountSignIn() throws ExecutionException, InterruptedException {
        AtomicBoolean usersComplete = new AtomicBoolean(false);
        Observer<Map<String, User>> usersObserver = users -> {
            if (users.size() >= 1) {
                Optional<User> query = users.values().stream().findAny();
                assertTrue(query.isPresent());
                User user = query.get();
                assertNotEquals("", user.getId());
                assertEquals("test3@example.com", user.getEmail());
                assertEquals(new ArrayList<>(), user.getHabits());

                usersComplete.set(true);
            }
        };

        AtomicBoolean currentUserComplete = new AtomicBoolean(false);
        Observer<User> currentUserObserver = user -> {
            if (user != null) {
                assertNotEquals("", user.getId());
                assertEquals("test3@example.com", user.getEmail());
                assertEquals(new ArrayList<>(), user.getHabits());

                currentUserComplete.set(true);
            }
        };

        this.repository.getUsers().observeForever(usersObserver);
        this.repository.getCurrentUser().observeForever(currentUserObserver);

        this.createUser("test3@example.com", "password3");

        await().until(usersComplete::get);
        await().until(currentUserComplete::get);
        this.repository.getUsers().removeObserver(usersObserver);
        this.repository.getCurrentUser().removeObserver(currentUserObserver);
    }

    @Test
    public void liveExistingAccountSignIn() throws ExecutionException, InterruptedException {
        this.createUser("test3@example.com", "password3");
        await().until(() -> this.repository.getCurrentUser().getValue() != null);
        this.auth.signOut();
        await().until(() -> this.repository.getCurrentUser().getValue() == null);

        AtomicBoolean usersComplete = new AtomicBoolean(false);
        Observer<Map<String, User>> usersObserver = users -> {
            if (users.size() >= 1) {
                Optional<User> query = users.values().stream().findAny();
                assertTrue(query.isPresent());
                User user = query.get();
                assertNotEquals("", user.getId());
                assertEquals("test3@example.com", user.getEmail());
                assertEquals(new ArrayList<>(), user.getHabits());

                usersComplete.set(true);
            }
        };

        AtomicBoolean currentUserComplete = new AtomicBoolean(false);
        Observer<User> currentUserObserver = user -> {
            if (user != null) {
                assertNotEquals("", user.getId());
                assertEquals("test3@example.com", user.getEmail());
                assertEquals(new ArrayList<>(), user.getHabits());

                currentUserComplete.set(true);
            }
        };

        this.repository.getUsers().observeForever(usersObserver);
        this.repository.getCurrentUser().observeForever(currentUserObserver);

        this.auth.signInWithEmailAndPassword("test3@example.com", "password3");

        await().until(usersComplete::get);
        await().until(currentUserComplete::get);
        this.repository.getUsers().removeObserver(usersObserver);
        this.repository.getCurrentUser().removeObserver(currentUserObserver);
    }

    @Test
    public void liveUserUpdates() throws ExecutionException, InterruptedException {
        this.createUser("test3@example.com", "password3");
        await().until(() -> this.repository.getCurrentUser().getValue() != null);

        AtomicBoolean usersComplete = new AtomicBoolean(false);
        AtomicBoolean usersReceived = new AtomicBoolean(false);
        Observer<Map<String, User>> usersObserver = users -> {
            if (users.size() >= 1 && !usersReceived.get()) {
                usersReceived.set(true);
            } else if (users.size() >= 1 && usersReceived.get()) {
                Optional<User> query = users.values().stream().findAny();
                assertTrue(query.isPresent());
                User user = query.get();

                assertEquals(Collections.singletonList("iTqYxu1N6IO72GNHFBtX"), user.getHabits());

                usersComplete.set(true);
            }
        };

        AtomicBoolean currentUserComplete = new AtomicBoolean(false);
        AtomicBoolean currentUserReceived = new AtomicBoolean(false);
        Observer<User> currentUserObserver = user -> {
            if (user != null && !currentUserReceived.get()) {
                currentUserReceived.set(true);
            } else if (user != null && currentUserReceived.get()) {
                assertEquals(Collections.singletonList("iTqYxu1N6IO72GNHFBtX"), user.getHabits());

                currentUserComplete.set(true);
            }
        };

        this.repository.getUsers().observeForever(usersObserver);
        this.repository.getCurrentUser().observeForever(currentUserObserver);

        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);

        this.collection.document(user.getUid()).update("habits", FieldValue.arrayUnion("iTqYxu1N6IO72GNHFBtX"));

        await().until(usersComplete::get);
        await().until(currentUserComplete::get);
        this.repository.getUsers().removeObserver(usersObserver);
        this.repository.getCurrentUser().removeObserver(currentUserObserver);
    }

    private void createUser(String email, String password) throws ExecutionException, InterruptedException {
        CompletableFuture<FirebaseUser> createFuture = new CompletableFuture<>();
        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                createFuture.complete(this.auth.getCurrentUser());
            } else {
                assertNull(task.getException());
            }
        });

        FirebaseUser fbUser = createFuture.get();
        await().until(() -> {
            User user = this.repository.getCurrentUser().getValue();
            Map<String, User> users = this.repository.getUsers().getValue();
            if (user != null && users != null) {
                return Objects.equals(user.getId(), fbUser.getUid()) && users.containsKey(fbUser.getUid());
            }
            return false;
        });
    }
}
