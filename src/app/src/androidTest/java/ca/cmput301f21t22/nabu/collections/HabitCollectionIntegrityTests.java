package ca.cmput301f21t22.nabu.collections;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.Objects;

import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.model.Habit;
import ca.cmput301f21t22.nabu.model.LiveCollection;

public class HabitCollectionIntegrityTests extends CollectionIntegrityTests<Habit> {
    @NonNull
    @Override
    protected LiveCollection<Habit> getCollection() {
        return new LiveCollection<Habit>(Habit.class, this.ref) {};
    }

    @Test
    public void AddThenGetHabit() {
        LiveCollection<Habit> collection = this.getCollection();
        await().until(collection::isAlive);

        Habit write = Objects.requireNonNull(collection.add());
        await().until(write::isAlive);

        write.setTitle("Go to Work");
        write.setReason("Need that money.");
        write.setOccurrence(new Occurrence());

        Habit read = collection.get(write.getId());
        assertNotNull(read);
        await().until(read::isAlive);

        await().until(() -> read.getTitle() != null);
        assertEquals("Go to Work", read.getTitle());

        await().until(() -> read.getReason() != null);
        assertEquals("Need that money.", read.getReason());

        await().until(() -> read.getOccurrence() != null);
        assertEquals(new Occurrence(), read.getOccurrence());
    }
}
