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
import java.util.HashSet;

public abstract class LiveDocument<TProperties extends Enum<TProperties>>
        implements EventListener<DocumentSnapshot>, PropertyObservable<TProperties>, LifetimeObservable {
    public static String TAG = "LiveDocument";

    protected boolean alive;
    @NonNull
    protected final DocumentReference ref;
    @NonNull
    private final HashSet<PropertyChangeCallback<TProperties>> propertyCallbacks;
    @NonNull
    private final HashSet<LifetimeChangeCallback> lifetimeCallbacks;

    protected LiveDocument(@NonNull DocumentReference ref) {
        this.alive = false;
        this.propertyCallbacks = new HashSet<>();
        this.lifetimeCallbacks = new HashSet<>();

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

    @Override
    public void addPropertyChangeCallback(PropertyChangeCallback<TProperties> callback) {
        this.propertyCallbacks.add(callback);
    }

    @Override
    public void removePropertyChangeCallback(PropertyChangeCallback<TProperties> callback) {
        this.propertyCallbacks.remove(callback);
    }

    @Override
    public void clearPropertyChangeCallbacks() {
        this.propertyCallbacks.clear();
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

    public String getId() {
        return this.ref.getId();
    }

    public boolean isAlive() {
        return this.alive;
    }

    protected void setAlive(boolean alive) {
        if (this.alive != alive) {
            this.alive = alive;
            for (LifetimeChangeCallback callback : this.lifetimeCallbacks) {
                callback.onLifetimeChanged(alive);
            }
        }
    }

    public void delete() {
        this.ref.delete();
    }

    protected abstract void readFields(@NonNull DocumentSnapshot snapshot);

    protected void notifyPropertyChanged(TProperties property) {
        for (PropertyChangeCallback<TProperties> callback : this.propertyCallbacks) {
            callback.onPropertyChanged(property);
        }
    }
}
