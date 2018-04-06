//@@author emer7
package seedu.address.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.review.Review;

/**
 * JAXB-friendly adapted version of the Review.
 */
public class XmlAdaptedReview {

    @XmlValue
    private String review;

    /**
     * Constructs an XmlAdaptedReview.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedReview() {}

    /**
     * Constructs a {@code XmlAdaptedReview} with the given {@code review}.
     */
    public XmlAdaptedReview(String review) {
        this.review = review;
    }

    /**
     * Converts a given Review into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedReview(Review source) {
        review = source.toString();
    }

    /**
     * Converts this jaxb-friendly adapted review object into the model's Review object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Review toModelType() throws IllegalValueException {
        if (!Review.isValidCombined(review)) {
            throw new IllegalValueException(Review.MESSAGE_REVIEW_CONSTRAINTS);
        }
        return new Review(review);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedReview)) {
            return false;
        }

        return review.equals(((XmlAdaptedReview) other).review);
    }
}
