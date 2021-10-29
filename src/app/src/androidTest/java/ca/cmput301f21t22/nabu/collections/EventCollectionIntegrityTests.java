package ca.cmput301f21t22.nabu.collections;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.Date;
import java.util.Objects;

import ca.cmput301f21t22.nabu.model.Event;
import ca.cmput301f21t22.nabu.model.LiveCollection;

public class EventCollectionIntegrityTests extends CollectionIntegrityTests<Event> {
    @NonNull
    @Override
    protected LiveCollection<Event> getCollection() {
        return new LiveCollection<Event>(Event.class, this.ref) {};
    }

    @Test
    public void AddThenGetEvent() {
        LiveCollection<Event> collection = this.getCollection();
        await().until(collection::isAlive);

        Event write = Objects.requireNonNull(collection.add());
        await().until(write::isAlive);

        write.setDate(new Date(934284434));
        write.setPhotoPath("/home/default/photos/test.png");

        Event read = collection.get(write.getId());
        assertNotNull(read);
        await().until(read::isAlive);

        await().until(() -> read.getDate() != null);
        assertEquals(new Date(934284434), read.getDate());

        await().until(() -> read.getPhotoPath() != null);
        assertEquals("/home/default/photos/test.png", read.getPhotoPath());
    }
}
