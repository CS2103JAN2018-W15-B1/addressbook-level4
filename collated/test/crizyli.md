# crizyli
###### \java\seedu\address\logic\commands\TodoListCommandTest.java
``` java

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;
//import seedu.address.logic.CommandHistory;
//import seedu.address.logic.UndoRedoStack;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) for {@code TodoListCommand}.
 */
public class TodoListCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {

        TodoListCommand todoListCommand = new TodoListCommand();

        // same object -> returns true
        assertTrue(todoListCommand.equals(todoListCommand));

        // different types -> returns false
        assertFalse(todoListCommand.equals(1));

        // null -> returns false
        assertFalse(todoListCommand.equals(null));
    }

    /*@Test
    public void showSuccess() {
        TodoListCommand command = new TodoListCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        String expectedMessage = TodoListCommand.MESSAGE_SUCCESS;
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
    }*/
}
```
###### \java\seedu\address\logic\parser\TestAddEventCommandParserTest.java
``` java
public class TestAddEventCommandParserTest {

    private TestAddEventCommandParser parser = new TestAddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        final String expectedTitle = "Test Event";
        final String expectedLocation = "NUS";
        final String expectedStarttime = "2018-05-15T10:00:00";
        final String expectedEndtime = "2018-05-15T12:00:00";
        final String expectedDescription = "A test event.";

        assertParseSuccess(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, new TestAddEventCommand(INDEX_FIRST_PERSON, expectedTitle,
                expectedLocation, expectedStarttime, expectedEndtime, expectedDescription));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, new TestAddEventCommand(INDEX_FIRST_PERSON, expectedTitle,
                expectedLocation, expectedStarttime, expectedEndtime, expectedDescription));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE);

        //missing title prefix
        assertParseFailure(parser, "1" + VALID_EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing location prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + VALID_EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing starttime prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + VALID_EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing endtime prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + VALID_EVENT_ENDTIME + EVENT_DESCRIPTION, expectedMessage);

        //missing description prefix
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + VALID_EVENT_DESCRIPTION, expectedMessage);

        //all prefix missing
        assertParseFailure(parser, "1" + VALID_EVENT_TITLE + VALID_EVENT_LOCATION + VALID_EVENT_STARTTIME
                + VALID_EVENT_ENDTIME + VALID_EVENT_DESCRIPTION, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid start time
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + INVALID_EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION, "Invalid date/time format: " + "2018-04-09T08:00");

        // invalid end time
        assertParseFailure(parser, "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + INVALID_EVENT_ENDTIME + EVENT_DESCRIPTION, "Invalid date/time format: " + "2018-04-09T10");

        //non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + "1" + EVENT_TITLE + EVENT_LOCATION + EVENT_STARTTIME
                + EVENT_ENDTIME + EVENT_DESCRIPTION,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TestAddEventCommand.MESSAGE_USAGE));
    }
}
```
