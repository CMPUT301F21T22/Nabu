package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Habit;

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

    private static void guard(Habit habit) {
    }

    public CompletableFuture<String> addEvent(@NonNull String habitId, @NonNull String eventId) {
        if (habitId.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update("events", FieldValue.arrayUnion(eventId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Event with id: " + eventId + " added to habit with id: " + habitId);
                    future.complete(habitId);
                });
        return future;
    }

    public CompletableFuture<String> deleteEvent(@NonNull String habitId, @NonNull String eventId) {
        if (habitId.equals("") || eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.habitsCollection.document(habitId)
                .update("events", FieldValue.arrayRemove(eventId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Event with id: " + habitId + " removed from habit with id: " + eventId);
                    future.complete(habitId);
                });
        return future;
    }
}
