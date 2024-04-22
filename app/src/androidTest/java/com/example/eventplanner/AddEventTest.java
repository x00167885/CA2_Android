package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddEventTest {

    @Rule
    public ActivityScenarioRule<AddEvent> activityScenarioRule = new ActivityScenarioRule<>(AddEvent.class);

    @Test
    public void addEventTest() {
        onView(withId(R.id.add_event_name)).perform(replaceText("Test Event"), closeSoftKeyboard());
        onView(withId(R.id.add_event_date)).perform(click());
        Calendar calendar = Calendar.getInstance();
        onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(isDialog()).perform(setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.add_event_description)).perform(replaceText("Test Description"), closeSoftKeyboard());
        onView(withId(R.id.add_event_button)).perform(click());
    }

    @Test
    public void ensureDatePickerDialogOpens() {
        onView(withId(R.id.add_event_date)).perform(click());
        onView(withText("OK")).check(matches(isDisplayed()));
        onView(withText("Cancel")).check(matches(isDisplayed()));
    }

    @Test
    public void testFieldsAreRequired() {
        // Try to add an event with empty title
        onView(withId(R.id.add_event_name)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.add_event_date)).perform(replaceText("2024-05-01"), closeSoftKeyboard());
        onView(withId(R.id.add_event_description)).perform(replaceText("Sample Description"), closeSoftKeyboard());
        onView(withId(R.id.add_event_button)).perform(click());
        // Since the activity doesn't close, we just check that we're still on the same screen
        onView(withId(R.id.add_event_button)).check(matches(isDisplayed()));

        // Reset and try with empty date
        onView(withId(R.id.add_event_name)).perform(replaceText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.add_event_date)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.add_event_button)).perform(click());
        onView(withId(R.id.add_event_button)).check(matches(isDisplayed()));

        // Reset and try with empty description
        onView(withId(R.id.add_event_name)).perform(replaceText("Sample Event"), closeSoftKeyboard());
        onView(withId(R.id.add_event_date)).perform(replaceText("2024-05-01"), closeSoftKeyboard());
        onView(withId(R.id.add_event_description)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.add_event_button)).perform(click());
        onView(withId(R.id.add_event_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddEventAndCheckResultInEventsList() {
        // Initialize Espresso Intents
        init();
        // Launch the EventsList activity
        ActivityScenario.launch(EventsList.class);

        // Perform actions on the EventsList activity
        onView(withId(R.id.add_event_button_link)).perform(click());

        // Verify that we are on the AddEvent activity
        onView(withId(R.id.add_event)).check(matches(isDisplayed()));

        // Fill in all fields in AddEvent activity
        onView(withId(R.id.add_event_name)).perform(replaceText("Test Event"), closeSoftKeyboard());
        onView(withId(R.id.add_event_date)).perform(click());
        Calendar calendar = Calendar.getInstance();
        onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(isDialog()).perform(setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
        onView(withId(R.id.add_event_description)).perform(replaceText("Test Description"), closeSoftKeyboard());
        // Perform the click to add the event
        onView(withId(R.id.add_event_button)).perform(click());

        // Verify that an intent was sent back to EventsList activity
        intended(hasComponent(EventsList.class.getName()));

        // Release Espresso Intents
        release();
    }
}
