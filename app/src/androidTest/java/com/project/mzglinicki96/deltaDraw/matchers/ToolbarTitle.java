package com.project.mzglinicki96.deltaDraw.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by mazg on 08.03.2017.
 */

public class ToolbarTitle extends BoundedMatcher<Object, Toolbar> {

    private final Matcher<CharSequence> textMatcher;

    public ToolbarTitle(final Class<? extends Toolbar> expectedType, final Matcher<CharSequence> textMatcher) {
        super(expectedType);
        this.textMatcher = textMatcher;
    }

    @Override
    protected boolean matchesSafely(final Toolbar toolbar) {
        return textMatcher.matches(toolbar.getTitle());
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("with toolbar title: ");
        textMatcher.describeTo(description);
    }
}
