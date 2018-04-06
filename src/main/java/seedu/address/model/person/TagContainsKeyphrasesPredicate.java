//@@author emer7
package seedu.address.model.person;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Person}'s {@code UniqueTagList} matches any of the keyphrases given.
 */
public class TagContainsKeyphrasesPredicate implements Predicate<Person> {
    private final List<String> keyphrases;

    public TagContainsKeyphrasesPredicate(List<String> keyphrases) {
        this.keyphrases = keyphrases;
    }

    @Override
    public boolean test(Person person) {
        Iterator tagsSetIterator = person.getTags().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(tagsSetIterator.next());
        while (tagsSetIterator.hasNext()) {
            sb.append(" " + tagsSetIterator.next());
        }
        String tagStringList = sb.toString()
                .replace("[", "")
                .replace("]", "");
        return keyphrases.isEmpty()
                ||  keyphrases.stream()
                .anyMatch(keyphrase -> StringUtil.containsWordsIgnoreCase(tagStringList, keyphrase));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeyphrasesPredicate // instanceof handles nulls
                && this.keyphrases.equals(((TagContainsKeyphrasesPredicate) other).keyphrases)); // state check
    }

}
