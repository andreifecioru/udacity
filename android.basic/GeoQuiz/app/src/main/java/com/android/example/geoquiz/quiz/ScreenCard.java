package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;
import android.widget.TextView;

import com.android.example.geoquiz.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base abstraction for displaying info on the screen in a region with a top
 * title strip and a content section underneath it.
 *
 * This base implementation is only concerned with displaying the title and
 * clearing the content area. It is the responsibility of the specialized
 * child classes to provide the functionality for displaying the actual content.
 */
public abstract class ScreenCard {
    String mTitle;
    final ViewGroup mScreenCardContainer;

    @BindView(R.id.screen_card_title) TextView mTitleTextView;

    /**
     * Creates a ScreenCard instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the screen card will
     *                            be rendered.
     */
    ScreenCard(ViewGroup screenCardContainer) {
        mScreenCardContainer = screenCardContainer;
        ButterKnife.bind(this, mScreenCardContainer);
    }

    /**
     * Displays the content of the screen card on the device screen:
     *  - renders the tile section
     *  - clears any previous info in the content section
     *  - renders the content section
     */
    public void display() {
        displayTitle();
        clearContent();
        displayContent(mScreenCardContainer);
    }

    /**
     * Clears the content section in the screen card.
     *
     * The content section is considered to be the 2nd child of the
     * parent container (1st being the title section).
     */
    private void clearContent() {
        // remove the 2nd child of the parent container (i.e. index 1)
        if (mScreenCardContainer.getChildAt(1) != null) {
            mScreenCardContainer.removeViewAt(1);
        }
    }

    /**
     * Renders the title section by populating the associated TextView.
     */
    private void displayTitle() {
        mTitleTextView.setText(mTitle);
    }

    /**
     * Renders the content section (abstract).
     *
     * It is up to the specialized child class to provide a custom implementation
     * for this operation.
     *
     * @param screenCardContainer The parent container where the screen card will
     *                            be rendered.
     */
    abstract protected void displayContent(ViewGroup screenCardContainer);
}
