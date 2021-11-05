package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.data.Occurrence;
import ca.cmput301f21t22.nabu.model.commands.AddEventCommand;

public class AddHabitCommandTest {
    @Before
    @Test
    private void fireSetUp() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.useEmulator("10.0.2.2", 8080);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        firestore.setFirestoreSettings(settings);
    }

    @Test
    private void testAddHabitCommand() {
        Habit habit = new Habit("Floss teeth", "get healthier teeth", new GregorianCalendar().getTime(), new Occurrence(), new ArrayList<>(), false);

        AddEventCommand addHabitCommand = new AddEventCommand(habit, new Event());
        assertEquals(habit, addHabitCommand.execute());
        //TODO: add process for checking database
    }
}