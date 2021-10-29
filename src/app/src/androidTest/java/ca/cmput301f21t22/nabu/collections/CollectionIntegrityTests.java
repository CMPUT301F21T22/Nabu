package ca.cmput301f21t22.nabu.collections;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ca.cmput301f21t22.nabu.model.LiveCollection;
import ca.cmput301f21t22.nabu.model.LiveDocument;

public abstract class CollectionIntegrityTests<T extends LiveDocument<?>> {
    protected final CollectionReference ref = FirebaseFirestore.getInstance()
                                                               .collection("Testing")
                                                               .document(this.getClass().getSimpleName())
                                                               .collection("Items");
    private int total = Integer.MIN_VALUE;
    private final AtomicInteger deleted = new AtomicInteger();

    @Before
    public void Setup() {
        this.total = Integer.MIN_VALUE;
        this.ref.get().addOnCompleteListener(task -> {
            QuerySnapshot snapshots = Objects.requireNonNull(task.getResult());
            this.total = snapshots.size();
            for (DocumentSnapshot document : snapshots.getDocuments()) {
                document.getReference().delete().addOnSuccessListener(v -> this.deleted.getAndIncrement());
            }
        }).addOnFailureListener(task -> fail());
        await().until(() -> this.total == this.deleted.get());
    }

    @Test
    public void AddItems() {
        LiveCollection<T> collection = this.getCollection();
        await().until(collection::isAlive);
        await().until(() -> collection.size() == 0);

        T item1 = collection.add();
        assertNotNull(item1);
        await().until(item1::isAlive);
        assertNotNull(item1.getId());
        await().until(() -> collection.size() == 1);

        T item2 = collection.add();
        assertNotNull(item2);
        await().until(item2::isAlive);
        assertNotNull(item2.getId());
        await().until(() -> collection.size() == 2);
    }

    @Test
    public void AddThenGetItems() {
        LiveCollection<T> collection = this.getCollection();
        await().until(collection::isAlive);
        await().until(() -> collection.size() == 0);

        T added = Objects.requireNonNull(collection.add());
        await().until(added::isAlive);
        await().until(() -> collection.size() == 1);

        T read = collection.get(added.getId());
        assertNotNull(read);
        await().until(read::isAlive);
        await().until(() -> collection.size() == 1);
    }

    @Test
    public void AddThenDeleteItems() {
        LiveCollection<T> collection = this.getCollection();
        await().until(collection::isAlive);
        await().until(() -> collection.size() == 0);

        T item1 = Objects.requireNonNull(collection.add());
        await().until(item1::isAlive);
        await().until(() -> collection.size() == 1);

        T item2 = Objects.requireNonNull(collection.add());
        await().until(item2::isAlive);
        await().until(() -> collection.size() == 2);

        item1.delete();
        await().until(() -> !item1.isAlive());
        await().until(() -> collection.size() == 1);

        item2.delete();
        await().until(() -> !item2.isAlive());
        await().until(() -> collection.size() == 0);
    }

    @Test
    public void AddThenGetThenDeleteItems() {
        LiveCollection<T> collection = this.getCollection();
        await().until(collection::isAlive);
        await().until(() -> collection.size() == 0);

        T added = Objects.requireNonNull(collection.add());
        await().until(added::isAlive);

        T read = Objects.requireNonNull(collection.get(added.getId()));
        await().until(read::isAlive);

        added.delete();
        await().until(() -> !added.isAlive());
        await().until(() -> !read.isAlive());
    }

    @NonNull
    protected abstract LiveCollection<T> getCollection();
}
