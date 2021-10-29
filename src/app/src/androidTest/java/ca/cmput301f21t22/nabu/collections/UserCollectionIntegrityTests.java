package ca.cmput301f21t22.nabu.collections;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.Arrays;
import java.util.Objects;

import ca.cmput301f21t22.nabu.model.LiveCollection;
import ca.cmput301f21t22.nabu.model.User;

public class UserCollectionIntegrityTests extends CollectionIntegrityTests<User> {

    @NonNull
    @Override
    protected LiveCollection<User> getCollection() {
        return new LiveCollection<User>(User.class, this.ref) {};
    }

    @Test
    public void AddThenGetUser() {
        LiveCollection<User> collection = this.getCollection();
        await().until(collection::isAlive);

        User write = Objects.requireNonNull(collection.add());
        await().until(write::isAlive);

        write.setUserId("User ID");
        write.setHabits(Arrays.asList("Habit1", "Habit2", "Habit3"));

        User read = collection.get(write.getId());
        assertNotNull(read);
        await().until(read::isAlive);

        await().until(() -> read.getUserId() != null);
        assertEquals("User ID", read.getUserId());

        await().until(() -> read.getHabits() != null);
        assertEquals(Arrays.asList("Habit1", "Habit2", "Habit3"), read.getHabits());
    }
}
