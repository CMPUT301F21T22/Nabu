package ca.cmput301f21t22.nabu.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Objects;
import java.util.WeakHashMap;

public abstract class Document<TProperties extends Enum<TProperties>> implements EventListener<DocumentSnapshot> {
    @NonNull
    public static String TAG = "Document";

    @NonNull
    protected final DocumentReference ref;

    private boolean alive;
    @NonNull
    private final WeakHashMap<PropertyChangedCallback<TProperties>, Boolean> propertyCallbacks;
    @NonNull
    private final WeakHashMap<LifeChangedCallback<TProperties>, Boolean> lifetimeCallbacks;
    @NonNull
    private final Class<TProperties> cls;

    protected Document(@NonNull Class<TProperties> cls, @NonNull DocumentReference ref) {
        this.alive = false;
        this.propertyCallbacks = new WeakHashMap<>();
        this.lifetimeCallbacks = new WeakHashMap<>();
        this.cls = cls;

        this.ref = ref;
        this.ref.set(new HashMap<>(), SetOptions.merge());
        this.ref.addSnapshotListener(this);
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
        if (error != null || snapshot == null) {
            Log.e(TAG, "(" + this.ref.getPath() + ") Error while listening for document event.", error);
            this.setAlive(false);
        }
        // If data has been set to null, it's been deleted, and the object is no longer bound.
        else if (snapshot.getData() == null) {
            Log.d(TAG, "(" + this.ref.getPath() + ") Data set to null.");
            this.setAlive(false);
        }
        // Otherwise, we update fields.
        else {
            Log.d(TAG, "(" + this.ref.getPath() + ") Updating fields.");
            this.readFields(snapshot);
            this.setAlive(true);
        }
    }

    public void observeProperties(PropertyChangedCallback<TProperties> callback) {
        this.propertyCallbacks.put(callback, true);
        for (TProperties property : Objects.requireNonNull(this.cls.getEnumConstants())) {
            this.notifyPropertyChanged(property);
        }
    }

    public void observeLife(@NonNull LifeChangedCallback<TProperties> callback) {
        this.lifetimeCallbacks.put(callback, true);
        callback.onLifeChanged(this, this.alive);
    }

    @NonNull
    public String getId() {
        return this.ref.getId();
    }

    public boolean isAlive() {
        return this.alive;
    }

    protected void setAlive(boolean alive) {
        if (this.alive != alive) {
            this.alive = alive;
            for (LifeChangedCallback<TProperties> callback : this.lifetimeCallbacks.keySet()) {
                callback.onLifeChanged(this, alive);
            }
        }
    }

    public void delete() {
        this.ref.delete();
    }

    public abstract void readFields(@NonNull DocumentSnapshot snapshot);

    protected void notifyPropertyChanged(TProperties property) {
        for (PropertyChangedCallback<TProperties> callback : this.propertyCallbacks.keySet()) {
            callback.onPropertyChanged(this, property);
        }
    }

    public interface LifeChangedCallback<TProperties extends Enum<TProperties>> {
        void onLifeChanged(Document<TProperties> sender, boolean alive);
    }

    public interface PropertyChangedCallback<TProperties extends Enum<TProperties>> {
        void onPropertyChanged(Document<TProperties> sender, TProperties property);
    }
}
