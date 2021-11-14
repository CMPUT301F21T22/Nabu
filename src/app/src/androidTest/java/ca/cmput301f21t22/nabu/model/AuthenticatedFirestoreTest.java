package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import ca.cmput301f21t22.nabu.data.User;
import ca.cmput301f21t22.nabu.model.repositories.UserRepository;

public abstract class AuthenticatedFirestoreTest {
    protected FirebaseFirestore db;
    protected FirebaseAuth auth;

    @BeforeClass
    public static void setUpClass() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.useEmulator("10.0.2.2", 8080);
        FirebaseFirestoreSettings settings =
                new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build();
        db.setFirestoreSettings(settings);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.useEmulator("10.0.2.2", 9099);
    }

    @Before
    public void setUp() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    @After
    public void tearDown() throws IOException {
        new OkHttpClient().newCall(new Request.Builder().url(
                "http://10.0.2.2:8080/emulator/v1/projects/nabu-94833/databases/(default)/documents").delete().build())
                .execute();

        this.auth.signOut();

        new OkHttpClient().newCall(
                new Request.Builder().url("http://10.0.2.2:9099/emulator/v1/projects/nabu-94833/accounts")
                        .delete()
                        .build()).execute();
    }

    protected void createUser(String email, String password) throws ExecutionException, InterruptedException {
        CompletableFuture<FirebaseUser> createFuture = new CompletableFuture<>();
        this.auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                createFuture.complete(this.auth.getCurrentUser());
            } else {
                assertNull(task.getException());
            }
        });

        UserRepository repository = UserRepository.getInstance();
        FirebaseUser fbUser = createFuture.get();
        await().until(() -> {
            User user = repository.getCurrentUser().getValue();
            Map<String, User> users = repository.getUsers().getValue();
            if (user != null && users != null) {
                return Objects.equals(user.getId(), fbUser.getUid()) && users.containsKey(fbUser.getUid());
            }
            return false;
        });
    }

    protected void createMockUser() throws ExecutionException, InterruptedException {
        this.createUser("test1@example.com", "password1");
    }
}
