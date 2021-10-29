package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventCollection extends LiveCollection<Event> {
    @Nullable
    private static EventCollection INSTANCE;

    protected EventCollection(@NonNull Class<Event> cls, @NonNull CollectionReference ref) {
        super(cls, ref);
    }

    @NonNull
    public synchronized static EventCollection getInstance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (INSTANCE == null) {
            INSTANCE = new EventCollection(Event.class, db.collection("Events"));
        }

        return INSTANCE;
    }
}
