package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HabitCollection extends LiveCollection<Habit> {
    @Nullable
    private static HabitCollection INSTANCE;

    protected HabitCollection(@NonNull Class<Habit> cls, @NonNull CollectionReference ref) {
        super(cls, ref);
    }

    @NonNull
    public synchronized static HabitCollection getInstance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (INSTANCE == null) {
            INSTANCE = new HabitCollection(Habit.class, db.collection("Habits"));
        }

        return INSTANCE;
    }
}
