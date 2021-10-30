package ca.cmput301f21t22.nabu.documents;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.google.firebase.firestore.DocumentSnapshot;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import ca.cmput301f21t22.nabu.model.User;

public class UserIntegrityTests extends IntegrityTests {
    @Test
    public void WriteUser() {
        User user = new User(this.ref);
        await().until(user::isAlive);

        assertNull(user.getEmail());
        assertNull(user.getHabits());

        user.setEmail("user@gmail.com");
        await().until(() -> user.getEmail() != null);
        assertEquals("user@gmail.com", user.getEmail());

        user.setHabits(Arrays.asList("Habit1", "Habit3", "Habit2"));
        await().until(() -> user.getHabits() != null);
        assertEquals(Arrays.asList("Habit1", "Habit3", "Habit2"), user.getHabits());
    }

    @Test
    public void WriteThenReadUser() {
        User write = new User(this.ref);
        await().until(write::isAlive);

        write.setEmail("write@gmail.com");
        write.setHabits(Arrays.asList("Habit1", "Habit3", "Habit2"));

        User read = new User(this.ref);
        await().until(read::isAlive);

        await().until(() -> read.getEmail() != null);
        assertEquals("write@gmail.com", read.getEmail());

        await().until(() -> read.getHabits() != null);
        assertEquals(Arrays.asList("Habit1", "Habit3", "Habit2"), read.getHabits());
    }

    @Test
    public void LiveUpdateUser() {
        User user1 = new User(this.ref);
        await().until(user1::isAlive);

        User user2 = new User(this.ref);
        await().until(user2::isAlive);

        assertNull(user1.getEmail());
        assertNull(user2.getEmail());
        user1.setEmail("first_user@gmail.com");
        await().until(() -> Objects.equals(user1.getEmail(), user2.getEmail()));
        assertEquals("first_user@gmail.com", user1.getEmail());
        assertEquals("first_user@gmail.com", user2.getEmail());

        assertNull(user1.getHabits());
        assertNull(user2.getHabits());
        user2.setHabits(Arrays.asList("Habit4", "Habit8"));
        await().until(() -> Objects.equals(user1.getHabits(), user2.getHabits()));
        assertEquals(Arrays.asList("Habit4", "Habit8"), user1.getHabits());
        assertEquals(Arrays.asList("Habit4", "Habit8"), user2.getHabits());

        assertEquals("first_user@gmail.com", user1.getEmail());
        assertEquals("first_user@gmail.com", user2.getEmail());
        user2.setEmail("second_user@gmail.com");
        await().until(() -> Objects.equals(user1.getEmail(), user2.getEmail()));
        assertEquals("second_user@gmail.com", user1.getEmail());
        assertEquals("second_user@gmail.com", user2.getEmail());

        assertEquals(Arrays.asList("Habit4", "Habit8"), user1.getHabits());
        assertEquals(Arrays.asList("Habit4", "Habit8"), user2.getHabits());
        user1.setHabits(Arrays.asList("Habit9", "Habit2", "Habit4", "Habit3"));
        await().until(() -> Objects.equals(user1.getHabits(), user2.getHabits()));
        assertEquals(Arrays.asList("Habit9", "Habit2", "Habit4", "Habit3"), user1.getHabits());
        assertEquals(Arrays.asList("Habit9", "Habit2", "Habit4", "Habit3"), user2.getHabits());
    }

    @Test
    public void WriteThenDeleteUser() {
        User user = new User(this.ref);
        await().until(user::isAlive);

        user.setEmail("user@gmail.com");
        user.setHabits(Arrays.asList("Habit1", "Habit3", "Habit2"));
        await().until(() -> user.getEmail() != null && user.getHabits() != null);

        user.delete();
        await().until(() -> !user.isAlive());
        this.ref.get().addOnCompleteListener(task -> {
            DocumentSnapshot snapshot = task.getResult();
            assertTrue(task.isSuccessful());
            assertNotNull(snapshot);
            Map<String, Object> data = snapshot.getData();
            assertNull(data);
        }).addOnFailureListener(task -> fail());
    }
}
