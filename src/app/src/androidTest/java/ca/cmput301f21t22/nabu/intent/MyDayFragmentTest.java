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
 * Runs tests on the My Day fragment.
 * User being logged out of the app is preferable, although the program will attempt to log the user
 * out otherwise.
 */
public class MyDayFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);
    private Solo solo;
    private DateProvider dateProvider = new DateProvider();

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
     * Creates a Habit scheduled for the current day
     * Creates a Habit scheduled for all days
     * Creates a Habit scheduled for days except for the current day
     * Checks if the habits appear in the My Day Fragment
     */
    @Test
    public void checkHabitsAppear() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates 3 Habits
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bees");
        solo.clickOnToggleButton(this.dateProvider.getCurrentDay());

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("Farm Bees");

        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Hornets");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("Farm Hornets");

        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Wasps");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Go to My Day Fragment and check what Habits appear
        solo.clickOnMenuItem("My Day");
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));
        assertTrue(solo.waitForText("Farm Hornets", 1, 2000));
        assertFalse(solo.waitForText("Farm Wasps", 1, 2000));
    }

    /**
     * Creates a Habit scheduled for the current day
     * Checks that it appears in the My Day Fragment
     * Deletes the Habit
     * Checks that the Habit does not appear in the My Day Fragment
     */
    @Test
    public void checkRegistersDelete() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates a Habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bees");
        solo.clickOnToggleButton(this.dateProvider.getCurrentDay());

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Checks Habit is shown in My Day Fragment
        solo.clickOnMenuItem("My Day");
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));

        //Deletes Habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((ImageButton) solo.getView(R.id.button_overflow_menu));
        solo.clickOnMenuItem("Delete Habit");
        solo.clickOnText("Delete");

        //Checks that Habit is no longer shown in My Day Fragment
        solo.clickOnMenuItem("My Day");
        assertFalse(solo.waitForText("Farm Bees", 1, 2000));
    }

    /**
     * Creates a Habit that has the current day scheduled
     * Presses the Habit in the My Day Fragment
     * Checks if Habit updated to be checked
     * Unchecks Habit
     */
    @Test
    public void checkChecked() {
        //Asserts that the current activity is the MainActivity. Otherwise, show ???Wrong Activity???
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Creates a Habit
        solo.clickOnMenuItem("Habits");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bees");
        solo.clickOnToggleButton(this.dateProvider.getCurrentDay());

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want honey");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0, 2021, 10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.waitForText("My Day");

        //Clicks habit and asserts that click has been registered
        solo.clickOnMenuItem("My Day");
        solo.clickOnText("Farm Bees");
        assertTrue(solo.waitForText("Habit marked as complete", 1, 2000));
        solo.clickOnText("Farm Bees");
    }

    /**
     * Close activity after each test
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
