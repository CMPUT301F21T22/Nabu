package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

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

    private UserController() {
        this.usersCollection = FirebaseFirestore.getInstance().collection("Users");
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
                .addOnCompleteListener(unused -> future.complete(userId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Habit with id: " + habitId + " added to user with id: " + userId);
                })
                .addOnFailureListener(unused -> {
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
                .addOnCompleteListener(unused -> future.complete(userId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Habit with id: " + habitId + " removed from user with id: " + userId);
                })
                .addOnFailureListener(unused -> {
                    Log.w(TAG, "Failed to remove habit with id: " + habitId + " from user with id: " + userId);
                });
        return future;
    }
}
