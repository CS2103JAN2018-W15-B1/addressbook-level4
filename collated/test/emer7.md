# emer7
###### \java\guitests\guihandles\DetailPanelHandle.java
``` java
    public static final String BROWSER_PANEL_ID = "#detailPanel";
    private static final String NAME_FIELD_ID = "#name";
    private static final String ADDRESS_FIELD_ID = "#address";
    private static final String PHONE_FIELD_ID = "#phone";
    private static final String EMAIL_FIELD_ID = "#email";
    private static final String TAGS_FIELD_ID = "#tags";
```
###### \java\guitests\guihandles\DetailPanelHandle.java
``` java
    private final Label nameLabel;
    private final Label addressLabel;
    private final Label phoneLabel;
    private final Label emailLabel;
    private final List<Label> tagLabels;

    private Label oldNameLabel;
    private Label oldAddressLabel;
    private Label oldPhoneLabel;
    private Label oldEmailLabel;
    private List<Label> oldTagLabels;
```
###### \java\guitests\guihandles\DetailPanelHandle.java
``` java
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.addressLabel = getChildNode(ADDRESS_FIELD_ID);
        this.phoneLabel = getChildNode(PHONE_FIELD_ID);
        this.emailLabel = getChildNode(EMAIL_FIELD_ID);

        Region tagsContainer = getChildNode(TAGS_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
```
###### \java\guitests\guihandles\DetailPanelHandle.java
``` java
    public String getName() {
        return nameLabel.getText();
    }

    public String getAddress() {
        return addressLabel.getText();
    }

    public String getPhone() {
        return phoneLabel.getText();
    }

    public String getEmail() {
        return emailLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }

    /**
     * Remember the current Person details
     */
    public void rememberPersonDetail() {
        oldNameLabel = nameLabel;
        oldAddressLabel = addressLabel;
        oldPhoneLabel = phoneLabel;
        oldEmailLabel = emailLabel;
        oldTagLabels = tagLabels;
    }

    public boolean isDetailChanged() {
        return !(oldNameLabel.getText().equals(getName())
                && oldAddressLabel.getText().equals(getAddress())
                && oldPhoneLabel.getText().equals(getPhone())
                && oldEmailLabel.getText().equals(getEmail())
                && oldTagLabels
                .stream().map(Label::getText).collect(Collectors.toList())
                .equals(getTags()));
    }

    public boolean isFieldsEmpty() {
        return nameLabel.getText().equals("")
                && addressLabel.getText().equals("")
                && phoneLabel.getText().equals("")
                && emailLabel.getText().equals("")
                && oldTagLabels == null;
    }
```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
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
```
###### \java\seedu\address\logic\commands\FindCommandTest.java
``` java
        FindCommand command =
                new FindCommand(
                        new FieldContainKeyphrasesPredicate(
                                Arrays.asList(nameInput.split("\\s+")),
                                Arrays.asList(tagInput.split("\\s+")),
                                Arrays.asList(ratingInput.split("\\s+"))));
