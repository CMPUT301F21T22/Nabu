package ca.cmput301f21t22.nabu.model.controllers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;

/**
 * Singleton providing methods for write operations on remote event instances, including addition, deletion, and updates.
 */
public class EventController {
    @NonNull
    public final static String TAG = "EventController";

    @Nullable
    private static EventController INSTANCE;

    @NonNull
    private final CollectionReference eventsCollection;

    private EventController() {
        this.eventsCollection = FirebaseFirestore.getInstance().collection("Events");
    }

    /**
     * @return Singleton instance of the EventController.
     */
    @NonNull
    public static EventController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventController();
        }

        return INSTANCE;
    }

    @NonNull
    private static Map<String, Object> createFromEvent(Event event) {
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(event.getDate()));
        map.put("comment", event.getComment());
        map.put("photoPath", event.getPhotoPath());
        map.put("location", event.getLocation());
        return map;
    }

    /**
     * Deletes an event by ID from the database.
     *
     * @param eventId The ID of the event to delete.
     * @return A future indicating whether the operation completed successfully.
     */
    @NonNull
    public CompletableFuture<Boolean> delete(@NonNull String eventId) {
        if (eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.eventsCollection.document(eventId).delete().addOnSuccessListener(task -> {
            Log.d(TAG, "Event deleted with id: " + eventId);
            future.complete(true);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Failed to delete event with id: " + eventId, e);
            future.complete(false);
        });
        return future;
    }

    /**
     * Update the data fields of an event in the database.
     *
     * @param eventId The ID of the event to update.
     * @param event   An event to read data from.
     * @return A future for the ID of the event, once updated.
     */
    @NonNull
    public CompletableFuture<String> update(@NonNull String eventId, @NonNull Event event) {
        if (eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.eventsCollection.document(eventId).update(createFromEvent(event)).addOnSuccessListener(unused -> {
            future.complete(eventId);
            Log.d(TAG, "Event with id: " + eventId + " updated.");
        }).addOnFailureListener(unused -> {
            future.complete(eventId);
            Log.d(TAG, "Failed to update event with id: " + eventId);
        });
        return future;
    }

    /**
     * Add an event to the database.
     *
     * @param event An event to read data from.
     * @return A future for the ID of the newly added event.
     */
    @NonNull
    public CompletableFuture<String> add(@NonNull Event event) {
        CompletableFuture<String> future = new CompletableFuture<>();
        this.eventsCollection.add(createFromEvent(event)).addOnSuccessListener(doc -> {
            Log.d(TAG, "New event added.");
            future.complete(doc.getId());
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to add new event.", e);
            future.complete(null);
        });
        return future;
    }
}
