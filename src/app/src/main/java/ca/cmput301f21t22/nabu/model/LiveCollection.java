package ca.cmput301f21t22.nabu.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public abstract class LiveCollection<T extends LiveDocument<?>>
        implements EventListener<QuerySnapshot>, LifetimeObservable {
    @NonNull
    public static String TAG = "LiveCollection";

    @NonNull
    protected final CollectionReference ref;

    private boolean alive;
    @NonNull
    private final Set<String> items;
    @NonNull
    private final HashSet<LifetimeChangeCallback> lifetimeCallbacks;
    @NonNull
    private final Class<T> cls;

    protected LiveCollection(@NonNull Class<T> cls, @NonNull CollectionReference ref) {
        this.alive = false;
        this.items = new HashSet<>();
        this.lifetimeCallbacks = new HashSet<>();

        this.cls = cls;

        this.ref = ref;
        this.ref.addSnapshotListener(this);
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
        if (error != null || snapshots == null) {
            Log.e(TAG, "(" + this.ref.getPath() + ") Error while listening for collection event.", error);
            this.setAlive(false);
        } else {
            Log.d(TAG, "(" + this.ref.getPath() + ") Updating collection.");
            for (DocumentChange change : snapshots.getDocumentChanges()) {
                QueryDocumentSnapshot doc = change.getDocument();
                if (change.getType() == DocumentChange.Type.ADDED || change.getType() == DocumentChange.Type.MODIFIED) {
                    this.items.add(doc.getId());
                } else {
                    this.items.remove(doc.getId());
                }
            }
            this.setAlive(true);
        }
    }

    @Override
    public void addLifetimeChangeCallback(LifetimeChangeCallback callback) {
        this.lifetimeCallbacks.add(callback);
    }

    @Override
    public void removeLifetimeChangeCallback(LifetimeChangeCallback callback) {
        this.lifetimeCallbacks.remove(callback);
    }

    @Override
    public void clearLifetimeChangeCallbacks() {
        this.lifetimeCallbacks.clear();
    }

    public boolean isAlive() {
        return this.alive;
    }

    private void setAlive(boolean alive) {
        if (this.alive != alive) {
            this.alive = alive;
            for (LifetimeChangeCallback callback : this.lifetimeCallbacks) {
                callback.onLifetimeChanged(this, alive);
            }
        }
    }

    public int size() {
        return this.items.size();
    }

    @Nullable
    public T get(@NonNull String key) {
        if (this.items.contains(key)) {
            DocumentReference ref = this.ref.document(key);
            try {
                return this.cls.getDeclaredConstructor(DocumentReference.class).newInstance(ref);
            } catch (@NonNull IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                Log.wtf(
                        TAG,
                        "(" + this.ref.getPath() + ") Error spawning object instance while retrieving from collection.",
                        e);
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public T add() {
        DocumentReference ref = this.ref.document();
        try {
            return this.cls.getDeclaredConstructor(DocumentReference.class).newInstance(ref);
        } catch (@NonNull IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            Log.wtf(
                    TAG,
                    "(" + this.ref.getPath() +
                    ") Error spawning object instance while pushing new object to collection.",
                    e);
            return null;
        }
    }
}
