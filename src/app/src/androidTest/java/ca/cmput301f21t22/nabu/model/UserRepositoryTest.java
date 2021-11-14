package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
    public void liveNewAccountSignIn() {
    }

    @Test
    public void liveExistingAccountSignIn() {
    }

    @Test
    public void liveSignOut() {
    }

    @Test
    public void liveUserUpdates() {
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
