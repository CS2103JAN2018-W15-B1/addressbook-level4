//@@author emer7
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class RatingContainsKeyphrasesPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeyphraseList = Collections.singletonList("first");
        List<String> secondPredicateKeyphraseList = Arrays.asList("first", "second");

        RatingContainsKeyphrasesPredicate firstPredicate = new RatingContainsKeyphrasesPredicate(
                firstPredicateKeyphraseList);
        RatingContainsKeyphrasesPredicate secondPredicate = new RatingContainsKeyphrasesPredicate(
                secondPredicateKeyphraseList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        RatingContainsKeyphrasesPredicate firstPredicateCopy = new RatingContainsKeyphrasesPredicate(
                firstPredicateKeyphraseList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_ratingContainsKeyphrases_returnsTrue() {
        // Zero keyphrase
        RatingContainsKeyphrasesPredicate predicate = new RatingContainsKeyphrasesPredicate(Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder().withRating("2").build()));

        // One keyphrase
        predicate = new RatingContainsKeyphrasesPredicate(Collections.singletonList("3"));
        assertTrue(predicate.test(new PersonBuilder().withRating("3").build()));

        // Multiple keyphrases
        predicate = new RatingContainsKeyphrasesPredicate(Arrays.asList("3", "4"));
        assertTrue(predicate.test(new PersonBuilder().withRating("4").build()));
    }

    @Test
    public void test_ratingDoesNotContainKeyphrases_returnsFalse() {
        // Non-matching keyphrase
        RatingContainsKeyphrasesPredicate predicate = new RatingContainsKeyphrasesPredicate(
                Collections.singletonList("2"));
        assertFalse(predicate.test(new PersonBuilder().withRating("1").build()));

        // Keyphrase match name, phone, email, address, and tags, but does not match rating
        predicate = new RatingContainsKeyphrasesPredicate(
                Arrays.asList("Alice 12345 alice@email.com Main Street Friends"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("Friends")
                .withRating("3")
                .build()));
    }
}
