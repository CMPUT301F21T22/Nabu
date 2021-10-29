package ca.cmput301f21t22.nabu;

import static org.awaitility.Awaitility.await;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;

public abstract class IntegrityTests {
    protected final DocumentReference ref =
            FirebaseFirestore.getInstance().collection("Testing").document(this.getClass().getSimpleName());
    private boolean ready = false;

    @Before
    public void Setup() {
        this.ref.delete().addOnCompleteListener(task -> this.ready = true);
        await().until(() -> this.ready);
    }
}
