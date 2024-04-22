package com.example.eventplanner;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EventDetailsTest {

    @Rule
    public IntentsTestRule<EventDetails> intentsTestRule = new IntentsTestRule<>(EventDetails.class, true, false);

    // Configuring the data we expect to see within each test.
    private Intent createIntentWithExtras() {
        Intent testIntent = new Intent();
        Event mockEvent = new Event("Test Event", "2024-01-01", "This is a test event.");
        List<Person> mockPeopleList = new ArrayList<>();
        mockPeopleList.add(new Person("John Doe", 3));
        testIntent.putExtra("selectedEvent", mockEvent);
        testIntent.putExtra("retrievedPeople", (Serializable) mockPeopleList);
        return testIntent;
    }

    @Test
    public void checkEventDetailsDisplay() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.event_name_text_view)).check(matches(withText("Test Event")));
        onView(withId(R.id.event_date)).check(matches(withText("2024-01-01")));
        onView(withId(R.id.event_description)).check(matches(withText("This is a test event.")));
    }

    @Test
    public void clickEditButton() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.goto_update_event)).perform(click());
        intended(hasComponent(UpdateEvent.class.getName()));
    }

    @Test
    public void clickDeleteButtonAndConfirm() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.delete_event_button)).perform(click());
        onView(withText("Are you sure you want to delete this item?")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());
    }
}

