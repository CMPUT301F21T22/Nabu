package ca.cmput301f21t22.nabu.intent;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import ca.cmput301f21t22.nabu.MainActivity;
import ca.cmput301f21t22.nabu.R;

/**
 * Runs tests on the login menu
 * User being logged out of the app is preferable, although the program will attempt to log the user
 * out otherwise.
 * Each test, at its end deletes any Habits and Events it makes, and then signs out
 */
public class SignInTest {
    private Solo solo;
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
     * Creates an account
     * Checks settings to make sure you are in that account
     */
    @Ignore("As there is no way within the code to remove a user from the database, this test " +
            "should only be used with express intent. Also, whenever this test is run, a new email "
            + "must be used")
    @Test
    public void checkSignUp() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        String email = "test3@pleasedelete.del";
        //Signs up for account
        solo.enterText((EditText) solo.getView(R.id.email), email);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "password123");
        solo.enterText((EditText) solo.getView(R.id.name), "John Smith");
        solo.clickOnButton("Save");

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Logs into account
        solo.enterText((EditText) solo.getView(R.id.email), email);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "password123");
        solo.clickOnButton("Sign in");

        //Checks that user is logged in
        solo.clickOnMenuItem("Settings");
        assertTrue(solo.waitForText(email, 1,
                2000));

        //Logs Out
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);
    }

    /**
     * Signs into an account
     * Checks the settings to make sure you are signed in
     * Logs Out
     * Checks to make sure you are
     */
    @Test
    public void checkSignIn() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Logs into account
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        solo.clickOnMenuItem("Settings");

        //Checks that user is logged in
        assertTrue(solo.waitForText("boggles@swamp.bog", 1, 2000));

        //Logs Out
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Checks that user is logged out
        assertTrue(solo.waitForText("Sign in", 1, 4000));
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
    public void checkDataKeeps() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Logs into account
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        //Creates a Habit
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
        solo.clickOnText("Nov 5, 2021");
        solo.setDatePicker(0,2021,
                10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_text_comment), "Bought Bees");
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
        assertTrue(solo.waitForText("Nov 5, 2021", 1, 2000));
        assertTrue(solo.waitForText("Every Day",
                1, 2000));

        //Event Checks
        assertTrue(solo.waitForText("Bought Bees", 1, 2000));
        assertTrue(solo.waitForText("Nov 9, 2021", 1, 2000));

        //Deletes Habit
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);
    }


    /**
     * Signs into an account
     * Creates a new Habit
     * Creates a new Event
     * Creates a new Habit
     * Resets account from menu
     * Logs out of Account
     * Logs back into Account and checks if Habits and the Event are deleted
     */
    @Test
    public void checkReset() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Logs into account
        solo.enterText((EditText) solo.getView(R.id.email), "boggles@swamp.bog");
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), "boggle");
        solo.clickOnButton("Sign in");

        //Creates a Habit
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
        solo.clickOnText("Nov 5, 2021");
        solo.setDatePicker(0,2021,
                10, 9);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.edit_text_comment), "Bought Bees");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Go to Habits Fragment and make another Habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Hornets");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
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


        //Deletes Habits
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        //Asserts that information has been deleted
        solo.clickOnMenuItem("Habits");

        //Habit1 checks
        assertFalse(solo.waitForText("Farm Bees", 1, 2000));
        assertFalse(solo.waitForText("I want honey", 1, 2000));
        assertFalse(solo.waitForText("Nov 5, 2021", 1, 2000));
        assertFalse(solo.waitForText("Every Day",
                1, 2000));

        //Event Checks
        assertFalse(solo.waitForText("Bought Bees", 1, 2000));
        assertFalse(solo.waitForText("Nov 9, 2021", 1, 2000));

        //Habit2 checks
        //Asserts Habit is on screen and has the relevant information
        assertFalse(solo.waitForText("Farm Hornets", 1, 2000));
        assertFalse(solo.waitForText("Sun, Mon, Tues, Thu, Fri, Sat",
                1, 2000));

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);
    }

    /**
     * Close activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

}
