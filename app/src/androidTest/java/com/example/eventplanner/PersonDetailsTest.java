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

import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import org.junit.Rule;
import org.junit.Test;

public class PersonDetailsTest {

    @Rule
    public IntentsTestRule<PersonDetails> intentsTestRule = new IntentsTestRule<>(PersonDetails.class, true, false);

    // Configuring the data we expect to see within each test.
    private Intent createIntentWithExtras() {
        Intent testIntent = new Intent();
        Event mockEvent = new Event("Test Event", "2024-01-01", "This is a test event.", 5, Event.EventType.ARTEXHIBITION);
        // Create a dummy Person object
        Person mockPerson = new Person("John Doe", 30);
        testIntent.putExtra("selectedEvent", mockEvent);
        testIntent.putExtra("selectedPerson", mockPerson);
        return testIntent;
    }

    @Test
    public void checkEventDetailsDisplay() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.person_name_text_view)).check(matches(withText("John Doe")));
        onView(withId(R.id.person_age_text_view)).check(matches(withText("30")));
    }

    @Test
    public void clickEditButton() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.goto_update_person)).perform(click());
        intended(hasComponent(UpdatePerson.class.getName()));
    }

    @Test
    public void clickDeleteButtonAndConfirm() {
        intentsTestRule.launchActivity(createIntentWithExtras());
        onView(withId(R.id.delete_person_button)).perform(click());
        onView(withText("Are you sure you want to delete this person?")).check(matches(isDisplayed()));
        onView(withText("Yes")).perform(click());
    }
}