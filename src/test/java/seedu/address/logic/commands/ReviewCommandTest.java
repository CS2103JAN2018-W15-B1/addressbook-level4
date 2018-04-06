//@@author emer7
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
                .withReviews("sales@example.com\nLazy").build();

        ReviewCommand reviewCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        HashSet<Review> newReviews = new HashSet<>();
        newReviews.add(new Review("supervisor@example.com\nLazy"));
        newReviews.add(new Review("sales@example.com\nLazy"));
        reviewedPerson.setReviews(newReviews);

        String expectedMessage = String.format(ReviewCommand.MESSAGE_REVIEW_PERSON_SUCCESS, reviewedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), reviewedPerson);

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
                new EditPersonDescriptorBuilder().withReviews("sales@example.com\nLazy").build());

        HashSet<Review> newReviews = new HashSet<>();
        newReviews.add(new Review("supervisor@example.com\nLazy"));
        newReviews.add(new Review("sales@example.com\nLazy"));
        reviewedPerson.setReviews(newReviews);

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
                .withReviews("sales@example.com\nLazy")
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
                .withReviews("sales@example.com\nLazy")
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
