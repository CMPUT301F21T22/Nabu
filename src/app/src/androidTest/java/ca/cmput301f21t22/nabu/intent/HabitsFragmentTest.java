package ca.cmput301f21t22.nabu.intent;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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

/**
 * Runs tests on the habits fragment.
 * User being logged out of the app is preferable, although the program will attempt to log the user
 * out otherwise.
 */
public class HabitsFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);
    private Solo solo;
    private DateProvider dateProvider = new DateProvider();
    private String habitText = "Farm Bees";
    private String habitReason = "I want Honey";

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
            solo.clickOnText("Reset");
            solo.clickOnText("Reset", 2);
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
     * Goes to the Habit page
     * Adds a Habit
     * Checks if that Habit's information is correct
     */
    @Test
    public void checkAdd() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));

        //Asserts Habit is on screen and has the relevant information
        assertTrue(solo.waitForText(this.habitText, 1, 2000));
        assertTrue(solo.waitForText(this.habitReason, 1, 2000));
        assertTrue(solo.waitForText(this.dateProvider.getCurrentYear(), 1, 2000));
        assertTrue(solo.waitForText("Every Day", 1, 2000));

        //Check again, but this time with different dates
        String habitText2 = "Farm Hornets";
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), habitText2);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));

        //Asserts Habit is on screen and has the relevant information
        assertTrue(solo.waitForText(habitText2, 1, 2000));
        assertTrue(solo.waitForText(this.habitReason, 1, 2000));
        assertTrue(solo.waitForText("Nov 5, 2021", 1, 2000));
        assertTrue(solo.waitForText("Sun, Mon, Tues, Thu, Fri, Sat", 1, 2000));

        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");
    }

    /**
     * Goes to Habit page
     * Adds a Habit
     * Creates a new Event
     * Checks if Event information is accessible and correct
     */
    @Test
    public void checkEvent() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Go to My Day Fragment and create an Event
        String eventComment = "Bought Bees";
        solo.clickOnMenuItem("My Day");
        solo.clickOnText(this.habitText);
        solo.clickOnText("Edit Event");
        solo.clickOnText(this.dateProvider.getCurrentYear());
        solo.setDatePicker(0, 2021, 10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), eventComment);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("Habits");

        //Check the Event
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        assertTrue(solo.waitForText(eventComment, 1, 2000));
        assertTrue(solo.waitForText("Nov 9, 2021", 1, 2000));

        solo.goBack();
    }

    /**
     * Creates a Habit
     * Deletes the Habit
     * Checks if the habit is deleted
     */
    @Test
    public void checkDelete() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText(this.habitText);

        //Deletes Habit
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        //Checks if deleted
        assertFalse(solo.waitForText(this.habitText, 1, 2000));
    }

    /**
     * Creates a Habit
     * Creates an Event
     * Deletes the Event
     * Checks if the Event is deleted
     */
    @Test
    public void checkDeleteEvent() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Go to My Day Fragment and create an Event
        String eventComment = "Bought Bees";
        solo.clickOnMenuItem("My Day");
        solo.clickOnText(this.habitText);
        solo.clickOnText("Edit Event");
        solo.clickOnText("2021");
        solo.setDatePicker(0, 2021, 10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), eventComment);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("Habits");

        //Deletes the Event
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        solo.waitForText("Bought Bees");
        solo.clickOnImage(6);
        solo.clickOnMenuItem("Delete Event");
        solo.clickOnText("Delete");

        //Checks if Event deleted
        solo.clickOnMenuItem("My Day");
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        assertFalse(solo.waitForText(eventComment, 1, 2000));
        assertFalse(solo.waitForText("Nov 9, 2021", 1, 2000));
    }

    /**
     * Creates a Habit
     * Edits a Habit
     * Checks if the Habit is updated
     */
    @Test
    public void checkEdit() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText(this.habitText);

        //Edits the Habit
        String editedHabitTitle = "Exterminate Bees";
        String editedHabitReason = "They went crazy";
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Edit Habit");

        solo.clearEditText((EditText) solo.getView(R.id.edit_title));
        solo.enterText((EditText) solo.getView(R.id.edit_title), editedHabitTitle);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Sat");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.clearEditText((EditText) solo.getView(R.id.edit_reason));
        solo.enterText((EditText) solo.getView(R.id.edit_reason), editedHabitReason);
        solo.clickOnView(solo.getView(R.id.text_start_date));
        solo.setDatePicker(0, 2021, 10, 9);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));

        //Checks if the Habit was updated
        //Asserts Habit is on screen and has the relevant information
        assertTrue(solo.waitForText(editedHabitTitle, 1, 2000));
        assertTrue(solo.waitForText(editedHabitReason, 1, 2000));
        assertTrue(solo.waitForText("Nov 9, 2021", 1, 2000));
        assertTrue(solo.waitForText("Fri, Sat", 1, 2000));
    }

    /**
     * Creates a Habit
     * Creates an Event
     * Edits the Event
     * Checks if the Event is updated
     */
    @Test
    public void checkEditEvent() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to the Habits Fragment and create a habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), this.habitText);
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), this.habitReason);
        solo.clickOnView(solo.getView(R.id.text_start_date));
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Go to My Day Fragment and create an Event
        String eventComment1 = "Bought Bees";
        solo.clickOnMenuItem("My Day");
        solo.clickOnText(this.habitText);
        solo.clickOnText("Edit Event");
        solo.clickOnText(this.dateProvider.getCurrentYear());
        solo.setDatePicker(0, 2021, 10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_comment), eventComment1);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("Habits");

        //Edits the Event
        String eventComment2 = "Getting new bees";
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("View Events");

        solo.waitForText("Bought Bees");
        solo.clickOnImage(6);
        solo.clickOnMenuItem("Edit Event");
        solo.clickOnView(solo.getView(R.id.text_date));
        solo.setDatePicker(0, 2021, 11, 15);
        solo.clickOnText("OK");
        solo.clearEditText((EditText) solo.getView(R.id.edit_comment));
        solo.enterText((EditText) solo.getView(R.id.edit_comment), eventComment2);
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));

        //Checks if the Event is edited
        assertTrue(solo.waitForText(eventComment2, 1, 2000));
        assertTrue(solo.waitForText("Dec 15, 2021", 1, 2000));
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
