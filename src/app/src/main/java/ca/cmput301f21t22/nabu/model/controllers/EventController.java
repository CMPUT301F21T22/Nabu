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

    @NonNull
    public CompletableFuture<String> update(@NonNull String eventId, @NonNull Event event) {
        if (eventId.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<String> future = new CompletableFuture<>();
        this.eventsCollection.document(eventId)
                .update(createFromEvent(event))
                .addOnCompleteListener(unused -> future.complete(eventId))
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "Event with id: " + eventId + " updated.");
                })
                .addOnFailureListener(unused -> {
                    Log.d(TAG, "Failed to update event with id: " + eventId);
                });
        return future;
    }

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
