package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class FieldContainKeyphrasesPredicateTest {

    @Test
    public void equals() {
        List<String> firstNamePredicateKeyphraseList = Collections.singletonList("first");
        List<String> secondNamePredicateKeyphraseList = Arrays.asList("first", "second");
        List<String> firstTagPredicateKeyphraseList = Collections.singletonList("first");
        List<String> secondTagPredicateKeyphraseList = Arrays.asList("first", "second");
        List<String> firstRatingPredicateKeyphraseList = Collections.singletonList("first");
        List<String> secondRatingPredicateKeyphraseList = Collections.singletonList("second");

        FieldContainKeyphrasesPredicate firstPredicate =
                new FieldContainKeyphrasesPredicate(
                        firstNamePredicateKeyphraseList,
                        firstTagPredicateKeyphraseList,
                        firstRatingPredicateKeyphraseList);
        FieldContainKeyphrasesPredicate secondPredicate =
                new FieldContainKeyphrasesPredicate(
                        secondNamePredicateKeyphraseList,
                        secondTagPredicateKeyphraseList,
                        secondRatingPredicateKeyphraseList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        FieldContainKeyphrasesPredicate firstPredicateCopy = new FieldContainKeyphrasesPredicate(
                firstNamePredicateKeyphraseList, firstTagPredicateKeyphraseList, firstRatingPredicateKeyphraseList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different person -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_containKeyphrases_returnsTrue() {
        // All zero keyphrase
        FieldContainKeyphrasesPredicate predicate = new FieldContainKeyphrasesPredicate(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // All one keyphrase
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("Alice"),
                Collections.singletonList("Friends"),
                Collections.singletonList("3"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // Name one keyphrase
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("Alice Bob"),
                Collections.emptyList(),
                Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // Tag one keyphrase
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.emptyList(),
                Collections.singletonList("Friends Family"),
                Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // Rating one keyphrase
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList("3"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // Both name and tag multiple keyphrases, but only one matches
        predicate = new FieldContainKeyphrasesPredicate(
                Arrays.asList("Alice", "Carol"),
                Arrays.asList("Friends", "Enemy"),
                Collections.emptyList());
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));

        // Both name and tag mixed-case keyphrase
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("aLIce"),
                Collections.singletonList("fRIeNDs"),
                Collections.singletonList("3"));
        assertTrue(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends", "Family")
                .withRating("3")
                .build()));
    }

    @Test
    public void test_notContainKeyphrases_returnsFalse() {
        // Non-matching keyphrase
        FieldContainKeyphrasesPredicate predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("Carol"),
                Collections.singletonList("Enemy"),
                Collections.singletonList("3"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice Bob")
                .withTags("Friends")
                .withRating("3")
                .build()));

        // Keyphrase match phone, email, address, and tags, but does not match name
        predicate = new FieldContainKeyphrasesPredicate(
                Arrays.asList("12345 alice@email.com Main Street"),
                Collections.singletonList("Friends"),
                Collections.singletonList("3"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("Friends")
                .withRating("3")
                .build()));

        // Keyphrase match name, phone, email, and address, but does not match tags
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("Alice"),
                Arrays.asList("12345 alice@email.com Main Street"),
                Collections.singletonList("3"));
        assertFalse(predicate.test(new PersonBuilder()
                .withName("Alice")
                .withPhone("12345")
                .withEmail("alice@email.com")
                .withAddress("Main Street")
                .withTags("Friends")
                .withRating("3")
                .build()));

        // Keyphrase match name, phone, email, address, and tags, but does not match rating
        predicate = new FieldContainKeyphrasesPredicate(
                Collections.singletonList("Alice"),
                Collections.singletonList("Friends"),
                Arrays.asList("12345 alice@email.com Main Street"));
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
