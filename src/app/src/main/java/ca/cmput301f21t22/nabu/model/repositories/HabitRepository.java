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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;

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
        String title = Objects.requireNonNull(snapshot.getString("title"));
        String reason = Objects.requireNonNull(snapshot.getString("reason"));
        Date startDate = Objects.requireNonNull(snapshot.getDate("startDate"));
        Occurrence occurrence = Objects.requireNonNull(snapshot.get("occurrence", Occurrence.class));
        @SuppressWarnings("unchecked") List<String> events =
                Objects.requireNonNull((List<String>) snapshot.get("events"));
        Boolean shared = Objects.requireNonNull(snapshot.getBoolean("shared"));
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
