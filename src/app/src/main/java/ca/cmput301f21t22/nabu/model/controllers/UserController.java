package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

/**
 * Add/Delete/Update user data to userbase
 *
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

    @NonNull
    public static UserController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserController();
        }

        return INSTANCE;
    }

    /**
     * Add habit and User ID in habit
     * @param userId -> Current user ID
     * @param habitId -> habit object ID
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
     * Delete habit and user ID in habit
     * @param userId -> Current User unique ID
     * @param habitId -> habit object ID
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
