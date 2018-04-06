//@@author emer7
package seedu.address.commons.events.logic;

import seedu.address.commons.events.BaseEvent;

/**
 * An event to inform review input is available.
 */
public class ReviewInputEvent extends BaseEvent {

    private String reviewerInput;
    private String reviewInput;

    public ReviewInputEvent(String reviewerInput, String reviewInput) {
        this.reviewerInput = reviewerInput;
        this.reviewInput = reviewInput;
    }

    public String getReviewerInput() {
        return reviewerInput;
    }

    public String getReviewInput() {
        return reviewInput;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
