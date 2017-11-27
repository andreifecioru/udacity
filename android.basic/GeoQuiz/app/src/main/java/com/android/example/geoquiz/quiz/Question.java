package com.android.example.geoquiz.quiz;

import android.view.ViewGroup;

/**
 * Base abstraction for displaying a question on the screen.
 *
 * A question is implemented as a sub-type of {@link ScreenCard}.
 * The question text is displayed in the screen cards's title section
 * while the content area is reserved for the question choices/response.
 *
 * This base abstraction is only concerned with displaying the question
 * text in the title area of the screen card. It is up to the child classes
 * to provide custom logic for displaying the content for the user to provide
 * an answer (in the screen card's content area).
 */
abstract class Question extends ScreenCard {
    /**
     * Custom exception used for answer validation purposes.
     */
    static class InvalidAnswerException extends RuntimeException {
        InvalidAnswerException(String message) { super(message); }
    }

    String mCorrectAnswer;

    /**
     * Creates a Question instance inside a parent container.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     */
    Question(ViewGroup screenCardContainer) {
        super(screenCardContainer);
    }

    /**
     * Checks the user-provided answer against what we know to be the correct one.
     *
     * @return <code>true</code> if answer is correct <code>false</code> otherwise.
     */
    boolean checkAnswer() {
        String answer = getAnswer(mScreenCardContainer);
        return (answer != null) && answer.equals(mCorrectAnswer);
    }

    /**
     * Captures the user-provided answer in a <code>String</code> value.
     *
     * The return value from this method is compared with what we know to be
     * the correct answer (see {@link Question#checkAnswer()}.
     *
     * It is up to the specialized child class to provide a custom implementation
     * for this operation.
     *
     * @param screenCardContainer The parent container where the question will
     *                            be rendered.
     *
     * @return a <code>String</code> representation of the user-provided answer.
     */
    abstract protected String getAnswer(ViewGroup screenCardContainer);

    @Override
    public String toString() { return mTitle; }
}
