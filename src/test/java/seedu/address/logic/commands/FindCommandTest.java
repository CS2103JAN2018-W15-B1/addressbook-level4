package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FieldContainKeyphrasesPredicate;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        //@@author emer7
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
        //@@author

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeyphrases_allPersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 7);
        FindCommand command = prepareCommand(" ", " ", " ");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_multipleKeyphrases_multiplePersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2);
        FindCommand command = prepareCommand("Kurz Elle Kunz", "Friends Family", "5 -1");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(CARL, ELLE));
    }

    /**
     * Parses {@code userInput} into a {@code FindCommand}.
     */
    private FindCommand prepareCommand(String nameInput, String tagInput, String ratingInput) {
        //@@author emer7
        FindCommand command =
                new FindCommand(
                        new FieldContainKeyphrasesPredicate(
                                Arrays.asList(nameInput.split("\\s+")),
                                Arrays.asList(tagInput.split("\\s+")),
                                Arrays.asList(ratingInput.split("\\s+"))));
        //@@author
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<Person>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(FindCommand command, String expectedMessage, List<Person> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
