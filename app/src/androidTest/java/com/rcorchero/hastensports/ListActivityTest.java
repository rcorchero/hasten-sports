package com.rcorchero.hastensports;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.rcorchero.hastensports.ui.list.ListActivity;
import com.rcorchero.hastensports.ui.list.ListAdapter;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ListActivityTest {

    @Rule
    public ActivityTestRule<ListActivity> mActivityRule = new ActivityTestRule<>(ListActivity.class);

    @Before
    public void setup() {
    }
    @Test
    public void isCorrectTitleDisplayed() {
        onView(withText(R.string.app_name))
                .check(matches(isDisplayed()));
    }

    @Test
    public void isSpinnerEnabled() {
        onView(withId(R.id.spinner_types))
                .check(matches(isEnabled()));
    }

    @Test
    public void onSpinnerItemClicked_CorrectListDisplayed() {

        // Click on the Spinner and select Tennis
        onView(withId(R.id.spinner_types)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Tennis"))).perform(click());
        onView(withId(R.id.spinner_types)).check(matches(withSpinnerText(containsString("Tennis"))));

        // Check that Rafa Nadal is on the list
        onView(withId(R.id.recycler_players))
                .perform(RecyclerViewActions.scrollToHolder(withHolderTimeView("Rafa Nadal")));
    }

    public static Matcher<RecyclerView.ViewHolder> withHolderTimeView(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder,
                ListAdapter.PlayerViewHolder>(ListAdapter.PlayerViewHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found with text: " + text);
            }

            @Override
            protected boolean matchesSafely(ListAdapter.PlayerViewHolder item) {
                TextView timeViewText = item.itemView.findViewById(R.id.name);
                if (timeViewText == null) {
                    return false;
                }
                return timeViewText.getText().toString().contains(text);
            }
        };
    }
}