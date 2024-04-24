package com.example.eventplanner;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventsListTest {

    @Test
    public void testNavigateToEventsList() {
        ActivityScenario.launch(MainActivity.class);
        // Go to events list.
        Espresso.onView(withId(R.id.goto_events)).perform(click());
        // Checking launch.
        Espresso.onView(withId(R.id.eventsList)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddEventButton() {
        ActivityScenario.launch(EventsList.class);
        Espresso.onView(withId(R.id.add_event_button_link)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.add_event_button_link)).perform(click());
        // Check launch
        Espresso.onView(withId(R.id.add_event)).check(matches(isDisplayed()));
    }

    @Test
    public void testAddPersonButton() {
        ActivityScenario.launch(EventsList.class);
        // Check if the add person button is displayed.
        Espresso.onView(withId(R.id.add_person_button_link)).check(matches(isDisplayed()));
        // Click on the add person button.
        Espresso.onView(withId(R.id.add_person_button_link)).perform(click());
        // Check launch
        Espresso.onView(withId(R.id.add_person)).check(matches(isDisplayed()));
    }

    @Test
    public void testBackToWelcomePageButton() {
        ActivityScenario.launch(MainActivity.class);
        // Click on the "Go to Events" button in MainActivity
        Espresso.onView(withId(R.id.goto_events)).perform(click());
        // Display checks.
        Espresso.onView(withId(R.id.eventsList)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.back_to_welcome_page)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.back_to_welcome_page)).perform(click());
        Espresso.onView(withId(R.id.goto_events)).check(matches(isDisplayed()));
    }

    @Test
    public void testEventDetailsNavigation() {
        ActivityScenario.launch(EventsList.class);
        // Click on the first item in the event list.
        Espresso.onData(anything()).inAdapterView(withId(R.id.eventsList)).atPosition(0).perform(click());
        // Check launch
        Espresso.onView(withId(R.id.event_details)).check(matches(isDisplayed()));
    }
}