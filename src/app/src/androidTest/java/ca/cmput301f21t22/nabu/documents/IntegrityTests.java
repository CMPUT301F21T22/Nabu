package ca.cmput301f21t22.nabu.documents;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.fail;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;

public abstract class IntegrityTests {
    protected final DocumentReference ref =
            FirebaseFirestore.getInstance().collection("Testing").document(this.getClass().getSimpleName());
    private boolean ready = false;

    @Before
    public void Setup() {
        this.ref.delete().addOnCompleteListener(task -> this.ready = true).addOnFailureListener(task -> fail());
        await().until(() -> this.ready);
    }
}
