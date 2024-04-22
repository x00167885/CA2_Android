package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.widget.DatePicker;

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
}