```
###### \java\seedu\address\logic\commands\ReviewCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.review.Review;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

public class ReviewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person reviewedPerson = new PersonBuilder().withEmail("alice@example.com").withRating("1")
                .withReviews("supervisor@example.com\nLazy")
                .build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(reviewedPerson)
                .withReview("supervisor@example.com\nLazy").build();

        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(ReviewCommand.MESSAGE_REVIEW_PERSON_SUCCESS, reviewedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), reviewedPerson);

        assertCommandSuccess(reviewCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON, new EditCommand.EditPersonDescriptor());
        Person reviewedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        reviewedPerson.setReviews(new HashSet<Review>());

        String expectedMessage = String.format(ReviewCommand.MESSAGE_REVIEW_PERSON_SUCCESS, reviewedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(reviewCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person reviewedPerson = new PersonBuilder(personInFilteredList)
                .withReviews("supervisor@example.com\nLazy")
                .build();
        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withReview("supervisor@example.com\nLazy").build());

        String expectedMessage = String.format(ReviewCommand.MESSAGE_REVIEW_PERSON_SUCCESS,
                reviewedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), reviewedPerson);

        assertCommandSuccess(reviewCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .build();
        ReviewCommand reviewCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(reviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Review filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        ReviewCommand reviewCommand = prepareCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(reviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person reviewedPerson = new PersonBuilder()
                .withEmail("alice@example.com")
                .withRating("5")
                .withReviews("supervisor@example.com\nLazy")
                .build();
        Person personToReview = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(reviewedPerson)
                .withReview("supervisor@example.com\nLazy")
                .build();
        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // review -> first person reviewed
        reviewCommand.execute();
        undoRedoStack.push(reviewCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first person reviewed again
        expectedModel.updatePerson(personToReview, reviewedPerson);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .build();
        ReviewCommand reviewCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> reviewCommand not pushed into undoRedoStack
        assertCommandFailure(reviewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Reviews a {@code Person} from a filtered list.
     * 2. Undo the review.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously reviewed person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the review. This ensures {@code RedoCommand} reviews the person object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonReviewed() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Person reviewedPerson = new PersonBuilder()
                .withName("Benson Meier")
                .withPhone("98765432")
                .withEmail("johnd@example.com")
                .withAddress("311, Clementi Ave 2, #02-25")
                .withTags("owesMoney", "friends")
                .withReviews("supervisor@example.com\nLazy")
                .build();
        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(reviewedPerson)
                .withReview("supervisor@example.com\nLazy")
                .build();
        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        Person personToReview = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // review -> reviews second person in unfiltered person list / first person in filtered person list
        reviewCommand.execute();
        undoRedoStack.push(reviewCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all persons
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updatePerson(personToReview, reviewedPerson);
        assertNotEquals(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), personToReview);
        // redo -> reviews same second person in unfiltered person list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final ReviewCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditCommand.EditPersonDescriptor copyDescriptor = new EditCommand.EditPersonDescriptor(DESC_AMY);
        ReviewCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ReviewCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new ReviewCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code ReviewCommand} with parameters {@code index} and {@code descriptor}
     */
    private ReviewCommand prepareCommand(Index index, EditCommand.EditPersonDescriptor descriptor) {
        ReviewCommand reviewCommand = new ReviewCommand(index, descriptor);
        reviewCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return reviewCommand;
    }
}
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
                        + " t/"
                        + tagKeyphrases.stream().collect(Collectors.joining(" t/"))
                        + " r/"
                        + ratingKeyphrases.stream().collect(Collectors.joining(" r/")));
        assertEquals(new FindCommand(
                new FieldContainKeyphrasesPredicate(
                        nameKeyphrases, tagKeyphrases, ratingKeyphrases)),
                        command);
```
###### \java\seedu\address\logic\parser\ReviewCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.ReviewCommand;

public class ReviewCommandParserTest {
    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ReviewCommand.MESSAGE_USAGE);

    private ReviewCommandParser parser = new ReviewCommandParser();

    @Test
    public void parse_indexInvalid_failure() {
        assertParseFailure(parser, "a", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_indexZeroOrLess_failure() {
        assertParseFailure(parser, "0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "-1", MESSAGE_INVALID_FORMAT);
    }
}
```
###### \java\seedu\address\model\person\FieldContainKeyphrasesPredicateTest.java
``` java
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
```
###### \java\seedu\address\model\person\RatingContainsKeyphrasesPredicateTest.java
``` java
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
```
###### \java\seedu\address\testutil\EditPersonDescriptorBuilder.java
``` java
    /**
     * Sets the {@code Review} of the {@code EditPersonDescriptor} that we are building.
     */
    public EditPersonDescriptorBuilder withReview(String review) {
        HashSet<Review> reviewSet = new HashSet<Review>();
        reviewSet.add(new Review(review));
        descriptor.setReviews(reviewSet);
        return this;
    }
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
    /**
     * Sets the {@code Review} of the {@code Person} that we are building.
     */
    public PersonBuilder withReviews(String ... reviews) {
        this.reviews = SampleDataUtil.getReviewSet(reviews);
        return this;
    }
```
###### \java\seedu\address\testutil\PersonBuilder.java
``` java
        Person toReturn = new Person(name, phone, email, address, tags, calendarId);
        toReturn.setRating(rating);
        toReturn.setReviews(reviews);
        return toReturn;
```
###### \java\seedu\address\ui\DetailPanelTest.java
``` java
        detailPanelHandle = new DetailPanelHandle(detailPanel.getRoot());
        assertPanelDisplay(new PersonCardHandle(selectionChangedPersonCardStub.getRoot()), detailPanelHandle);
    }

    /**
     * Asserts that {@code personCard} displays the details of {@code expectedPerson} correctly and matches
     * {@code expectedId}.
     */
    private void assertPanelDisplay(PersonCardHandle expectedPersonCard, DetailPanelHandle detailPanelHandle) {
        guiRobot.pauseForHuman();
        assertPanelDisplaysPerson(expectedPersonCard, detailPanelHandle);
```
###### \java\seedu\address\ui\testutil\GuiTestAssert.java
``` java
    /**
     * Asserts that {@code actualPanel} displays the details of {@code expectedPerson}.
     */
    public static void assertPanelDisplaysPerson(PersonCardHandle expectedPersonCard, DetailPanelHandle actualPanel) {
        assertEquals(expectedPersonCard.getName(), actualPanel.getName());
        assertEquals(expectedPersonCard.getPhone(), actualPanel.getPhone());
        assertEquals(expectedPersonCard.getEmail(), actualPanel.getEmail());
        assertEquals(expectedPersonCard.getAddress(), actualPanel.getAddress());
        assertEquals(expectedPersonCard.getTags(), actualPanel.getTags());
    }
```
