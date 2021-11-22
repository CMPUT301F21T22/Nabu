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
import java.util.Arrays;
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

    @Test
    public void updateUserHabits() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();

        userId = this.controller.updateHabits(userId, Arrays.asList("habit1", "habit2", "habit3")).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> habits = (List<String>) document.get("habits");
            assertNotNull(habits);
            assertEquals(Arrays.asList("habit1", "habit2", "habit3"), habits);

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void addFollowingToUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();

        userId = this.controller.addFollowing(userId, "OtherUser").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> following = (List<String>) document.get("following");
            assertNotNull(following);
            assertTrue(following.contains("OtherUser"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void removeFollowingFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addFollowing(userId, "OtherUser").get();

        userId = this.controller.deleteFollowing(userId, "OtherUser").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> following = (List<String>) document.get("following");
            assertNotNull(following);
            assertFalse(following.contains("OtherUser"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void clearFollowingFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addFollowing(userId, "OtherUser1").get();
        userId = this.controller.addFollowing(userId, "OtherUser2").get();
        userId = this.controller.addFollowing(userId, "OtherUser3").get();

        userId = this.controller.clearFollowing(userId).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> following = (List<String>) document.get("following");
            assertNotNull(following);
            assertEquals(new ArrayList<>(), following);

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void addRequestToUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();

        userId = this.controller.addRequest(userId, "OtherUser").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> requests = (List<String>) document.get("requests");
            assertNotNull(requests);
            assertTrue(requests.contains("OtherUser"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void removeRequestFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addRequest(userId, "OtherUser").get();

        userId = this.controller.deleteRequest(userId, "OtherUser").get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> requests = (List<String>) document.get("requests");
            assertNotNull(requests);
            assertFalse(requests.contains("OtherUser"));

            complete.set(true);
        });
        await().until(complete::get);
    }

    @Test
    public void clearRequestsFromUser() throws ExecutionException, InterruptedException {
        this.createMockUser();
        FirebaseUser user = this.auth.getCurrentUser();
        assertNotNull(user);
        String userId = user.getUid();
        userId = this.controller.addRequest(userId, "OtherUser1").get();
        userId = this.controller.addRequest(userId, "OtherUser2").get();
        userId = this.controller.addRequest(userId, "OtherUser3").get();

        userId = this.controller.clearRequests(userId).get();

        AtomicBoolean complete = new AtomicBoolean(false);
        this.collection.document(userId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(document);

            //noinspection unchecked
            List<String> requests = (List<String>) document.get("requests");
            assertNotNull(requests);
            assertEquals(new ArrayList<>(), requests);

            complete.set(true);
        });
        await().until(complete::get);
    }
}
