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
        map.put("following", user.getFollowing());
        map.put("requests", user.getRequests());
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

    /**
     * Add a user reference to the list of users a user is following.
     *
     * @param userId         The ID of the user to update.
     * @param followTargetId The ID of the user being followed.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> addFollowing(@NonNull String userId, @NonNull String followTargetId) {
        if (userId.equals("") || followTargetId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("following", FieldValue.arrayUnion(followTargetId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Follow target: " + followTargetId + " added to user: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Failed to add follow target: " + followTargetId + " to user: " + userId);
                });
        return future;
    }

    /**
     * Delete a user reference from the list of users a user is following.
     *
     * @param userId         The ID of the user to update.
     * @param followTargetId The ID of the user to unfollow.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> deleteFollowing(@NonNull String userId, @NonNull String followTargetId) {
        if (userId.equals("") || followTargetId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("following", FieldValue.arrayRemove(followTargetId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Follow target: " + followTargetId + " removed from user: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Failed to remove follow target: " + followTargetId + " from user: " + userId);
                });
        return future;
    }

    /**
     * Remove all references a user is following
     *
     * @param userId The ID of the user to update.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> clearFollowing(@NonNull String userId) {
        if (userId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId).update("following", new ArrayList<>()).addOnSuccessListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Cleared follow targets from user: " + userId);
        }).addOnFailureListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Failed to clear follow targets from user: " + userId);
        });
        return future;
    }

    /**
     * Add a request to the list of incoming follow requests of a user.
     *
     * @param userId      The ID of the user to update.
     * @param requesterId The ID of the user from whom the request comes.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> addRequest(@NonNull String userId, @NonNull String requesterId) {
        if (userId.equals("") || requesterId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("requests", FieldValue.arrayUnion(requesterId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Request from: " + requesterId + " added to user: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Failed to add request from: " + requesterId + " to user: " + userId);
                });
        return future;
    }

    /**
     * Remove a request from the list of incoming follow requests of a user.
     *
     * @param userId      The ID of the user to update.
     * @param requesterId The ID of the user whose request should be deleted.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> deleteRequest(@NonNull String userId, @NonNull String requesterId) {
        if (userId.equals("") || requesterId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId)
                .update("requests", FieldValue.arrayRemove(requesterId))
                .addOnSuccessListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Request from: " + requesterId + " removed from user: " + userId);
                })
                .addOnFailureListener(unused -> {
                    future.complete(userId);
                    Log.d(TAG, "Failed to remove request from: " + requesterId + " from user: " + userId);
                });
        return future;
    }

    /**
     * Remove all incoming follow requests of a user.
     *
     * @param userId The ID of the user to update.
     * @return A future for the ID of the user, once updated.
     */
    public CompletableFuture<String> clearRequests(@NonNull String userId) {
        if (userId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.usersCollection.document(userId).update("requests", new ArrayList<>()).addOnSuccessListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Requests cleared to user: " + userId);
        }).addOnFailureListener(unused -> {
            future.complete(userId);
            Log.d(TAG, "Failed to clear requests to user: " + userId);
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
