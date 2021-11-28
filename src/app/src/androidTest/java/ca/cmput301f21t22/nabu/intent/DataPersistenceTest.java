package ca.cmput301f21t22.nabu.intent;

import static junit.framework.TestCase.assertTrue;

import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ca.cmput301f21t22.nabu.MainActivity;
import ca.cmput301f21t22.nabu.R;
import ca.cmput301f21t22.nabu.TestResources.DateProvider;

public class DataPersistenceTest {
    private Solo solo;
    private DateProvider dateProvider = new DateProvider();
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Runs before all tests and makes sure the user is logged out before logging in to test account
     */
    @Before
    public void logInSetup() {
        //Checks if application is on the log in menu, if not, tries to log out
        if (solo.waitForText("Sign in", 1, 4000) == false) {

            //Checks if application is on a page where the navigation bar is, if not, go back to it
            if (solo.waitForText("Settings", 1, 4000) == false) {
                solo.goBack(); //Any fragment is at most one back away from the navigation bar
            }

            //Logs out user
            solo.clickOnMenuItem("Settings");
            solo.clickOnText("Sign out");
            solo.clickOnText("Sign", 3);
        }

        //Logs in test account
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Signs into an account
     * Creates a new Habit
     * Creates a new Event
     * Logs out of an account
     * Logs back into the account
     * Checks if Habit and Event are retained
     */
    @Test
    public void checkAddedDataKeeps() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates a Habit to be stored
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bees");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0,2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Go to My Day Fragment and create an Event
        solo.clickOnMenuItem("My Day");
        solo.clickOnText("Farm Bees");
        solo.clickOnText("Edit Event");
        solo.clickOnText(this.dateProvider.getCurrentYear());
        solo.setDatePicker(0,2021,
                10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "Bought Bees");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Sign in again
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        //Asserts that information has been retained between log ins
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        //Habit checks
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));
        assertTrue(solo.waitForText("I want honey", 1, 2000));
        assertTrue(solo.waitForText(this.dateProvider.getCurrentDate(), 1, 2000));
        assertTrue(solo.waitForText("Every Day",
                1, 2000));

        //Event Checks
        assertTrue(solo.waitForText("Bought Bees", 1, 2000));
        assertTrue(solo.waitForText("Nov 9, 2021", 1, 2000));
    }

    @Test
    public void checkEditedDataKeeps() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates a Habit to be edited
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Beavers");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want dams");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0,2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Go to My Day Fragment and create an Event
        solo.clickOnMenuItem("My Day");
        solo.clickOnText("Farm Beavers");
        solo.clickOnText("Edit Event");
        solo.clickOnText(this.dateProvider.getCurrentYear());
        solo.setDatePicker(0,2021,
                10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "Bought Beavers");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Edits the farm beavers habit and habit event
        //Edits the Habit
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Edit Habit");

        solo.clearEditText((EditText) solo.getView(R.id.edit_title));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Exterminate Bees");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Sat");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.clearEditText((EditText) solo.getView(R.id.edit_reason));
        solo.enterText((EditText) solo.getView(R.id.edit_reason), "They went crazy");
        solo.clickOnView(solo.getView(R.id.text_start_date));
        solo.setDatePicker(0,2021,
                10, 9);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Edits the Event
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        solo.clickOnImage(6);
        solo.clickOnMenuItem("Edit Event");
        solo.clickOnView(solo.getView(R.id.text_date));
        solo.setDatePicker(0,2021,
                11, 15);
        solo.clickOnText("OK");
        solo.clearEditText((EditText) solo.getView(R.id.edit_comment));
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "Getting new bees");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Sign in again
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        //Asserts that information has been retained between log ins
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        //Habit checks
        assertTrue(solo.waitForText("Farm Beans", 1, 2000));
        assertTrue(solo.waitForText("I want beans", 1, 2000));
        assertTrue(solo.waitForText(this.dateProvider.getCurrentDate(), 1, 2000));
        assertTrue(solo.waitForText("Every Day",
                1, 2000));

        //Event Checks
        assertTrue(solo.waitForText("Bought Beans", 1, 2000));
        assertTrue(solo.waitForText("Nov 9, 2021", 1, 2000));

    }

    @Test
    public void checkDeletedDataKeeps() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates a Habit to be deleted
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Beats");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want soup");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Deletes Habit
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        //Creates a Habit for an event to be attached and deleted
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Birds");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I like birds");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Go to My Day Fragment and create an Event
        solo.clickOnMenuItem("My Day");
        solo.clickOnText("Farm Birds");
        solo.clickOnText("Edit Event");
        solo.clickOnText(this.dateProvider.getCurrentYear());
        solo.setDatePicker(0, 2021,
                10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), "Bought Birds");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Deletes the Event
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        //TODO: Issue, can not find proper index for event overflow menu, and cannot seem to press
        // it otherwise
        solo.clickOnImage(6);
        solo.clickOnMenuItem("Delete Event");
        solo.clickOnText("Delete");

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Sign in again
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        //Asserts that information has been retained between log ins
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        //Habit checks
        assertFalse(solo.waitForText("Farm Beats", 1, 2000));
        assertFalse(solo.waitForText("I want soup", 1, 2000));

        //Event Checks
        assertFalse(solo.waitForText("Bought Birds", 1, 2000));
        assertFalse(solo.waitForText("Nov 9, 2021", 1, 2000));
    }

    /**
     * Logout and close activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Reset");
        solo.clickOnText("Reset", 2);
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);
        solo.finishOpenedActivities();
    }
}
