package ca.cmput301f21t22.nabu.model;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import ca.cmput301f21t22.nabu.model.controllers.UserController;

public class UserControllerTest extends AuthenticatedFirestoreTest {
    private UserController controller;
    private CollectionReference collection;

    @Override
    public void setUp() {
        super.setUp();
        this.controller = UserController.getInstance();
        this.collection = this.db.collection("Users");
    }

    @Test
    public void addHabitToUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();

        userId = this.controller.addHabit(userId, "UiSfaFKK7WxX6HOAShoR").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> habits = (List<String>) document.get("habits");
            assertNotNull(habits);
            assertTrue(habits.contains("UiSfaFKK7WxX6HOAShoR"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void deleteHabitFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addHabit(userId, "UiSfaFKK7WxX6HOAShoR").get();

        userId = this.controller.deleteHabit(userId, "UiSfaFKK7WxX6HOAShoR").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> habits = (List<String>) document.get("habits");
            assertNotNull(habits);
            assertFalse(habits.contains("UiSfaFKK7WxX6HOAShoR"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void clearHabitsFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addHabit(userId, "habit1").get();
        userId = this.controller.addHabit(userId, "habit2").get();
        userId = this.controller.addHabit(userId, "habit3").get();
        userId = this.controller.addHabit(userId, "habit4").get();

        userId = this.controller.clearHabits(userId).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> habits = (List<String>) document.get("habits");
            assertNotNull(habits);
            assertEquals(new ArrayList<>(), habits);

            complete.set(true);
        });
        await().until(complete::get);
    }
}
