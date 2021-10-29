package ca.cmput301f21t22.nabu.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserCollection extends LiveCollection<User> {
    @Nullable
    private static UserCollection INSTANCE;

    protected UserCollection(@NonNull Class<User> cls, @NonNull CollectionReference ref) {
        super(cls, ref);
    }

    @NonNull
    public synchronized static UserCollection getInstance() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (INSTANCE == null) {
            INSTANCE = new UserCollection(User.class, db.collection("Users"));
        }

        return INSTANCE;
    }
}
