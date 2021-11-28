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
 * Runs tests on the social fragment.
 * User being logged out of the app is preferable, although the program will attempt to log the user
 * out otherwise.
 */
public class SocialFragmentTest {
    private Solo solo;
    private DateProvider dateProvider = new DateProvider();
    private String mainEmail = "boggles@swamp.bog";
    private String mainEmailPassword = "boggle";
    private String socialEmail = "Tim@magnus.uk";
    private String socialEmailPassword = "3yesSuck";
    private String socialName = "tim@magnus.uk";

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
     * Runs before all tests and sets up each test
     * Logs in to the main user
     * Sends a request to social user
     * Logs out
     * Logs in to social user
     * Sets up test case habits
     */
    @Before
    public void socialSetUp() {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

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

        //Logs into user account
        solo.enterText((EditText) solo.getView(R.id.email), mainEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), mainEmailPassword);
        solo.clickOnButton("Sign in");

        //Make request to follow social user
        solo.clickOnMenuItem("Social");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_follow_request));
        solo.enterText((EditText) solo.getView(R.id.edit_email), socialEmail);
        solo.clickOnText("Add");

        //Logs out main user
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Logs into main account
        solo.enterText((EditText) solo.getView(R.id.email), socialEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), socialEmailPassword);
        solo.clickOnButton("Sign in");

        //Creates three habits for testing purposes
        solo.clickOnMenuItem("Habits");

        //Should show up
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.clickOnText("Public");
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

        //Should show up
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.clickOnText("Public");
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bats");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");
        solo.clickOnToggleButton(this.dateProvider.getCurrentDay());

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want to have an army");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0,2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();

        //Should not show up as it is not public
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_add_habit));
        solo.enterText((EditText) solo.getView(R.id.edit_title), "Farm Bears");
        solo.clickOnToggleButton("Sun");
        solo.clickOnToggleButton("Mon");
        solo.clickOnToggleButton("Tue");
        solo.clickOnToggleButton("Wed");
        solo.clickOnToggleButton("Thu");
        solo.clickOnToggleButton("Fri");
        solo.clickOnToggleButton("Sat");

        solo.enterText((EditText) solo.getView(R.id.edit_reason), "I want to find beehives");
        solo.clickOnText("Start Date");
        solo.setDatePicker(0,2021,
                10, 5);
        solo.clickOnText("OK");
        solo.clickOnView((FloatingActionButton) solo.getView(R.id.button_save));
        solo.goBack();
    }

    /**
     * Takes a string to "Accept" or "Reject" the user, then accepts or rejects the user's request
     * and logs into the main user's account.
     */
    public void socialSetUpContinue(String option) {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Goes to social page and handles follow request
        solo.clickOnMenuItem("Social");
        if (option == "Accept") {
            solo.clickOnView((ImageButton) solo.getView(R.id.accept_follow_button));
        }
        else if (option == "Reject") {
            solo.clickOnView((ImageButton) solo.getView(R.id.reject_follow_Button));
        }

        //Logs Out
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Logs into account
        solo.enterText((EditText) solo.getView(R.id.email), mainEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), mainEmailPassword);
        solo.clickOnButton("Sign in");
        solo.clickOnMenuItem("Social");
    }

    /**
     * Check if main user is following the social user
     */
    @Test
    public void checkFollowUser() {
        this.socialSetUpContinue("Accept");

        //Check that email of social user shows up on main user's social screen
        assertTrue(solo.waitForText(socialName, 1, 2000));
    }

    /**
     * Check if social user's habit is properly shown on the MyDay Page
     */
    @Test
    public void checkFollowedUserHabits(){
        this.socialSetUpContinue("Accept");
        solo.clickOnMenuItem("My Day");

        //Check that social user's public habits appears
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));
        assertTrue(solo.waitForText("I want honey", 1, 2000));
        assertTrue(solo.waitForText("Bats", 1, 2000));

        //Check that the habits of social user that are not supposed to appear don't appear
        assertFalse(solo.waitForText("Bears", 1, 2000));
    }

    /**
     * Checks if social user's habits are shown on the MyDay Page
     * Logs out
     * Logs in to social user
     * Resets user data
     * Logs out
     * Logs in to main user
     * Checks that social user's habits have changed in the social feed
     */
    @Test
    public void checkFollowedUserHabitsUpdate(){
        this.socialSetUpContinue("Accept");
        solo.clickOnMenuItem("My Day");

        //Check that social user's public habits appear
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));
        assertTrue(solo.waitForText("I want honey", 1, 2000));
        assertTrue(solo.waitForText("Bats", 1, 2000));

        //Log out of account
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Log into other user, reset their data, and log them out
        solo.enterText((EditText) solo.getView(R.id.email), socialEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), socialEmailPassword);
        solo.clickOnButton("Sign in");
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Reset");
        solo.clickOnText("Reset", 2);
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Logs back into main account and checks that the habit is not on the my day page
        solo.enterText((EditText) solo.getView(R.id.email), mainEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), mainEmailPassword);
        solo.clickOnButton("Sign in");

        assertFalse(solo.waitForText("Farm Bees", 1, 2000));
        assertFalse(solo.waitForText("I want honey", 1, 2000));
        assertFalse(solo.waitForText("Bats", 1, 2000));
    }

    /**
     * Check if social user's habits are shown on the main user's MyDay Page
     * Unfollow social user
     * Check to make sure social user's habits aren't shown on the main user's MyDay Page
     */
    @Test
    public void checkUnfollowUser(){
        this.socialSetUpContinue("Accept");

        //Check that social user's public habits appear
        solo.clickOnMenuItem("My Day");
        assertTrue(solo.waitForText("Farm Bees", 1, 2000));
        assertTrue(solo.waitForText("I want honey", 1, 2000));
        assertTrue(solo.waitForText("Bats", 1, 2000));

        //Unfollow user
        solo.clickOnMenuItem("Social");
        solo.clickOnView((ImageButton) solo.getView(R.id.unfollow_Button));

        //Check that social user's public habits do not appear
        solo.clickOnMenuItem("My Day");
        assertFalse(solo.waitForText("Farm Bees", 1, 2000));
        assertFalse(solo.waitForText("I want honey", 1, 2000));
        assertFalse(solo.waitForText("Bats", 1, 2000));
    }


    /**
     * Indicates for social user to reject main user
     * Check if the main user is not following the social user
     * Check to make sure social user's habits aren't shown on the main user's Page
     */
    @Test
    public void checkRejectUser(){
        this.socialSetUpContinue("Reject");

        //Check that social user's public habit does not appear
        solo.clickOnMenuItem("My Day");
        assertFalse(solo.waitForText("Farm Bees", 1, 2000));
        assertFalse(solo.waitForText("I want honey", 1, 2000));
        assertFalse(solo.waitForText("Bats", 1, 2000));

        //Check that email of social user shows up on main user's social screen
        solo.clickOnMenuItem("Social");
        assertFalse(solo.waitForText(socialEmail, 1, 2000));

    }

    /**
     * Close activity after each test and reset accounts
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        //Reset data and log out main user
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Reset");
        solo.clickOnText("Reset", 2);
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        //Log into other user, reset their data, and log them out
        solo.enterText((EditText) solo.getView(R.id.email), socialEmail);
        solo.clickOnButton("Next");
        solo.enterText((EditText) solo.getView(R.id.password), socialEmailPassword);
        solo.clickOnButton("Sign in");
        solo.clickOnMenuItem("Settings");
        solo.clickOnText("Reset");
        solo.clickOnText("Reset", 2);
        solo.clickOnText("Sign out");
        solo.clickOnText("Sign", 3);

        solo.finishOpenedActivities();
    }
}
