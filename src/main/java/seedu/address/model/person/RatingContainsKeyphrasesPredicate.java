//@@author emer7
package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keyphrases given.
 */
public class RatingContainsKeyphrasesPredicate implements Predicate<Person> {
    private final List<String> keyphrases;

    public RatingContainsKeyphrasesPredicate(List<String> keyphrases) {
        this.keyphrases = keyphrases;
    }

    @Override
    public boolean test(Person person) {
        return keyphrases.isEmpty()
                || keyphrases.stream()
                .anyMatch(keyphrase -> StringUtil.containsWordsIgnoreCase(
                        person.getRating().value.toString(), keyphrase));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RatingContainsKeyphrasesPredicate // instanceof handles nulls
                && this.keyphrases.equals(((RatingContainsKeyphrasesPredicate) other).keyphrases)); // state check
    }

}
