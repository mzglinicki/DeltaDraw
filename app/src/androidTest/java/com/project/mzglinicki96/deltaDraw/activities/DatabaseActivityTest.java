package com.project.mzglinicki96.deltaDraw.activities;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;

import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.matchers.ToastMatcher;
import com.project.mzglinicki96.deltaDraw.matchers.ToolbarTitle;
import com.project.mzglinicki96.deltaDraw.matchers.Utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by mazg on 07.03.2017.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseActivityTest {

    @Rule
    public final ActivityTestRule<DatabaseActivity> activityRule = new ActivityTestRule<>(DatabaseActivity.class);

    private final static int DATABASE_TEST_ITEM = 3;
    private final static int RANDOM_DATABASE_ITEM = 1;
    private final static int DATABASE_RECYCLE_VIEW = R.id.recycleView;
    private final static int FLOATING_BUTTON = R.id.floatingButton;

    @Test
    public void initDialogDisplayed() {
        //check if alertDialog is shown after on FloatingActionButton click.
        displayAlertDialog();
    }

    @Test
    public void uncompletedInitData() {
        displayAlertDialog();
        onView(withText("Rozpocznij")).perform(click());
        onView(withText(R.string.uncompletedData)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void longClickOnItem() {
        longClickOnItem(DATABASE_TEST_ITEM);
        longClickOnItem(RANDOM_DATABASE_ITEM);
        onView(withText(R.string.acceptPreviousChanges)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
    }

    @Test
    public void toolbarTitle() {
        final CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.app_name);
        matchToolbarTitle(title);
    }

    @Test
    public void startNewDrawTest() {
        performClick(FLOATING_BUTTON);
        setupNewText(R.id.textPictureTitle, "testDraw");
        setupNewText(R.id.textAuthor, "tester");
        onView(allOf(withId(android.R.id.button1), withText("Rozpocznij"))).perform(scrollTo(), click());
        onView(withId(R.id.drawActivityViewGroup)).check(matches(isDisplayed()));
    }

    @Test
    public void changeRecordData() {
        final String pictureTestTitle = "title";
        final String pictureTestAuthor = "author";
        changeRecordData(pictureTestTitle, pictureTestAuthor);
    }

    @Test
    public void deleteItemFromList() {
        changeRecordData("toRemove", "toRemove");
        onView(withId(DATABASE_RECYCLE_VIEW)).perform(RecyclerViewActions.actionOnItemAtPosition(DATABASE_TEST_ITEM, swipeRight()));
        onView(allOf(withText(R.string.undo), isDisplayed())).perform(click());
        onView(withId(DATABASE_RECYCLE_VIEW)).check(matches(Utils.atPosition(DATABASE_TEST_ITEM, hasDescendant(withText("toRemove")))));
    }

    private void changeRecordData(final String title, final String author) {
        longClickOnItem(DATABASE_TEST_ITEM);
        setupNewText(R.id.pictureTitleEditField, title);
        setupNewText(R.id.pictureAuthorEditField, author);
        performClick(R.id.acceptChangesImageBtn);
        assertNewText(title);
        assertNewText(author);
    }

    private void assertNewText(final String newText) {
        onView(allOf(withId(R.id.pictureTitleField), isDisplayed(), withText(newText)));
    }

    private void performClick(final int itemResId) {
        onView(allOf(withId(itemResId), isDisplayed())).perform(click());
    }

    private void setupNewText(final int editTextResId, final String newInput) {
        onView(allOf(withId(editTextResId), isDisplayed())).perform(replaceText(newInput), closeSoftKeyboard());
    }

    private void longClickOnItem(final int position) {
        onView(withId(DATABASE_RECYCLE_VIEW)).perform(RecyclerViewActions.actionOnItemAtPosition(position, longClick()));
    }

    private void matchToolbarTitle(final CharSequence title) {
        onView(isAssignableFrom(Toolbar.class))
                .check(matches(new ToolbarTitle(Toolbar.class, is(title))));
    }

    private void displayAlertDialog() {
        performClick(FLOATING_BUTTON);
        onView(withId(R.id.newPictureDialog))
                .check(matches(isDisplayed()));
    }

/**
 *  How to test toast with matcher
 *
 *  1. Test if the Toast Message is Displayed
 *  onView(withText(R.string.message)).inRoot(new ToastMatcher()).check(matches(isDisplayed()));
 *
 *  2. Test if the Toast Message is not Displayed
 *  onView(withText(R.string.message)).inRoot(new ToastMatcher()).check(matches(not(isDisplayed())));
 *
 *  3. Test id the Toast contains specific Text Message
 *  onView(withText(R.string.mssage)).inRoot(new ToastMatcher()).check(matches(withText("Invalid Name"));
 */
}