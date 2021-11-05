package ca.cmput301f21t22.nabu.model.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import ca.cmput301f21t22.nabu.data.Event;

/**
 * Retrieves event data from database
 * Deposits event data within proper event lists
 * Ensures consistency between database & local data
 * Informs listening objects of changes to the data
 */

public class EventRepository {
    @NonNull
    public final static String TAG = "EventRepository";

    @Nullable
    private static EventRepository INSTANCE;

    @NonNull
    private final Map<String, Event> eventsMap;
    @NonNull
    private final MutableLiveData<Map<String, Event>> events;

    @NonNull
    private final CollectionReference eventsCollection;

    private EventRepository() {
        this.eventsMap = new HashMap<>();
        this.events = new MutableLiveData<>();

        this.eventsCollection = FirebaseFirestore.getInstance().collection("Events");
        this.eventsCollection.addSnapshotListener(this::onEventsChanged);
    }

    /**
     * getInstance from EventRepository
     * Used to return a single object instance and creates a new Instance if it's null
     * @return Controller/Repositroy object Instance
     */
    @NonNull
    public static EventRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventRepository();
        }

        return INSTANCE;
    }

    /**
     * Create user/habit/event from a snapshot as returned by Firestore
     * @param snapshot of current event data from snapshot by Firestore database
     * @return Event details
     */
    @NonNull
    private static Event createFromSnapshot(@NonNull DocumentSnapshot snapshot) {
        Date date = snapshot.getDate("date");
        String comment = snapshot.getString("comment");
        String photoPath = snapshot.getString("photoPath");
        GeoPoint location = snapshot.getGeoPoint("location");

        if (date == null) {
            throw new IllegalArgumentException();
        }

        return new Event(snapshot.getId(), date, comment, photoPath, location);
    }

    @NonNull
    public LiveData<Map<String, Event>> getEvents() {
        return this.events;
    }

    /**
     * Retrieves event from Firestore database
     * @param id current event snapshot document ID
     * @return Retrieved event where the futures are asynchronous
     */
    @NonNull
    public CompletableFuture<Event> retrieveEvent(@NonNull String id) {
        CompletableFuture<Event> future = new CompletableFuture<>();
        this.eventsCollection.document(id).get().addOnSuccessListener(snapshot -> {
            try {
                future.complete(createFromSnapshot(snapshot));
            } catch (IllegalArgumentException e) {
                future.complete(null);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to retrieve event.", e);
            future.complete(null);
        });
        return future;
    }

    /**
     * Checks whether an event has been changed and update changing to event hashmap
     * @param snapshots Zero or more DocumentSnapshot for current event
     * @param e A class of exceptions thrown by Cloud Firestore
     */
    private void onEventsChanged(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null || snapshots == null) {
            Log.e(TAG, "Failed to listen to collection updates.", e);
            return;
        }

        for (DocumentChange change : snapshots.getDocumentChanges()) {
            QueryDocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) {
                case ADDED:
                case MODIFIED:
                    Event event = createFromSnapshot(snapshot);
                    this.eventsMap.put(snapshot.getId(), event);
                    this.events.setValue(this.eventsMap);
                    break;
                case REMOVED:
                    this.eventsMap.remove(snapshot.getId());
                    this.events.setValue(this.eventsMap);
                    break;
            }
        }
    }
}
