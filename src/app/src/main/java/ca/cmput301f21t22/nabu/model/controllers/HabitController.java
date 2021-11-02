package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

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

    @NonNull
    public static HabitController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitController();
        }

        return INSTANCE;
    }

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
}
