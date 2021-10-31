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

public abstract class LiveDocument<TProperties extends Enum<TProperties>> extends BaseObservable<TProperties>
        implements EventListener<DocumentSnapshot> {
    @NonNull
    public static String TAG = "Document";

    @NonNull
    protected final DocumentReference ref;

    protected LiveDocument(@NonNull Class<TProperties> cls, @NonNull DocumentReference ref) {
        super(cls);

        this.ref = ref;
        this.ref.set(new HashMap<>(), SetOptions.merge());
        this.ref.addSnapshotListener(this);
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
        if (error != null || snapshot == null) {
            Log.e(TAG, "(" + this.ref.getPath() + ") Error while listening for document event.", error);
            this.clearFields();
        }
        // If data has been set to null, it's been deleted, and the object is no longer bound.
        else if (snapshot.getData() == null) {
            Log.d(TAG, "(" + this.ref.getPath() + ") Data set to null.");
            this.clearFields();
        }
        // Otherwise, we update fields.
        else {
            Log.d(TAG, "(" + this.ref.getPath() + ") Updating fields.");
            this.readFields(snapshot);
        }
    }

    @NonNull
    public String getId() {
        return this.ref.getId();
    }

    public void delete() {
        this.ref.delete();
    }

    public abstract void readFields(@NonNull DocumentSnapshot snapshot);

    public abstract void clearFields();
}
