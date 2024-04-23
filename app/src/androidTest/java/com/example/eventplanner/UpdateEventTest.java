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
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.content.Intent;
import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateEventTest {

    @Rule
    public ActivityScenarioRule<UpdateEvent> activityRule = new ActivityScenarioRule<>(UpdateEvent.class);

    @Test
    public void testDatePickerDialog() {
        onView(withId(R.id.update_event_date)).perform(click());
        onView(withClassName(equalTo(DatePicker.class.getName()))).perform(setDate(2024, 1, 1));
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.update_event_date)).check(matches(withText("2024-01-01")));
    }

    @Test
    public void testCancelButton() {
        onView(withId(R.id.back_to_event_details)).perform(click());
    }
    @Test
    public void testUpdateEventAndCheckResultInEventDetails() {
        // Initialize Espresso Intents
        init();

        // Create the Intent that should be used to start EventDetails
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EventDetails.class);
        Event mockEvent = new Event("Mock Event", "2022-01-01","This is a mock event.", 12, Event.EventType.FOODFESTIVAL );
        ArrayList<Person> mockPeopleList = new ArrayList<>();
        mockPeopleList.add(new Person("John Doe", 3));
        intent.putExtra("selectedEvent", mockEvent);
        intent.putExtra("retrievedPeople", mockPeopleList);

        // Launch the EventDetails activity with the Intent
        try (ActivityScenario<EventDetails> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.goto_update_event)).perform(click());

            // Now assuming we're in the UpdateEvent activity, we fill in updated details for the event
            onView(withId(R.id.update_event_name)).perform(replaceText("Updated Event Name"), closeSoftKeyboard());
            onView(withId(R.id.update_event_date)).perform(click());
            Calendar calendar = Calendar.getInstance();
            onView(withClassName(equalTo(DatePicker.class.getName()))).inRoot(isDialog()).perform(setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
            onView(withId(android.R.id.button1)).inRoot(isDialog()).perform(click());
            onView(withId(R.id.update_event_description)).perform(replaceText("Updated Event Description"), closeSoftKeyboard());

            // Perform the click to update the event
            onView(withId(R.id.update_event_button)).perform(click());

            // Verify that an intent was sent back to EventDetails activity
            intended(hasComponent(EventDetails.class.getName()));
        }

        release();
    }

}