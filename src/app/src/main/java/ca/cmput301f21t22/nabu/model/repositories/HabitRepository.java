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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;

/**
 * Retrieves habit data from database
 * Deposit habit data within proper event lists
 * Ensure consistency between database & local data
 * Inform listening objects of changes to the data
 */

public class HabitRepository {
    @NonNull
    public final static String TAG = "HabitRepository";

    @Nullable
    private static HabitRepository INSTANCE;

    @NonNull
    private final Map<String, Habit> habitsMap;
    @NonNull
    private final MutableLiveData<Map<String, Habit>> habits;

    @NonNull
    private final CollectionReference habitsCollection;

    private HabitRepository() {
        this.habitsMap = new HashMap<>();
        this.habits = new MutableLiveData<>();

        this.habitsCollection = FirebaseFirestore.getInstance().collection("Habits");
        this.habitsCollection.addSnapshotListener(this::onHabitsChanged);
    }

    @NonNull
    public static HabitRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HabitRepository();
        }

        return INSTANCE;
    }

    @NonNull
    private static Habit createFromSnapshot(@NonNull DocumentSnapshot snapshot) {
        String title = snapshot.getString("title");
        String reason = snapshot.getString("reason");
        Date startDate = snapshot.getDate("startDate");
        Occurrence occurrence = snapshot.get("occurrence", Occurrence.class);
        @SuppressWarnings("unchecked") List<String> events = (List<String>) snapshot.get("events");
        Boolean shared = snapshot.getBoolean("shared");

        if (title == null || reason == null || startDate == null || occurrence == null || events == null ||
            shared == null) {
            throw new IllegalArgumentException();
        }

        return new Habit(snapshot.getId(), title, reason, startDate, occurrence, events, shared);
    }

    @NonNull
    public LiveData<Map<String, Habit>> getHabits() {
        return this.habits;
    }

    @NonNull
    public Optional<Habit> findHabit(Predicate<Habit> predicate) {
        return this.habitsMap.values().stream().filter(predicate).findFirst();
    }

    @NonNull
    public CompletableFuture<Habit> retrieveHabit(@NonNull String id) {
        CompletableFuture<Habit> future = new CompletableFuture<>();
        this.habitsCollection.document(id).get().addOnSuccessListener(snapshot -> {
            try {
                future.complete(createFromSnapshot(snapshot));
            } catch (IllegalArgumentException e) {
                future.complete(null);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to retrieve habit.", e);
            future.complete(null);
        });
        return future;
    }

    private void onHabitsChanged(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
        if (e != null || snapshots == null) {
            Log.e(TAG, "Failed to listen to collection updates.", e);
            return;
        }

        for (DocumentChange change : snapshots.getDocumentChanges()) {
            QueryDocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) {
                case ADDED:
                case MODIFIED:
                    Habit habit = createFromSnapshot(snapshot);
                    this.habitsMap.put(snapshot.getId(), habit);
                    this.habits.setValue(this.habitsMap);
                    break;
                case REMOVED:
                    this.habitsMap.remove(snapshot.getId());
                    this.habits.setValue(this.habitsMap);
                    break;
            }
        }
    }
}
