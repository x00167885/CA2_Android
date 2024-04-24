package com.example.eventplanner;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddPersonTest {

    @Test
    public void testAddPerson() {
        // Launch EventsList activity
        ActivityScenario.launch(EventsList.class);
        onView(withId(R.id.add_person_button_link)).perform(ViewActions.click());
        // Enter person's name and age in AddPerson activity
        onView(withId(R.id.add_person_name)).perform(ViewActions.typeText("John"));
        onView(withId(R.id.add_person_age)).perform(ViewActions.typeText("30"));
        closeSoftKeyboard();
        // Click on the add button
        onView(withId(R.id.add_person_button)).perform(ViewActions.click());
        // Finish and return.
        onView(withId(R.id.add_person)).check(ViewAssertions.doesNotExist());
    }
}
