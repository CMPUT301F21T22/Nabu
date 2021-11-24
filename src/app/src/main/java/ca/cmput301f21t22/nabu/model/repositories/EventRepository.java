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
import ca.cmput301f21t22.nabu.data.LatLngPoint;

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
     * @return Singleton instance of the EventRepository.
     */
    @NonNull
    public static EventRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventRepository();
        }

        return INSTANCE;
    }

    @NonNull
    private static Event createFromSnapshot(@NonNull DocumentSnapshot snapshot) {
        Date date = snapshot.getDate("date");
        String comment = snapshot.getString("comment");
        String photoPath = snapshot.getString("photoPath");
        GeoPoint gp = snapshot.getGeoPoint("location");

        if (date == null) {
            throw new IllegalArgumentException();
        }

        LatLngPoint location = null;
        if (gp != null) {
            location = new LatLngPoint(gp.getLatitude(), gp.getLongitude());
        }

        return new Event(snapshot.getId(), date, comment, photoPath, location);
    }

    /**
     * @return Handle to a live-updating copy of the events in the database.
     */
    @NonNull
    public LiveData<Map<String, Event>> getEvents() {
        return this.events;
    }

    /**
     * Retrieves an event from the database by ID.
     *
     * @param id The ID of the event to retrieve.
     * @return Future for the retrieved event.
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
