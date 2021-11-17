package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;

/**
 * Singleton providing methods for write operations on remote habit instances, including addition, deletion, and updates.
 */
public class HabitController {
    @NonNull
    public final static String TAG = "HabitController";

    @Nullable
    private static HabitController INSTANCE;

    @NonNull
    private final CollectionReference habitsCollection;

    private HabitController() {
        this.habitsCollection = FirebaseFirestore.getInstance().collection("Habits");
    }

    /**
     * @return Singleton instance of the HabitController.
     */
    @NonNull
    public static HabitController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitController();
        }

        return INSTANCE;
    }

    @NonNull
    private static Map<String, Object> createFromHabit(Habit habit) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", habit.getTitle());
        map.put("reason", habit.getReason());
        map.put("startDate", habit.getStartDate());
        map.put("occurrence", habit.getOccurrence());
        map.put("events", habit.getEvents());
        map.put("shared", habit.isShared());
        return map;
    }

    /**
     * Deletes a habit by ID from the database.
     *
     * @param habitId The ID of the habit to delete.
     * @return A future indicating whether the operation completed successfully.
     */
    @NonNull
    public CompletableFuture<Boolean> delete(@NonNull String habitId) {
        if (habitId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId).delete().addOnSuccessListener(task -> {
            Log.d(TAG, "Habit deleted with id: " + habitId);
            future.complete(true);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Failed to delete habit with id: " + habitId, e);
            future.complete(false);
        });
        return future;
    }

    /**
     * Update the data fields of a habit in the database.
     *
     * @param habitId The ID of the habit to update.
     * @param habit   An habit to read data from.
     * @return A future for the ID of the habit, once updated.
     */
    @NonNull
    public CompletableFuture<String> update(@NonNull String habitId, @NonNull Habit habit) {
        if (habitId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update(createFromHabit(habit))
                .addOnCompleteListener(unused -> future.complete(habitId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Habit with id: " + habitId + " updated.");
                })
                .addOnFailureListener(unused -> {
                    Log.d(TAG, "Failed to update habit with id: " + habitId);
                });
        return future;
    }

    /**
     * Add an event reference to the list of events owned by a habit.
     *
     * @param habitId The ID of the habit to update.
     * @param eventId The ID of the event to associate with the habit.
     * @return A future for the ID of the habit, once updated.
     */
    @NonNull
    public CompletableFuture<String> addEvent(@NonNull String habitId, @NonNull String eventId) {
        if (habitId.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update("events", FieldValue.arrayUnion(eventId))
                .addOnCompleteListener(unused -> future.complete(habitId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Event with id: " + eventId + " added to habit with id: " + habitId);
                })
                .addOnFailureListener(unused -> {
                    Log.w(TAG, "Failed to add event with id: " + eventId + " to habit with id: " + habitId);
                });
        return future;
    }

    /**
     * Deletes an event reference from the list of events owned by a habit.
     *
     * @param habitId The ID of the habit to update.
     * @param eventId The ID of the event to remove from the habit.
     * @return A future for the ID of the habit, once updated.
     */
    @NonNull
    public CompletableFuture<String> deleteEvent(@NonNull String habitId, @NonNull String eventId) {
        if (habitId.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update("events", FieldValue.arrayRemove(eventId))
                .addOnCompleteListener(unused -> future.complete(habitId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Event with id: " + habitId + " removed from habit with id: " + eventId);
                })
                .addOnFailureListener(unused -> {
                    Log.w(TAG, "Failed to remove Event with id: " + habitId + " from habit with id: " + eventId);
                });
        return future;
    }

    /**
     * Remove all events from a user.
     *
     * @param habitId The ID of the habit to update.
     * @return A future for the ID of the habit, once updated.
     */
    @NonNull
    public CompletableFuture<String> clearEvents(@NonNull String habitId) {
        if (habitId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update("events", new ArrayList<>())
                .addOnCompleteListener(unused -> future.complete(habitId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Cleared events of habit with id: " + habitId);
                })
                .addOnFailureListener(unused -> {
                    Log.d(TAG, "Could not clear events of habit with id: " + habitId);
                });
        return future;
    }

    /**
     * Add a habit to the database.
     *
     * @param habit A habit to read data from.
     * @return A future for the ID of the newly added habit.
     */
    @NonNull
    public CompletableFuture<String> add(@NonNull Habit habit) {
        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.add(createFromHabit(habit)).addOnSuccessListener(doc -> {
            Log.d(TAG, "New habit added.");
            future.complete(doc.getId());
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to add new habit.", e);
            future.complete(null);
        });
        return future;
    }
}
