package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.User;

/**
 * Singleton providing methods for write operations on remote user instances.
 */
public class UserController {
    @NonNull
    public final static String TAG = "UserController";

    @Nullable
    private static UserController INSTANCE;

    @NonNull
    private final CollectionReference usersCollection;
    @NonNull
    private final FirebaseAuth auth;

    private UserController() {
        this.usersCollection = FirebaseFirestore.getInstance().collection("Users");

        this.auth = FirebaseAuth.getInstance();
        this.auth.addAuthStateListener(this::onSignInChanged);
    }

    /**
     * @return Singleton instance of the UserController.
     */
    @NonNull
    public static UserController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserController();
        }

        return INSTANCE;
    }

    @NonNull
    private static Map<String, Object> createFromUser(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("habits", user.getHabits());
        return map;
    }

    /**
     * Add a habit reference to the list of habits owned by a user.
     *
     * @param userId  The ID of the user to update.
     * @param habitId The ID of the habit to associate with the user.
     * @return A future for the ID of the user, once updated.
     */
    @NonNull
    public CompletableFuture<String> addHabit(@NonNull String userId, @NonNull String habitId) {
        if (userId.equals("") || habitId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("habits", FieldValue.arrayUnion(habitId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Habit with id: " + habitId + " added to user with id: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.w(TAG, "Failed to add habit with id: " + habitId + " to user with id: " + userId);
                });
        return future;
    }

    /**
     * Delete a habit reference from the list of habits owned by a user.
     *
     * @param userId  The ID of the user to update.
     * @param habitId The ID of the habit to remove from the user.
     * @return A future for the ID of the user, once updated.
     */
    @NonNull
    public CompletableFuture<String> deleteHabit(@NonNull String userId, @NonNull String habitId) {
        if (userId.equals("") || habitId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("habits", FieldValue.arrayRemove(habitId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Habit with id: " + habitId + " removed from user with id: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.w(TAG, "Failed to remove habit with id: " + habitId + " from user with id: " + userId);
                });
        return future;
    }

    /**
     * Removes all habits from a user.
     *
     * @param userId The ID of the user to update.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> clearHabits(@NonNull String userId) {
        if (userId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId).update("habits", new ArrayList<>()).addOnSuccessListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Cleared habits of user with id: " + userId);
        }).addOnFailureListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Could not clear habits of user with id: " + userId);
        });
        return future;
    }

    /**
     * Updates the entire list of habits attached to a user.
     *
     * @param userId      The ID of the user to update.
     * @param newHabitIds The list of habits that should be attached to the user.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> updateHabits(@NonNull String userId, @NonNull List<String> newHabitIds) {
        if (userId.equals("") || newHabitIds.contains("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("habits", new ArrayList<>(newHabitIds))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Updated habits of user with id: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Could not update habits of user with id: " + userId);
                });
        return future;
    }

    private void onSignInChanged(FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            return;
        }

        this.usersCollection.document(user.getUid()).get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) {
                snapshot.getReference()
                        .set(createFromUser(
                                new User(user.getUid(), Objects.requireNonNull(user.getEmail()), new ArrayList<>())));
            }
        }).addOnFailureListener(e -> Log.e(TAG, "Could not retrieve currently logged in user from collection."));
    }
}
