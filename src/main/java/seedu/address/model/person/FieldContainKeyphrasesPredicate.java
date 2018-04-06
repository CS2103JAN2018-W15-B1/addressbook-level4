//@@author emer7
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that a {@code Person}'s fields matches any of the keyphrases given.
 */
public class FieldContainKeyphrasesPredicate implements Predicate<Person> {
    private final Predicate<Person> namePredicate;
    private final Predicate<Person> tagPredicate;
    private final Predicate<Person> ratingPredicate;

    public FieldContainKeyphrasesPredicate(List<String> nameKeyphrases,
                                           List<String> tagKeyphrases,
                                           List<String> ratingKeyphrases) {
        this.namePredicate = new NameContainsKeyphrasesPredicate(nameKeyphrases);
        this.tagPredicate = new TagContainsKeyphrasesPredicate(tagKeyphrases);
        this.ratingPredicate = new RatingContainsKeyphrasesPredicate(ratingKeyphrases);
    }


    @Override
    public boolean test(Person person) {
        return namePredicate.test(person) && tagPredicate.test(person) && ratingPredicate.test(person);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FieldContainKeyphrasesPredicate // instanceof handles nulls
                && this.namePredicate.equals(((FieldContainKeyphrasesPredicate) other).namePredicate)
                && this.tagPredicate.equals(((FieldContainKeyphrasesPredicate) other).tagPredicate)
                && this.ratingPredicate.equals(((
                        FieldContainKeyphrasesPredicate) other).ratingPredicate)); // state check
    }

}
