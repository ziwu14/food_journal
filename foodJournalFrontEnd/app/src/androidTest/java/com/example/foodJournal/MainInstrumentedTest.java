package com.example.foodJournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.foodJournal.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void clickSignUpBtn() throws Exception {
        onView(withId(R.id.btnSign))
                .perform(click());
    }
    @Test
    public void clickLoginBtn() throws Exception {
        onView(withId(R.id.account_edit)).perform();
        onView(withId(R.id.password)).perform();
        onView(withId(R.id.btnLogin))
                .perform(click());
    }
}
