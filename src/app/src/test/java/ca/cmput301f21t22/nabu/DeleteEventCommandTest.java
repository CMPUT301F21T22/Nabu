package ca.cmput301f21t22.nabu;

import static org.junit.Assert.assertEquals;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import ca.cmput301f21t22.nabu.data.Event;
import ca.cmput301f21t22.nabu.data.Habit;
import ca.cmput301f21t22.nabu.model.commands.AddEventCommand;
import ca.cmput301f21t22.nabu.model.commands.DeleteEventCommand;

public class DeleteEventCommandTest {
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

    private void testDeleteEventCommand() {
        Event event = new Event((new GregorianCalendar()).getTime(), "another day another floss", "user/events/event300", new GeoPoint(15, 78));

        AddEventCommand addEventCommand = new AddEventCommand(new Habit(), event);
        DeleteEventCommand deleteEventCommand = new DeleteEventCommand(event);
        assertEquals(true, deleteEventCommand.execute());
        //TODO: add process for checking database
    }
}