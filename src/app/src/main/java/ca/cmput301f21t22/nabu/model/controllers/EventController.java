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
    public CompletableFuture<Boolean> delete(@NonNull String id) {
        if (id.equals("")) {
            throw new IllegalArgumentException();
        }

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        this.eventsCollection.document(id).delete().addOnCompleteListener(task -> {
            Log.d(TAG, "Event deleted with id: " + id);
            future.complete(true);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Failed to delete event with id: " + id, e);
            future.complete(false);
        });
        return future;
    }

    @NonNull
    public CompletableFuture<String> add(@NonNull Event event) {
        CompletableFuture<String> future = new CompletableFuture<>();
        Map<String, Object> map = new HashMap<>();
        map.put("date", new Timestamp(event.getDate()));
        map.put("comment", event.getComment());
        map.put("photoPath", event.getPhotoPath());
        map.put("location", event.getLocation());
        this.eventsCollection.add(map).addOnSuccessListener(doc -> {
            Log.d(TAG, "New event added.");
            future.complete(doc.getId());
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to add new event.", e);
            future.complete(null);
        });
        return future;
    }
}
