package ca.cmput301f21t22.nabu.documents;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import ca.cmput301f21t22.nabu.model.LiveEvent;

public class LiveEventIntegrityTests extends IntegrityTests {
    @Test
    public void WriteEvent() {
        LiveEvent event = new LiveEvent(this.ref);

        assertNull(event.getDate());
        assertNull(event.getComment());
        assertNull(event.getPhotoPath());
        assertNull(event.getLocation());

        event.setDate(new Date(409238093));
        await().until(() -> event.getDate() != null);
        assertEquals(new Date(409238093), event.getDate());

        event.setComment("Some comment");
        await().until(() -> event.getComment() != null);
        assertEquals("Some comment", event.getComment());

        event.setPhotoPath("/home/default/photos/test.png");
        await().until(() -> event.getPhotoPath() != null);
        assertEquals("/home/default/photos/test.png", event.getPhotoPath());

        event.setLocation(new GeoPoint(53.523187, -113.526313));
        await().until(() -> event.getLocation() != null);
        assertEquals(new GeoPoint(53.523187, -113.526313), event.getLocation());
    }

    @Test
    public void WriteThenReadEvent() {
        LiveEvent write = new LiveEvent(this.ref);

        write.setDate(new Date(409238093));
        write.setComment("Some comment");
        write.setPhotoPath("/home/default/photos/test.png");
        write.setLocation(new GeoPoint(53.523187, -113.526313));

        LiveEvent read = new LiveEvent(this.ref);

        await().until(() -> read.getDate() != null);
        assertEquals(new Date(409238093), read.getDate());

        await().until(() -> read.getComment() != null);
        assertEquals("Some comment", read.getComment());

        await().until(() -> read.getPhotoPath() != null);
        assertEquals("/home/default/photos/test.png", read.getPhotoPath());

        await().until(() -> read.getLocation() != null);
        assertEquals(new GeoPoint(53.523187, -113.526313), read.getLocation());
    }

    @Test
    public void LiveUpdateEvent() {
        LiveEvent event1 = new LiveEvent(this.ref);

        LiveEvent event2 = new LiveEvent(this.ref);

        assertNull(event1.getDate());
        assertNull(event2.getDate());
        event1.setDate(new Date(920198319));
        await().until(() -> Objects.equals(event1.getDate(), event2.getDate()));
        assertEquals(new Date(920198319), event1.getDate());
        assertEquals(new Date(920198319), event2.getDate());

        assertNull(event1.getComment());
        assertNull(event2.getComment());
        event2.setComment("What is this comment?");
        await().until(() -> Objects.equals(event1.getComment(), event2.getComment()));
        assertEquals("What is this comment?", event1.getComment());
        assertEquals("What is this comment?", event2.getComment());

        assertNull(event1.getPhotoPath());
        assertNull(event2.getPhotoPath());
        event1.setPhotoPath("/home/user/photos/bananas.png");
        await().until(() -> Objects.equals(event1.getPhotoPath(), event2.getPhotoPath()));
        assertEquals("/home/user/photos/bananas.png", event1.getPhotoPath());
        assertEquals("/home/user/photos/bananas.png", event2.getPhotoPath());

        assertNull(event1.getLocation());
        assertNull(event2.getLocation());
        event2.setLocation(new GeoPoint(51.077562, -114.140687));
        await().until(() -> event1.getLocation() != null && event2.getLocation() != null);
        assertEquals(new GeoPoint(51.077562, -114.140687), event1.getLocation());
        assertEquals(new GeoPoint(51.077562, -114.140687), event2.getLocation());
    }

    @Test
    public void WriteThenDeleteEvent() {
        LiveEvent event = new LiveEvent(this.ref);

        event.setDate(new Date(409238093));
        event.setComment("Some comment");
        event.setPhotoPath("/home/default/photos/test.png");
        event.setLocation(new GeoPoint(53.523187, -113.526313));
        await().until(() -> event.getDate() != null && event.getComment() != null && event.getPhotoPath() != null &&
                            event.getLocation() != null);

        event.delete();
        await().until(() -> event.getDate() == null && event.getComment() == null && event.getPhotoPath() == null &&
                            event.getLocation() == null);
        this.ref.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(snapshot);
            Map<String, Object> data = snapshot.getData();
            assertNull(data);
        }).addOnFailureListener(task -> fail());
    }
}
