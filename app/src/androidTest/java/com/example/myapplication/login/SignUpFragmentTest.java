package com.example.myapplication.login;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.myapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SignUpFragmentTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Test
    public void signUpFragmentTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.sign_up), withText("No account? Register here for FREE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        0),
                                3),
                        isDisplayed()));
        appCompatTextView.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.email_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("mail"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.pass_field),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("gg"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.confirm_pass),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("ggg"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.confirm_pass), withText("ggg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText4.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.confirm_pass), withText("ggg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("gg"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.confirm_pass), withText("gg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.email_field), withText("mail"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("mail@a.com"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.email_field), withText("mail@a.com"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                1),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.useThisFragmentID),
                                childAtPosition(
                                        withId(R.id.login_layout),
                                        0)),
                        1),
                        isDisplayed()));
        relativeLayout.perform(click());

        pressBack();

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.pass_field), withText("gg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("gggggg"));

        ViewInteraction appCompatEditText10 = onView(
                allOf(withId(R.id.pass_field), withText("gggggg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                2),
                        isDisplayed()));
        appCompatEditText10.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText11 = onView(
                allOf(withId(R.id.confirm_pass), withText("gg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText11.perform(replaceText("gggggh"));

        ViewInteraction appCompatEditText12 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText12.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText13 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText13.perform(pressImeActionButton());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText14 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText14.perform(click());

        ViewInteraction appCompatEditText15 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText15.perform(replaceText("gggggg"));

        ViewInteraction appCompatEditText16 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText16.perform(closeSoftKeyboard());

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.confirm_pass), withText("gggggg"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText17.perform(pressImeActionButton());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.sign_up), withText("Sign Up"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.useThisFragmentID),
                                        1),
                                4),
                        isDisplayed()));
        appCompatButton5.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
